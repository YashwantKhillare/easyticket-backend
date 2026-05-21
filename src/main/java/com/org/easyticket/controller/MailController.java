package com.org.easyticket.controller;

import com.org.easyticket.service.common.service.Handler;
import com.org.easyticket.service.mail.service.MailService;
import com.org.easyticket.utils.ResponseHandleUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    @Autowired
    private MailService service;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ResponseHandleUtil responseHandleUtil;

    @PostMapping("/{serviceName}")
    public ResponseEntity<Map<String, Object>> handleMailService(
            @PathVariable("serviceName") String serviceName,
            HttpServletRequest request,
            HttpServletResponse response) {

        log.info("Starting_Mail_service: "+ serviceName);

        //validating service name & get a bean "Service Class Name" ...
        Handler handler = resolveHandler(serviceName);

        //calling the desired service...
        Map<String, Object> result = invokeDoPostCall(handler, request, response);

        log.debug("Mail_handlerDoPostCall_method_result: {}", result);

        return buildResponse(result);
    }

    //validating service name & get a bean "Service Class Name" ...
    private Handler resolveHandler(String serviceName) {
        //validating service name is not null not empty...
        validateServiceName(serviceName);

        try {
            return applicationContext.getBean(serviceName, Handler.class);
        } catch (Exception e) {
            log.error("Failed_to_retrieve_Handler_bean_for_KYC_service: "+ serviceName, e);
            throw new IllegalArgumentException("Handler_bean_not_found_for_KYC_service: " + serviceName, e);
        }
    }

    //validating service name is not null not empty...
    private void validateServiceName(String serviceName) {
        if (serviceName == null || serviceName.trim().isEmpty()) {
            throw new IllegalArgumentException("KYC_service_name_is_required");
        }
    }

    //calling the desired service...
    private Map<String, Object> invokeDoPostCall(Handler handler, HttpServletRequest request, HttpServletResponse response) {
        try {
            return handler.doPostCall(request, response);
        } catch (Exception e) {
            log.error("KYC_handler_doPostCall_execution_failed"+ e);
            throw new RuntimeException("Handler_doPostCall_execution_failed", e);
        }
    }

    private ResponseEntity<Map<String, Object>> buildResponse(Map<String, Object> result) {
        if (result != null && !result.isEmpty()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(responseHandleUtil.buildFailureResponse("NO_DATA_FOUND"));
    }

    @PostMapping("/testMail")
    public String sendMail(){

        service.sendMailService("Yashwant_Khillare","khillareyash95@gmail.com");

        return "SUCCESS";

    }

}