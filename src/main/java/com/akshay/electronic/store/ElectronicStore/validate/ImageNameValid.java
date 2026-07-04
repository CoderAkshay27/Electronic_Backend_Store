package com.akshay.electronic.store.ElectronicStore.validate;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.Valid;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {
    //error
    String message() default "Invalid Image Name !!!";
    //represent group of constraints
    Class<?>[] groups() default {};
    //additional information about the annotation
    Class<? extends Payload>[] payload() default {};
}
