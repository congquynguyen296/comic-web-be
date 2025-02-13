package com.nettruyen.comic.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.nettruyen.comic.service.ICloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/cloudinary")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CloudinaryController {

    ICloudinaryService cloudinaryService;

    @PostMapping("/upload/image")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return cloudinaryService.uploadFileToCloudinary(file);
    }
}
