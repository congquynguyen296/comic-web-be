package com.nettruyen.comic.util;

import java.text.Normalizer;

public class ConvertorUtil {


    public static String convertNameToCode(String name) {

        String withoutAccents = Normalizer.normalize(name, Normalizer.Form.NFD);
        withoutAccents = withoutAccents.replaceAll("\\p{InCombiningDiacriticalMarks}", "");

        return name.isEmpty()
                ? "unknown"
                : withoutAccents.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}
