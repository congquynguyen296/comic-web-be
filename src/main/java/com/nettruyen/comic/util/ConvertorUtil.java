package com.nettruyen.comic.util;

import com.nettruyen.comic.dto.response.chapter.ChapterComponentResponse;
import com.nettruyen.comic.entity.ChapterEntity;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConvertorUtil {


    public static String convertNameToCode(String name) {

        String withoutAccents = Normalizer.normalize(name, Normalizer.Form.NFD);
        withoutAccents = withoutAccents.replaceAll("\\p{InCombiningDiacriticalMarks}", "");

        return name.isEmpty()
                ? "unknown"
                : withoutAccents.trim().toLowerCase().replaceAll("\\s+", "-");
    }

    public static ChapterComponentResponse convertToChapterComponentResponse(ChapterEntity chapterEntity) {
        return ChapterComponentResponse.builder()
                .chapterNumber(chapterEntity.getChapterNumber())
                .date(convertDate(chapterEntity.getCreatedAt().toString()))
                .title(chapterEntity.getTitle())
                .storyId(chapterEntity.getId())
                .build();
    }

    public static String convertDate(String isoDate) {
        String dateTimeStr = isoDate.split("\\.")[0]; // Cắt bỏ phần thập phân
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeStr, formatter) // Parse thành LocalDateTime
                .toLocalDate() // Lấy phần ngày
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); // Định dạng lại
    }

}
