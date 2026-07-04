package com.akshay.electronic.store.ElectronicStore.controllers;

import com.akshay.electronic.store.ElectronicStore.dtos.ApiResponseMessage;
import com.akshay.electronic.store.ElectronicStore.dtos.ImageApiRespone;
import com.akshay.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.akshay.electronic.store.ElectronicStore.dtos.UserDTO;
import com.akshay.electronic.store.ElectronicStore.services.FileService;
import com.akshay.electronic.store.ElectronicStore.services.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    private Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userdto) {
        UserDTO user = userService.create(userdto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> UpadteUser(@PathVariable("userId") String userId, @Valid @RequestBody UserDTO userdto) {
        UserDTO user = userService.updateUser(userdto, userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deletrUser(@PathVariable("userId") String userId) throws IOException {
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage.builder().message("User is Deleted Successfully !!!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<UserDTO>> getAllUser(
            @RequestParam(value = "PageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "PageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

    ) {
        return new ResponseEntity<>(userService.getAllUser(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getSingleUser(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getUserByID(userId), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDTO
            >> searchUser(@PathVariable String keywords) {
        return new ResponseEntity<>(userService.searchUser(keywords), HttpStatus.OK);
    }

    //upload
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageApiRespone> uploadFile(
            @RequestParam("userImage") MultipartFile file,
            @PathVariable String userId) throws IOException {

        String imageName = fileService.uploadFile(file, imageUploadPath);
        UserDTO user = userService.getUserByID(userId);
        user.setImageName(imageName);
        userService.updateUser(user, userId);
        ImageApiRespone response = ImageApiRespone.builder()
                .filename(imageName)
                .success(true)
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    //serve image
    @GetMapping("/image/{userId}")
    public void serveImage(@PathVariable("userId") String userId, HttpServletResponse response) throws IOException {
        UserDTO user = userService.getUserByID(userId);
        logger.info("User name : {}", user.getName());
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
