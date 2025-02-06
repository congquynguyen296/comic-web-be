package com.nettruyen.comic.service.Impl;

import com.nettruyen.comic.service.IAccountService;
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
        String redisKey = getRedisKey(email);
        String storedOtp = (String) redisTemplate.opsForValue().get(redisKey);
        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete(redisKey); // Xóa OTP khi hợp lệ
            return true;
        }
        return false;
    }

    @Override
    public void saveOtp(String email, String otp) {
        redisTemplate.opsForValue().set(getRedisKey(email), otp, Duration.ofMinutes(3));
    }

    private String getRedisKey(String email) {
        return "OTP_" + email;
    }
}
