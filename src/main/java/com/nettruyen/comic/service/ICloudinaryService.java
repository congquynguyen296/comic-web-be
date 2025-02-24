package com.nettruyen.comic.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ICloudinaryService {

    String uploadFileToCloudinary(MultipartFile file) throws IOException;

    String downloadAndStorePicture(String pictureUrl, String accessToken);
}
