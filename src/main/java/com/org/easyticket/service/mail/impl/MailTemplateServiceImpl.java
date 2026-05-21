package com.org.easyticket.service.mail.impl;

import com.google.gson.JsonObject;
import com.org.easyticket.exception.ApiException;
import com.org.easyticket.service.common.service.Handler;
import com.org.easyticket.utils.JsonUtil;
import com.org.easyticket.utils.ResponseHandleUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import static com.org.easyticket.constant.CommonConstant.*;
import static com.org.easyticket.utils.JsonUtil.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service("mailTemplateService")
public class MailTemplateServiceImpl implements Handler {

    private final JsonUtil jsonUtil;
    private final ResponseHandleUtil responseHandleUtil;

    public JsonObject processMailTemplate(JsonObject mailJsonObjRequest){

        JsonObject jsonResponse = new JsonObject();

        String template="";
        String key = getAsString(mailJsonObjRequest,"key");

        if(key != null && !key.isEmpty()){
            
            if(key.equalsIgnoreCase("BUSSINESS")){
                template =
                        """
                        <html>
                        <body style="font-family: Arial, sans-serif;">
                        
                            <div style="padding:20px;border:1px solid #cccccc;border-radius:10px;">
                                <h2 style="color:#2E86C1;">Business Proposal</h2>
                                
                                <p>Dear Customer,</p>
                                
                                <p>
                                    Thank you for showing interest in our business services.
                                    We are excited to collaborate with you and grow together.
                                </p>
                                
                                <p>
                                    Please connect with our team for further discussion.
                                </p>
                                
                                <br/>
                                
                                <p>
                                    Regards,<br/>
                                    Business Team
                                </p>
                            </div>
                        
                        </body>
                        </html>
                        """;
            } else if (key.equalsIgnoreCase("BIRTHDAY")) {
                template =
                        """
                        <html>
                        <body style="font-family: Arial, sans-serif;">
                        
                            <div style="padding:20px;background:#FFF3CD;border-radius:10px;">
                                
                                <h1 style="color:#E67E22;">
                                    🎂 Happy Birthday!
                                </h1>
                                
                                <p>
                                    Wishing you a day filled with happiness,
                                    success, and lots of joy.
                                </p>
                                
                                <p>
                                    May all your dreams come true!
                                </p>
                                
                                <br/>
                                
                                <p>
                                    Best Wishes,<br/>
                                    Your Company Team
                                </p>
                                
                            </div>
                        
                        </body>
                        </html>
                        """;
            } else if (key.equalsIgnoreCase("SALARY")) {
                template =
                        """
                        <html>
                        <body style="font-family: Arial, sans-serif;">
                        
                            <div style="padding:20px;border:1px solid #28B463;border-radius:10px;">
                                
                                <h2 style="color:#28B463;">
                                    Salary Credited
                                </h2>
                                
                                <p>Dear Employee,</p>
                                
                                <p>
                                    Your salary has been successfully credited
                                    to your bank account.
                                </p>
                                
                                <p>
                                    Kindly check your account statement for details.
                                </p>
                                
                                <br/>
                                
                                <p>
                                    Regards,<br/>
                                    HR Department
                                </p>
                                
                            </div>
                        
                        </body>
                        </html>
                        """;
            } else {
                template =
                        """
                        <html>
                        <body style="font-family: Arial, sans-serif; background-color:#f4f4f4; padding:20px;">
            
                            <div style="max-width:600px; margin:auto; background:white; padding:30px; border-radius:10px; border:1px solid #dddddd;">
            
                                <h2 style="color:#2E86C1; text-align:center;">
                                    Registration Successful
                                </h2>
            
                                <p>Dear User,</p>
            
                                <p>
                                    We are happy to inform you that your account has been
                                    successfully registered with us.
                                </p>
            
                                <p>
                                    You can now login and access all available services and features.
                                </p>
            
                                <div style="margin-top:20px; margin-bottom:20px; text-align:center;">
                                    <a href="https://yourwebsite.com/login"
                                       style="background:#2E86C1;
                                              color:white;
                                              padding:12px 20px;
                                              text-decoration:none;
                                              border-radius:5px;">
                                        Login Now
                                    </a>
                                </div>
            
                                <p>
                                    If you did not create this account, please contact our support team immediately.
                                </p>
            
                                <br/>
            
                                <p>
                                    Regards,<br/>
                                    Support Team
                                </p>
            
                            </div>
            
                        </body>
                        </html>
                        """;
            }

            jsonResponse.addProperty(STATUS,SUCCESS);
            jsonResponse.addProperty(MESSAGE,"DATA_FOUND");
            jsonResponse.addProperty("TEMPLATE",template);
        }else{
            jsonResponse.addProperty(STATUS,FAILURE);
            jsonResponse.addProperty(MESSAGE,"KEY_NOT_FOUND!");
        }
        return jsonResponse;
    }


    @Override
    public Map<String, Object> doPostCall(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            JsonObject mailJsonObjRequest = jsonUtil.getDecryptedJsonObject(request, "mailInfo");
            JsonObject mailJsonObjResponse = processMailTemplate(mailJsonObjRequest);
            Map<String, Object> responseMap = jsonUtil.getGson().fromJson(mailJsonObjResponse.toString(), Map.class);
            return responseHandleUtil.buildSuccessResponse(responseMap);
        } catch (Exception e) {
            log.error("Exception in doPostCall: {}", e.getMessage(), e);
            throw new ApiException(e.getMessage(), FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}