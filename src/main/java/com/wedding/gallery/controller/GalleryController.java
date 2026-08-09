package com.wedding.gallery.controller;

import com.wedding.gallery.constant.ApiUrl;
import com.wedding.gallery.dto.response.CommonResponse;
import com.wedding.gallery.dto.response.GalleryResponseDTO;
import com.wedding.gallery.service.GalleryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping(path = ApiUrl.API_GALLERY)
public class GalleryController {

    private final GalleryService galleryService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/upload"
    )
    public ResponseEntity<CommonResponse<GalleryResponseDTO>> create(
            @RequestPart(name = "guestUuid") String guestId,
            @RequestParam(name = "image", required = false) MultipartFile image,
            @RequestParam(name = "voiceNote", required = false) MultipartFile voiceNote)
    {

        try{

            GalleryResponseDTO upload = galleryService.create(guestId, image, voiceNote);

            CommonResponse<GalleryResponseDTO> response = CommonResponse.<GalleryResponseDTO>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully create new product")
                    .data(upload)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {

            CommonResponse<GalleryResponseDTO> response = CommonResponse.<GalleryResponseDTO>builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal server error")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
