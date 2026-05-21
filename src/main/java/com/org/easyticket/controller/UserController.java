package com.org.easyticket.controller;

import com.org.easyticket.dto.requestdto.UserRequestDto;
import com.org.easyticket.entity.User;
import com.org.easyticket.service.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody UserRequestDto userRequestDto){

        User response = userService.addUserService(userRequestDto);

        if(response !=null){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.ofNullable(null);
        }

    }

    @GetMapping("/findUserById/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable("userId") Long userId){

        User response = userService.findUserByIdService(userId);

        if(response !=null){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.ofNullable(null);
        }

    }

    @PutMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestBody UserRequestDto userRequestDto){

        User response = userService.updateUserService(userRequestDto);

        if(response != null){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.ofNullable(null);
        }

    }

    @GetMapping("/findAllUser")
    public ResponseEntity<List<User>> findAllUser(){

        List<User> userList = userService.findAllUserService();

        if(userList !=null && !userList.isEmpty()){
            return ResponseEntity.ok(userList);
        }else{
            return ResponseEntity.ofNullable(null);
        }

    }

}