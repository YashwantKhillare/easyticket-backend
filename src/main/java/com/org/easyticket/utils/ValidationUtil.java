package com.org.easyticket.utils;

import static com.org.easyticket.constant.ConstantRole.*;

public class ValidationUtil {

    //because utility classes should not have public constructor.
    private ValidationUtil(){

    }

    public static String extractRoleFromParam(String roleParam){
        String role;
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
