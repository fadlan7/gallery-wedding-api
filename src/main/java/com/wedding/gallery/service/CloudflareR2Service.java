package com.wedding.gallery.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudflareR2Service {
    String uploadFile(MultipartFile file, String folder);
}
