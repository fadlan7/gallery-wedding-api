package com.wedding.gallery.repository;

import com.wedding.gallery.entity.GalleryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryItemRepository extends JpaRepository<GalleryItem, Long> {
    long countByGuestUuid(String guestUuid);
    List<GalleryItem> findByIsApprovedTrueOrderByCreatedAtDesc();
    List<GalleryItem> findByGuestUuidOrderByCreatedAtDesc(String guestUuid);
    List<GalleryItem> findAllByOrderByCreatedAtDesc();
}