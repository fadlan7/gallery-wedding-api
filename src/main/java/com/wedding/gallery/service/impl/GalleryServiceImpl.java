package com.wedding.gallery.service.impl;

import com.wedding.gallery.dto.response.GalleryResponseDTO;
import com.wedding.gallery.entity.GalleryItem;
import com.wedding.gallery.repository.GalleryItemRepository;
import com.wedding.gallery.service.CloudflareR2Service;
import com.wedding.gallery.service.GalleryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class GalleryServiceImpl implements GalleryService {

    private final CloudflareR2Service cloudflareR2Service;
    private final GalleryItemRepository galleryRepository;

    @Override
    public GalleryResponseDTO create( String guestId, MultipartFile image, MultipartFile voiceNote) {
        long jumlahUpload = galleryRepository.countByGuestUuid(guestId);
        if (jumlahUpload >= 10) {
            throw new IllegalArgumentException("Maaf, kuota upload kamu sudah penuh (Maksimal 10 kali upload).");
        }

        String imagePath = cloudflareR2Service.uploadFile(image, "images");
        String audioPath = cloudflareR2Service.uploadFile(voiceNote, "audios");

        GalleryItem item = GalleryItem.builder()
                .guestUuid(guestId)
                .imagePath(imagePath)
                .isApproved(true)
                .audioPath(audioPath)
                .build();

        galleryRepository.save(item);

        return GalleryResponseDTO.builder()
                .guestUuid(item.getGuestUuid())
                .imagePath(item.getImagePath())
                .audioPath(item.getAudioPath())
                .isApproved(item.getIsApproved())
                .createdAt(item.getCreatedAt())
                .build();
    }
}
