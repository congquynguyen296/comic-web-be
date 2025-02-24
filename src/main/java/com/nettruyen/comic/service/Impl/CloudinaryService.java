package com.nettruyen.comic.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nettruyen.comic.service.ICloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService implements ICloudinaryService {

    Cloudinary cloudinary;

    @Override
    public String uploadFileToCloudinary(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        String publicValue = generatePublicValue(file.getOriginalFilename());

        String extension = getFileName(file.getOriginalFilename())[1];
        File fileUpload = convert(file);

        cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));
        cleanDisk(fileUpload);

        return  cloudinary.url().generate(StringUtils.join(publicValue, ".", extension));
    }

    @Override
    public String downloadAndStorePicture(String pictureUrl, String accessToken) {
        try {
            URL url = new URL(pictureUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            try (InputStream in = connection.getInputStream()) {
                // Lưu ảnh vào thư mục cục bộ hoặc cloud (ví dụ: Cloudinary)
                String fileName = UUID.randomUUID() + ".jpg";
                Files.copy(in, Paths.get("src/main/resources/static/images/" + fileName));
                return "/images/" + fileName; // Trả về URL nội bộ
            }
        } catch (Exception e) {
            log.error("Error downloading picture: {}", e.getMessage());
            return "https://res.cloudinary.com/dyimxnbb8/image/upload/v1739182784/c1f3dec5-06db-4334-87ca-bd965612530f_avatar07.jpg"; // Fallback
        }
    }

    // Các hàm bổ trợ
    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        File convFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()), getFileName(file.getOriginalFilename())[1]));
        try(InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }

    private void cleanDisk(File file) {
        try {
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String generatePublicValue(String originalName){
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "_", fileName);
    }

    public String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }


}
