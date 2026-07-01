package com.org.easyticket.controller;

import com.org.easyticket.dto.requestdto.UserRequestDto;
import com.org.easyticket.entity.User;
import com.org.easyticket.service.user.service.UserService;
import com.org.easyticket.utils.ResponseHandleUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import static com.org.easyticket.utils.ResponseHandleUtil.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody UserRequestDto userRequestDto){

        User response = userService.addUserService(userRequestDto);

        if(response !=null){
            return ResponseEntity.ok(buildSuccessResponse(response));
        }else{
            return ResponseEntity.ofNullable(buildFailureResponse("FAILED_TO_REGISTER_USER"));
        }

    }

    @GetMapping("/findUserById/{userId}")
    public ResponseEntity<Map<String, Object>> findUserById(@PathVariable("userId") Long userId){

        User response = userService.findUserByIdService(userId);

        if(response !=null){
            return ResponseEntity.ok(buildSuccessResponse(response));
        }else{
            return ResponseEntity.ofNullable(buildFailureResponse("USER_NOT_FOUND"));
        }

    }

    @PutMapping("/updateUser")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestBody UserRequestDto userRequestDto){

        User response = userService.updateUserService(userRequestDto);

        if(response != null){
            return ResponseEntity.ok(buildSuccessResponse(response));
        }else{
            return ResponseEntity.ofNullable(buildFailureResponse("USER_UPDATE_FAILED"));
        }

    }

    @GetMapping("/findAllUser")
    public ResponseEntity<Map<String, Object>> findAllUser(){

        List<User> userList = userService.findAllUserService();

        if(userList !=null && !userList.isEmpty()){
            return ResponseEntity.ok(buildSuccessResponse(userList));
        }else{
            return ResponseEntity.ofNullable(buildFailureResponse("NO_DATA_FOUND"));
        }

    }

}