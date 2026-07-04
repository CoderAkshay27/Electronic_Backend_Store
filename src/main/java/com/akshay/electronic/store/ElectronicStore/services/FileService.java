package com.akshay.electronic.store.ElectronicStore.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {


    public String uploadFileService(MultipartFile file,String path);

    InputStream getResource(String path,String name) throws FileNotFoundException;

    String uploadFile(MultipartFile file, String imageUploadPath) throws IOException;
}
