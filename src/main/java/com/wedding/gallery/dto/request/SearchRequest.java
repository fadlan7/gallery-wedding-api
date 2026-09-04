package com.wedding.gallery.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SearchRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String guestUuid;
}