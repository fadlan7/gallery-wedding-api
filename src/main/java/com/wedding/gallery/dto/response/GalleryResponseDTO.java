package com.wedding.gallery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GalleryResponseDTO {

    private String id;
    private String guestUuid;
    private String imagePath;
    private String audioPath;
    private Boolean isApproved;
    private LocalDateTime createdAt;
}
