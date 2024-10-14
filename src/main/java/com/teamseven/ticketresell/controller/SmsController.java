package com.teamseven.ticketresell.controller;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {

    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "AC5247bc5b557b7b801b79b0f08fb4aa63";
    public static final String AUTH_TOKEN = "2cfb263b2b3fce35a5ab589bad3e8257";
    public static final String SERVICE_SID = "VAa74290c87331b1011320526964201c98"; // Your Verify Service SID

    // Constructor
    public SmsController() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @PostMapping("/send-test-sms-test")
    public ResponseEntity<?> sendSmsTest(@RequestParam String toPhoneNumber) {
        System.out.println("Start test sending SMS to: " + toPhoneNumber);

        try {
            // Gửi yêu cầu xác minh OTP
            Verification verification = Verification.creator(
                    SERVICE_SID,
                    toPhoneNumber,
                    "sms"
            ).create();

            System.out.println("Verification SID: " + verification.getSid());
            return ResponseEntity.ok("OTP sent successfully to " + toPhoneNumber);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send SMS: " + e.getMessage());
        }
    }
}
