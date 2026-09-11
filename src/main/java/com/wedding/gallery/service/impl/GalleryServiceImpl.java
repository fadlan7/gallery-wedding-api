package com.wedding.gallery.service.impl;

import com.wedding.gallery.config.CloudflareR2Access;
import com.wedding.gallery.dto.request.SearchGalleryRequest;
import com.wedding.gallery.dto.response.GalleryResponseDTO;
import com.wedding.gallery.entity.GalleryItem;
import com.wedding.gallery.repository.GalleryItemRepository;
import com.wedding.gallery.service.CloudflareR2Service;
import com.wedding.gallery.service.GalleryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GalleryServiceImpl implements GalleryService {

    private final CloudflareR2Service cloudflareR2Service;
    private final GalleryItemRepository galleryRepository;
    private final CloudflareR2Access r2Access;

    @Override
    public GalleryResponseDTO create( String guestId, MultipartFile image, MultipartFile previewImage, MultipartFile voiceNote) {
//        long jumlahUpload = galleryRepository.countByGuestUuid(guestId);
//        if (jumlahUpload >= 10) {
//            throw new IllegalArgumentException("Maaf, kuota upload kamu sudah penuh (Maksimal 10 kali upload).");
//        }

        String imagePath = cloudflareR2Service.uploadFile(image, "images");
        String audioPath = cloudflareR2Service.uploadFile(voiceNote, "audios");
        String previewImagePath = cloudflareR2Service.uploadFile(previewImage, "preview_images");

        GalleryItem item = GalleryItem.builder()
                .guestUuid(guestId)
                .imagePath(imagePath)
                .imagePreviewPath(previewImagePath)
                .isApproved(true)
                .audioPath(audioPath)
                .build();

        galleryRepository.save(item);

        return getGalleryResponseDTO(item);
    }

    private GalleryResponseDTO getGalleryResponseDTO(GalleryItem item) {
        return GalleryResponseDTO.builder()
                .id(item.getId())
                .guestUuid(item.getGuestUuid())
                .imagePath(r2Access.getPublicUrl() + "/" +  item.getImagePath())
                .imagePreviewPath(item.getImagePreviewPath() != null ? r2Access.getPublicUrl() + "/" + item.getImagePreviewPath() : null)
                .audioPath(item.getAudioPath() != null ? r2Access.getPublicUrl() + "/" + item.getAudioPath() : null)
                .isApproved(item.getIsApproved())
                .createdAt(item.getCreatedAt())
                .build();
    }

    @Override
    public Page<GalleryResponseDTO> getAllGallery(SearchGalleryRequest request, String type) {
        if (request.getPage() <= 0) request.setPage(1);
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<GalleryItem> galleryItems = null;

        if (Objects.equals(type, "admin")){
            galleryItems = galleryRepository.findAll(pageable);
        } else if (Objects.equals(type, "my-gallery")) {
            galleryItems = galleryRepository.findByGuestUuidOrderByCreatedAtDesc(request.getGuestUuid(), pageable);
        } else {
            galleryItems = galleryRepository.findByIsApprovedTrueOrderByCreatedAtDesc(pageable);
        }


        List<GalleryResponseDTO> galleryResponse = galleryItems.getContent().stream()
                .map(galleryItem -> getGalleryResponseDTO(galleryItem)).toList();

        return new PageImpl<>(galleryResponse, pageable, galleryItems.getTotalElements());
    }

    @Override
    public GalleryResponseDTO updateApprovedStatus(String id) {

        GalleryItem currentItem = findByIdOrThrowNotFound(id);

        currentItem.setIsApproved(!currentItem.getIsApproved());

        galleryRepository.saveAndFlush(currentItem);


        return getGalleryResponseDTO(currentItem);
    }

    @Override
    public GalleryResponseDTO getOneById(String id) {
        GalleryItem item =findByIdOrThrowNotFound(id);

        return getGalleryResponseDTO(item);
    }

    public GalleryItem findByIdOrThrowNotFound(String id) {
        return galleryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tidak ditemukan"));
    }


}
