package com.nettruyen.comic.util;

import com.nettruyen.comic.dto.request.chapter.ChapterCreationRequest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@Slf4j
public class ScraperData {

    private static String getTitle(Document doc) {
        String title = doc.select(".box h2").text();

        String cleanedTitle = title.replaceAll("^Chương\\s*\\d+\\s*", "").trim();

        return cleanedTitle;
    }

    private static int getChapterNumber(Document doc) {
        Element title = doc.getElementsByClass("chap-title").first();
        String fullText = title.select("span").first().text();
        return Integer.parseInt(fullText.replaceAll("[^0-9]", ""));
    }

    public static ChapterCreationRequest scraperChapter(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Element conentElement = document.getElementById("inner_chap_content_1");

            ChapterCreationRequest chapter = ChapterCreationRequest.builder()
                    .title(getTitle(document))
                    .chapterNumber(getChapterNumber(document))
                    .content(conentElement.html())
                    .build();

            return conentElement != null ? chapter : new ChapterCreationRequest();

        } catch (Exception e) {

            log.error(e.getMessage());
        }
        return new ChapterCreationRequest();
    }
}
