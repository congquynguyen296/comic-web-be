package com.nettruyen.comic.service;

public interface IAccountService {

    void sendEmail(String toEmail, String subject, String body);

    boolean validateOtp(String email, String otp);

    void saveOtp(String email, String otp);

    void sendEmailAsync(String toEmail, String username, String otpCode);

}
