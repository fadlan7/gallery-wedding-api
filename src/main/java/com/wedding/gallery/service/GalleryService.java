package com.wedding.gallery.service;

import com.wedding.gallery.dto.response.GalleryResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface GalleryService {
    GalleryResponseDTO create(String guestId, MultipartFile image, MultipartFile voiceNote);
}
