package com.akshay.electronic.store.ElectronicStore.services;

import com.akshay.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.akshay.electronic.store.ElectronicStore.dtos.UserDTO;

import java.io.IOException;
import java.util.List;

public interface UserService {

    List<UserDTO> searchUser(String keywords);

    // create
    UserDTO create(UserDTO userDto);

    // update
    UserDTO updateUser(UserDTO userDto, String userID);

    //delete
    void deleteUser(String userID) throws IOException;

    // get all user
    PageableResponse<UserDTO> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    // get single user by ID
    UserDTO getUserByID(String userID);

    // get single user by email
    UserDTO getUserByEmail(String email);

}
