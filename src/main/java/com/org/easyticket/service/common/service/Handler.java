package com.org.easyticket.service.common.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface Handler {
    Map<String, Object> doPostCall(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception;
}