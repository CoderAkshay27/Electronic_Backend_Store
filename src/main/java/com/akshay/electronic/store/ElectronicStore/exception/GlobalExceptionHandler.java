package com.akshay.electronic.store.ElectronicStore.exception;

import com.akshay.electronic.store.ElectronicStore.dtos.ApiResponseMessage;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger= (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException resourceNotFoundException) {
        logger.info("Exception Handler Invoked !!! ");
        ApiResponseMessage message= ApiResponseMessage.builder().message(resourceNotFoundException.getMessage()).success(true).status(HttpStatus.NOT_FOUND).build();

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
    // MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>>handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
        List<ObjectError> allerrors=ex.getBindingResult().getAllErrors();
        Map<String,Object> respone=new HashMap<>();
        allerrors.stream().forEach(ObjectError->{
            String message=ObjectError.getDefaultMessage();
            String field=((FieldError)ObjectError).getField();
            respone.put(field,message);
        });

        return new ResponseEntity<>(respone,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<BadApiRequest> resourceNotFoundExceptionHandler(BadApiRequest badApiRequest) {
        logger.info("Bad Api Request ");
        ApiResponseMessage message= ApiResponseMessage.builder().message(badApiRequest.getMessage()).success(false).status(HttpStatus.BAD_REQUEST).build();

        return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
    }
}
