package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.service.IAccountService;
import com.nettruyen.comic.service.IRedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements IAccountService {

    JavaMailSender mailSender;
    RedisTemplate<String, Object> redisTemplate;

    IRedisService redisService;

    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        log.info("Email send.");
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
}
