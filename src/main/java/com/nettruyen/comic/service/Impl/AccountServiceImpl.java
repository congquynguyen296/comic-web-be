package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.service.IAccountService;
import com.nettruyen.comic.service.IRedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements IAccountService {

    JavaMailSender mailSender;

    IRedisService redisService;

    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        String redisKey = getRedisKeyForConfirmEmail(email);
        String storedOtp = redisService.getObject(redisKey, String.class);

        if (storedOtp != null && storedOtp.equals(otp)) {
            redisService.deleteObject(redisKey);
            return true;
        }
        return false;
    }

    @Override
    public void saveOtp(String email, String otp) {
        redisService.setObject(getRedisKeyForConfirmEmail(email), otp, 300);
    }

    private String getRedisKeyForConfirmEmail(String email) {
        return "auth:email:otp:" + email;
    }

    // ===== Async Email =====

    @Override
    @Async("taskExecutor")
    public void sendEmailAsync(String toEmail, String username, String otpCode) {
        try {
            String subject = "🔑 Activate Your Account at Q.comic!";
            String body = "Hello " + username + ",\n\n"
                    + "Thank you for signing up at Q.comic. To activate your account, please use the following OTP code:\n\n"
                    + "🔒 Your OTP Code: " + otpCode + "\n\n"
                    + "This code is valid for the next 10 minutes. Please do not share this code with anyone.\n\n"
                    + "If you did not request this, please ignore this email.\n\n"
                    + "Best regards,\n"
                    + "The Q.comic Team";

            // Thực chất email sẽ được gửi ở đây
            sendEmail(toEmail, subject, body);
            log.info("Email sent to {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }
}
