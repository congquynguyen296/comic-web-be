package com.nettruyen.comic.util;

import com.nettruyen.comic.dto.request.chapter.ChapterCreationRequest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@Slf4j
public class ChapterScraper {

    public static String getTitle(Document doc) {
        Element titleElement = doc.select("a.chapter-title").first();
        String title = titleElement != null ? titleElement.text() : "";

        String cleanedTitle = title.replaceAll("^Chương\\s*\\d+\\s*:\\s*", "").trim();

        return cleanedTitle.isEmpty() ? "Unknown Title" : cleanedTitle;
    }

    public static int getChapterNumber(Document doc) {
        Element chapterNumInput = doc.select("input#chapter-num").first();
        if (chapterNumInput != null) {
            try {
                return Integer.parseInt(chapterNumInput.attr("value"));
            } catch (NumberFormatException e) {
                log.warn("Cannot parse chapter number from input: {}", chapterNumInput.attr("value"));
            }
        }

        return -1;
    }

    public static ChapterCreationRequest scraperChapter(String url) {
        try {
            Document document = Jsoup.connect(url).get();

            Element contentElement = document.select("div#chapter-c").first();

            if (contentElement == null) {
                log.warn("Content element not found for URL: {}", url);
                return new ChapterCreationRequest();
            }

            contentElement.select("div.ads-responsive, div.ads-mobile, div.ads-desktop").remove();

            // Tạo đối tượng ChapterCreationRequest
            ChapterCreationRequest chapter = ChapterCreationRequest.builder()
                    .title(getTitle(document))
                    .chapterNumber(getChapterNumber(document))
                    .content(contentElement.html())
                    .build();

            return chapter;

        } catch (Exception e) {
            log.error("Error scraping chapter from URL: {}. Error: {}", url, e.getMessage());
            return new ChapterCreationRequest();
        }
    }
}
