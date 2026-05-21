package com.org.easyticket.utils;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class ResponseHandleUtil {

    public Map<String, Object> buildSuccessResponse(Object data) {
        Map<String, Object> response = new HashMap<>();

        response.put("status", "SUCCESS");
        response.put("statusCode", 200);
        response.put("data", data);

        return response;
    }
    public Map<String, Object> buildFailureResponse(String message) {
        Map<String, Object> response = new HashMap<>();

        response.put("status", "FAILURE");
        response.put("statusCode", 500);
        response.put("message", message);

        return response;
    }

}