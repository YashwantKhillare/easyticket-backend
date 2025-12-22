package com.org.easyticket.service.impl;

import com.org.easyticket.dto.requestdto.UserRequestDto;
import com.org.easyticket.entity.User;
import com.org.easyticket.repository.UserRepository;
import com.org.easyticket.service.UserService;
import com.org.easyticket.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUserService(UserRequestDto userRequestDto) {
        User user = new User();
        try{

            user.setFirstName(userRequestDto.getFirstName());
            user.setMiddleName(userRequestDto.getMiddleName());
            user.setLastName(userRequestDto.getLastName());
            user.setEmailId(userRequestDto.getEmailId());
            user.setPassword(userRequestDto.getPassword());
            user.setRole(ValidationUtil.extractRoleFromParam(userRequestDto.getRole()));

            return userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User findUserByIdService(Long userId) {
        try{
            return userRepository.findById(userId).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User updateUserService(UserRequestDto userRequestDto) {
        User user = new User();
        try{

            user.setUserId(userRequestDto.getUserId());
            user.setFirstName(userRequestDto.getFirstName());
            user.setMiddleName(userRequestDto.getMiddleName());
            user.setLastName(userRequestDto.getLastName());
            user.setEmailId(userRequestDto.getEmailId());
            user.setPassword(userRequestDto.getPassword());
            user.setRole(ValidationUtil.extractRoleFromParam(userRequestDto.getRole()));

            return userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> findAllUserService() {
        return userRepository.findAll();
    }
}
