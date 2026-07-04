package com.akshay.electronic.store.ElectronicStore.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageApiRespone {
    private String filename;
    private String message;
    private HttpStatus status;
    private Boolean success;

}
