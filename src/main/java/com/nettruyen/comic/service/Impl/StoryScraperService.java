package com.nettruyen.comic.service.Impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StoryScraperService {

    public String scraperChapterContent(String url) {
        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .get();

            Element contentElement = document.getElementById("chapter-c");

            if (contentElement == null) {
                log.warn("Content element not found for URL: {}", url);
                return "No content found";
            }

            contentElement.select("div.ads-responsive, div.ads-mobile, div.ads-desktop").remove();
            return contentElement.html();

        } catch (Exception e) {
            log.error("Error scraping chapter content from URL: {}. Error: {}", url, e.getMessage());
            return "No content found due to error";
        }
    }
}