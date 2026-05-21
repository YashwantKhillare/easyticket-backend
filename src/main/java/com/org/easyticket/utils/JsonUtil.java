package com.org.easyticket.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class JsonUtil {

    public static final String IV = "algf";
    public static final String SCR_P_PHRASE = "sps";
    public static final String SALT = "sl";
    public static final String PATTERN = "[\">+</;:{}^`!=]";

    public Gson getGson(){
        return new Gson();
    }

    public String getNestedJsonValueTwoLevel(JsonObject parent, String nestedObjectKey, String targetKey) {
        if (parent == null || !parent.has(nestedObjectKey)) return null;

        JsonObject nested = parent.getAsJsonObject(nestedObjectKey);
        if (nested != null && nested.has(targetKey)) {
            return nested.get(targetKey).getAsString();
        }

        return null;
    }

    public JsonObject stringConvertJson(String data){
        return getGson().fromJson(data, JsonObject.class);
    }


    public <T> T parseEncrypted(String request, Class<T> responseType) throws Exception {

        // Parse the JSON string to JsonObject
        JsonObject encryptedParams = JsonParser.parseString(request).getAsJsonObject();
        // Access fields
        String password = encryptedParams.get("sps").getAsString();
        String salt = encryptedParams.get("sl").getAsString();
        String iv = encryptedParams.get("algf").getAsString();
        String encryptedMainParams = encryptedParams.get("params").getAsString();

        String requestString = EncryptDecryptUtil.decrypt(encryptedMainParams, salt, iv, password);
        log.debug("Request convert in String format {}" , requestString);
        return stringConvertObject(requestString, responseType);
    }

    public <T> T stringConvertObject(String data, Class<T> responseType){
        return getGson().fromJson(data, responseType);
    }

    public <T> T jsonObjectConvertPojo(JsonObject data, Class<T> responseType){
        return getGson().fromJson(data, responseType);
    }

    public  Map<String, Object> objectConvertMap(JsonObject object) {
        Map<String, Object> map = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> entries = object.entrySet();
        for (Map.Entry<String, com.google.gson.JsonElement> entry : entries) {
            map.put(entry.getKey(), entry.getValue().getAsString());
        }
        return map;
    }

    public Map<String, Object> objectConvertMap2(JsonObject object) {
        Map<String, Object> map = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            JsonElement value = entry.getValue();

            if (value == null || value.isJsonNull()) {
                map.put(entry.getKey(), null);
            }
            else if (value.isJsonPrimitive()) {
                map.put(entry.getKey(), value.getAsString());
            }
            else {
                // JsonArray or JsonObject → convert to JSON string
                map.put(entry.getKey(), value.toString());
            }
        }
        return map;
    }

    public Map<String, Object> objectConvertMapForDndComplaint(JsonObject object) {
        Map<String, Object> map = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> entries = object.entrySet();

        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (value == null || value.isJsonNull()) {
                map.put(key, null);
            } else if (value.isJsonPrimitive()) {
                // String, number, or boolean
                map.put(key, value.getAsString());
            } else if (value.isJsonArray() || value.isJsonObject()) {
                // Preserve arrays/objects as their JSON string representation
                map.put(key, value.toString());
            } else {
                // Fallback for unexpected types
                map.put(key, value.toString());
            }
        }
        return map;
    }

    public  JsonObject toJsonObject(Object obj) {
        JsonElement element = getGson().toJsonTree(obj);
        return element.getAsJsonObject();
    }


    //******************************* enterprise_encrypted_json_class *****************************************

    public static JSONObject getDecryptedJson(HttpServletRequest request, String parameterName) throws Exception {
        // For @RestController, we need to read from request body
        StringBuilder requestBody = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        if (StringUtils.hasText(requestBody.toString())) {
            JSONObject requestJson = new JSONObject(requestBody.toString());
            String inputData = requestJson.optString(parameterName, "");

            if (StringUtils.hasText(inputData)) {
                JSONObject encryptedparams = new JSONObject(inputData);

                // Use your constants with correct mapping:
                // algf = IV (Initialization Vector)
                // sps = SCR_P_PHRASE (Password/Secret phrase)
                // sl = SALT (Salt)
                String iv = encryptedparams.getString(IV);                // algf
                String password = encryptedparams.getString(SCR_P_PHRASE);  // sps
                String salt = encryptedparams.getString(SALT);             // sl
                String encryptedMainparams = encryptedparams.getString("params");           // params

                return new JSONObject(EncryptDecryptUtil.decrypt(encryptedMainparams, salt, iv, password));
            }
        }

        return new JSONObject();
    }


    public static JsonObject getDecryptedJsonObject(HttpServletRequest request, String parameterName) throws Exception {

        // Read request body
        StringBuilder requestBody = new StringBuilder();

        BufferedReader reader = request.getReader();
        String line;

        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        // Check request body
        if (StringUtils.hasText(requestBody.toString())) {

            JSONObject requestJson = new JSONObject(requestBody.toString());

            String inputData = requestJson.optString(parameterName, "");

            if (StringUtils.hasText(inputData)) {

                JSONObject encryptedparams = new JSONObject(inputData);

                // Extract encrypted values
                String iv = encryptedparams.getString(IV);
                String password = encryptedparams.getString(SCR_P_PHRASE);
                String salt = encryptedparams.getString(SALT);
                String encryptedMainparams = encryptedparams.getString("params");

                // Decrypt string
                String decryptedJsonString =
                        EncryptDecryptUtil.decrypt(encryptedMainparams, salt, iv, password);

                // Convert String -> Gson JsonObject
                return JsonParser.parseString(decryptedJsonString).getAsJsonObject();
            }
        }

        return new JsonObject();
    }

    public static  String getAsString(JsonObject json, String key) {
        return json.has(key) && !json.get(key).isJsonNull()
                ? json.get(key).getAsString().replaceAll(PATTERN, " ")
                : null;
    }

    public static  String getAsString(JsonObject json, String key, String defaultVal) {
        return json.has(key) && !json.get(key).isJsonNull()
                ? json.get(key).getAsString().replaceAll(PATTERN, " ")
                : defaultVal;
    }

    public static JSONObject gsonToJsonObject(JsonObject gsonObject) {
        return gsonObject == null ? new JSONObject() : new JSONObject(gsonObject.toString());
    }

    public static Map<String, Object> jsonObjectToMap(JsonObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            if (!entry.getValue().isJsonNull()) {
                map.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        return map;
    }


    public static Map<String, Object> convertJSONObjectToMap(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();

        if (jsonObject == null) {
            return map;
        }

        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
                value = convertJSONObjectToMap((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = convertJSONArrayToList((JSONArray) value);
            }

            map.put(key, value);
        }

        return map;
    }

    private static Object convertJSONArrayToList(JSONArray array) {
        Object[] list = new Object[array.length()];

        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);

            if (value instanceof JSONObject) {
                value = convertJSONObjectToMap((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = convertJSONArrayToList((JSONArray) value);
            }

            list[i] = value;
        }
        return list;
    }


    public static Map<String, String> encrypt(String plainText) throws Exception {
        Map<String, String> encryptedMap = new HashMap<>();

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        byte[] ivBytes = new byte[16];
        byte[] passwordBytes = new byte[16];
        random.nextBytes(salt);
        random.nextBytes(ivBytes);
        random.nextBytes(passwordBytes);
        String password = Base64.getEncoder().encodeToString(passwordBytes);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));

        String encryptedData = Base64.getEncoder().encodeToString(encryptedBytes);
        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String ivBase64 = Base64.getEncoder().encodeToString(ivBytes);

        encryptedMap.put("encryptedData", encryptedData);
        encryptedMap.put("salt", saltBase64);
        encryptedMap.put("iv", ivBase64);
        encryptedMap.put("password", password);

        return encryptedMap;
    }

    public static String getUuid() {
        String uniqueID = UUID.randomUUID().toString();
        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        String dateTime = simpleDateFormat.format(today);
        uniqueID = uniqueID + "||" + dateTime;
//        log.debug("UniqueID: " + uniqueID);
        return uniqueID;
    }

    public static boolean onlyDigits(String str) {
        //String regex = "[0-9]+";
        String regex ="\\d{10}";
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String getEncryptedVal(String token) {
        try {
            log.debug("Cookie getEncryptedVal Token:::{}" , token);
            token = CRSAESEncryptor.encrypt(token);
            log.debug("Cookie Token after Encryption:::{}" , token);
        } catch (Exception e) {
            log.error("Exception Occured ", e);

        }
        return token;
    }

}