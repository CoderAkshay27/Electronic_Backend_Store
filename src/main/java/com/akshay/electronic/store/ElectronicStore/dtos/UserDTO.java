package com.akshay.electronic.store.ElectronicStore.dtos;

import com.akshay.electronic.store.ElectronicStore.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private String userId;


    @Size(min = 3,max = 15,message = "Invalid Name !!!")
    private String name;
    @Email
    @NotBlank(message = "Email is Required.")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid Message !!!")
    private String email;
    @NotBlank(message = "Invalid Password !!!")
    private String password;
    @Size(min = 4,message = "Invalid Gender !!!")
    private String gender;
    @NotBlank(message = "Write something about yourself !!!")
    private String about;

    // pattern
    // custom validator
    @ImageNameValid
    private String imageName;

}
