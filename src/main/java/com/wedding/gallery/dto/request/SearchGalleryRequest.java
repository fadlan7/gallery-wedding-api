package com.wedding.gallery.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchGalleryRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String guestUuid;
}