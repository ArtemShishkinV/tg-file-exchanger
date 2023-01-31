package com.shishkin.model;

public record EmailMessage(String to,
                           String subject,
                           String text) {
}
