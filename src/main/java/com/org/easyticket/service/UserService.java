package com.org.easyticket.service;

import com.org.easyticket.dto.requestdto.UserRequestDto;
import com.org.easyticket.entity.User;

import java.util.List;

public interface UserService {

    User addUserService(UserRequestDto userRequestDto);

    User findUserByIdService(Long userId);

    User updateUserService(UserRequestDto userRequestDto);

    List<User> findAllUserService();
}
