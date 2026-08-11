package com.wedding.gallery.service;

import com.wedding.gallery.dto.request.SearchGalleryRequest;
import com.wedding.gallery.dto.response.GalleryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface GalleryService {
    GalleryResponseDTO create(String guestId, MultipartFile image, MultipartFile voiceNote);
    Page<GalleryResponseDTO> getAllGallery(SearchGalleryRequest request, String type);
    GalleryResponseDTO updateApprovedStatus(Long guestId);
}
