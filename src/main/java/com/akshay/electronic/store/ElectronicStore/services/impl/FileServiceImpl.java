package com.akshay.electronic.store.ElectronicStore.services.impl;

import com.akshay.electronic.store.ElectronicStore.exception.BadApiRequest;
import com.akshay.electronic.store.ElectronicStore.services.FileService;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger =
            LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        String originalFilename = file.getOriginalFilename();

        logger.info("Filename : {}", originalFilename);

        String filename = UUID.randomUUID().toString();

        String extension =
                originalFilename.substring(originalFilename.lastIndexOf("."));

        String filenameWithExtension = filename + extension;

        String fullPathWithFilename =
                path + filenameWithExtension;

        if (extension.equalsIgnoreCase(".png")
                || extension.equalsIgnoreCase(".jpeg")
                || extension.equalsIgnoreCase(".jpg")) {

            File folder = new File(path);
logger.info("full path name : {}",fullPathWithFilename);
            if (!folder.exists()) {
                logger.info("file extension :{}",extension);
                folder.mkdirs();
            }

            Files.copy(
                    file.getInputStream(),
                    Paths.get(fullPathWithFilename)
            );

            return filenameWithExtension;

        } else {

            throw new BadApiRequest(
                    "File with extension " + extension + " not allowed."
            );
        }
    }
    @Override
    public String uploadFileService(MultipartFile file, String path) {
        return "";
    }

    @Override
    public InputStream getResource(String path, String name)
            throws FileNotFoundException {

        String fullPath = path + name;

        return new FileInputStream(fullPath);
    }
}