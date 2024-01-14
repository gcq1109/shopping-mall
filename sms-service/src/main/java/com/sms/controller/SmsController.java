package com.sms.controller;

import com.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcq1109
 * @email gcq1109@126.com
 */
@RestController
@RequestMapping("/sms")
public class SmsController {


    @Autowired
    private SmsService smsService;

    @RequestMapping("/send-msg-code")
    public void senSms(@RequestParam String phoneNumber) {
        smsService.sendSms(phoneNumber);
    }
}
