package com.shishkin.controller;

import com.shishkin.model.EmailMessage;
import com.shishkin.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody EmailMessage emailMessage) {
        emailService.send(emailMessage);
        return ResponseEntity.ok().build();
    }
}
