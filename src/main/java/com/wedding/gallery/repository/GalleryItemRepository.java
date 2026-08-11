package com.wedding.gallery.repository;

import com.wedding.gallery.entity.GalleryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GalleryItemRepository extends JpaRepository<GalleryItem, Long> {
    long countByGuestUuid(String guestUuid);
    Page<GalleryItem> findByIsApprovedTrueOrderByCreatedAtDesc(Pageable pageable);
    Page<GalleryItem> findByGuestUuidOrderByCreatedAtDesc(String guestUuid,Pageable pageable);
    List<GalleryItem> findAllByOrderByCreatedAtDesc();
}