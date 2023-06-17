package com.gsa.tech.bazaar.services;

import com.gsa.tech.bazaar.dtos.PageableResponse;
import com.gsa.tech.bazaar.dtos.UserDto;

import java.util.List;

public interface UserService {
    //Create user
    UserDto createUser (UserDto userDto);

    //Update
    UserDto updateUser (UserDto userDto,String userId);

    //Delete
    void DeleteUser (String userId);

    //Get all users
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    //Get single user by id
    UserDto getUserById(String userId);

    //get single user by email
    UserDto getUserByEmail(String email);
    //search user
    List<UserDto> searchUser(String keyword);

    //Other user Specific Features

}
