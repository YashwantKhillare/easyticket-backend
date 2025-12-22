package com.org.easyticket.dto.requestdto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class UserRequestDto {

    private Long userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String emailId;
    private String password;
    private String role;

}
