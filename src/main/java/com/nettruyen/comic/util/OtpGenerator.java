package com.nettruyen.comic.util;

import java.util.Random;

public class OtpGenerator {
    private static final Random RANDOM = new Random();

    public static String generateOtp() {
        return String.format("%06d", RANDOM.nextInt(999999));
    }
}
