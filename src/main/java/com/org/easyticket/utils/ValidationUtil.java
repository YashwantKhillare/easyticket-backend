package com.org.easyticket.utils;

import com.org.easyticket.constant.ConstantRole;
import static com.org.easyticket.constant.ConstantRole.*;
public class ValidationUtil {

    public static String extractRoleFromParam(String roleParam){
        String role=null;
        if(roleParam.equals("A")){
            role = ADMIN;
        } else if (roleParam.equals("U")) {
            role = USER;
        } else {
            role = NOT_APPLICABLE;
        }
        return role;
    }

}
