package com.event.booking.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("timestamp", new Date());
        map.put("message", message);
        map.put("status", status);
        map.put("statusCode", status.value());
        map.put("data", responseObj);

        return new ResponseEntity<Object>(map,status);
    }

}
