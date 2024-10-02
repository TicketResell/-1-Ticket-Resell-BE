package com.teamseven.ticketresell.service;

public interface ISmsService {
    public void sendSms(String toPhoneNumber, String message);
}
