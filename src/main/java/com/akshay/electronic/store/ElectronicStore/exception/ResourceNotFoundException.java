package com.akshay.electronic.store.ElectronicStore.exception;


import lombok.Builder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
@Builder
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Resource Not Found !!! ");

    }

    public ResourceNotFoundException(String message) {
        super(message);

    }
}
