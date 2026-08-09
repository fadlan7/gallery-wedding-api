package com.wedding.gallery.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "gallery_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GalleryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guest_uuid", nullable = false, length = 36)
    private String guestUuid;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "audio_path")
    private String audioPath;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}