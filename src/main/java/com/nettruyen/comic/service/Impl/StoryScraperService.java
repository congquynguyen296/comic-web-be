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
            Document document = Jsoup.connect(url).get();

            Element conentElement = document.getElementById("inner_chap_content_1");

            return conentElement != null ? conentElement.html() : "No content";

        } catch (Exception e) {

            log.error(e.getMessage());
        }
        return "No content";
    }
}
