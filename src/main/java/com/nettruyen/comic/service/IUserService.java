package com.nettruyen.comic.service;

import com.nettruyen.comic.dto.request.authentication.UserCreationRequest;
import com.nettruyen.comic.dto.response.user.UserResponse;

import java.util.List;

public interface IUserService {

    UserResponse createUser(UserCreationRequest request);

    UserResponse updateUser(Object request);

    void deleteUser(String username);

    UserResponse findUserByUsername(String username);

    List<UserResponse> findAllUsers();

    UserResponse findUserById(String id);

    UserResponse getMyInfo();
}
