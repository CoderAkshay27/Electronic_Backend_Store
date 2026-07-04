package com.akshay.electronic.store.ElectronicStore.services.impl;

import com.akshay.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.akshay.electronic.store.ElectronicStore.dtos.UserDTO;
import com.akshay.electronic.store.ElectronicStore.entities.User;
import com.akshay.electronic.store.ElectronicStore.exception.BadApiRequest;
import com.akshay.electronic.store.ElectronicStore.exception.ResourceNotFoundException;
import com.akshay.electronic.store.ElectronicStore.helper.Helper;
import com.akshay.electronic.store.ElectronicStore.repositories.UserRepository;
import com.akshay.electronic.store.ElectronicStore.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${user.profile.image.path}")
    private String path;
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDTO create(UserDTO userDto) {

        String userID = UUID.randomUUID().toString();
        userDto.setUserId(userID);

        User user = DtoToEntity(userDto);
        User savedUser = userRepository.save(user);
        return entityToDto(savedUser);
    }


    @Override
    public UserDTO updateUser(UserDTO userDto, String userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new ResourceNotFoundException("User Not Found With Given ID!!!"));
        user.setAbout(userDto.getAbout());
        user.setName(userDto.getName());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        User savedUser = userRepository.save(user);
        return entityToDto(savedUser);
    }
    @Override
    public void deleteUser(String userID) {

        User user = userRepository.findById(userID)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User Not Found With Given ID!!!"));

        String imageName = user.getImageName();

        // Delete image if it exists
        if (imageName != null && !imageName.isBlank()) {

            String fullPath = path + File.separator + imageName;

            try {
                Path imagePath = Paths.get(fullPath);

                if (Files.exists(imagePath)) {
                    Files.delete(imagePath);
                    logger.info("Image deleted successfully: {}", imageName);
                } else {
                    logger.info("Image not found: {}", imageName);
                }

            } catch (IOException e) {
                logger.error("Error deleting image: {}", imageName, e);
                throw new BadApiRequest("Unable to delete user image.");
            }
        }

        // Delete user from database
        userRepository.delete(user);

        logger.info("User deleted successfully with ID: {}", userID);
    }

    @Override
    public PageableResponse<UserDTO> getAllUser(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> page = userRepository.findAll(pageable);

        return Helper.getPageableResponse(page, this::entityToDto);
    }

    @Override
    public UserDTO getUserByID(String userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new ResourceNotFoundException("User Not Found With Given ID!!!"));
        return entityToDto(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User Not Found With Given Email !!!"));
        return entityToDto(user);
    }

    public List<UserDTO> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);


        return users.stream().map(this::entityToDto).toList();
    }

    private User DtoToEntity(UserDTO userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .about(userDto.getAbout())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .password(userDto.getPassword())
//                .build();
//        return user;
        return mapper.map(userDto, User.class);
    }

    private UserDTO entityToDto(User savedUser) {
//        UserDTO user = UserDTO.builder()
//                .userId(savedUser.getUserId())
//                .about(savedUser.getAbout())
//                .email(savedUser.getEmail())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName())
//                .password(savedUser.getPassword())
//                .name(savedUser.getName()).build();
//
//        return user;

        return mapper.map(savedUser, UserDTO.class);
    }
}
