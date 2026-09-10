package com.wedding.gallery.controller;

import com.wedding.gallery.constant.ApiUrl;
import com.wedding.gallery.dto.request.SearchGalleryRequest;
import com.wedding.gallery.dto.response.CommonResponse;
import com.wedding.gallery.dto.response.GalleryResponseDTO;
import com.wedding.gallery.dto.response.PagingResponse;
import com.wedding.gallery.service.GalleryService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
            @RequestParam(name = "image") MultipartFile image,
            @RequestParam(name = "imagePreview") MultipartFile imagePreview,
            @RequestParam(name = "voiceNote", required = false) MultipartFile voiceNote) {
        try{
            GalleryResponseDTO upload = galleryService.create(guestId, image, imagePreview, voiceNote);

            CommonResponse<GalleryResponseDTO> response = CommonResponse.<GalleryResponseDTO>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully upload")
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

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<GalleryResponseDTO>> getGalleryById(@PathVariable String id) {
        GalleryResponseDTO menu = galleryService.getOneById(id);

        CommonResponse<GalleryResponseDTO> response = CommonResponse.<GalleryResponseDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get data")
                .data(menu)
                .build();

        return ResponseEntity.ok(response);
    }
    @GetMapping(path = "/galleries")
    public ResponseEntity<CommonResponse<List<GalleryResponseDTO>>> userGallery(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "direction", defaultValue = "desc") String direction,
            @RequestParam(name = "guestUuid", required = false) String guestUuid
    ){
        SearchGalleryRequest request = SearchGalleryRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .guestUuid(guestUuid)
                .build();
        Page<GalleryResponseDTO> gallery = galleryService.getAllGallery(request, "");

        return getCommonResponseResponseEntity(gallery);
    }

    @GetMapping(path = "/admin/galleries")
    public ResponseEntity<CommonResponse<List<GalleryResponseDTO>>> adminGallery(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "direction", defaultValue = "desc") String direction
    ){
        SearchGalleryRequest request = SearchGalleryRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .build();
        Page<GalleryResponseDTO> gallery = galleryService.getAllGallery(request, "admin");

        return getCommonResponseResponseEntity(gallery);
    }

    @GetMapping(path = "/my-galleries")
    public ResponseEntity<CommonResponse<List<GalleryResponseDTO>>> getMyGallery(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "direction", defaultValue = "desc") String direction
    ){
        SearchGalleryRequest request = SearchGalleryRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .build();
        Page<GalleryResponseDTO> gallery = galleryService.getAllGallery(request, "my-gallery");

        return getCommonResponseResponseEntity(gallery);
    }


    @PutMapping(name = "update-status")
    public ResponseEntity<CommonResponse<?>> updateMenu(
            @RequestParam(name = "id") String id
    ) {
        try {
            GalleryResponseDTO updatedMenu = galleryService.updateApprovedStatus(id);

            CommonResponse<GalleryResponseDTO> response = CommonResponse.<GalleryResponseDTO>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully update approved status")
                    .data(updatedMenu)
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


    @GetMapping(path = "/health-check")
    public ResponseEntity<CommonResponse<String>> userGallery(
    ){
        PagingResponse pagingResponse = PagingResponse.builder().build();

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success")
                .data("healthy")
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @NonNull
    private ResponseEntity<CommonResponse<List<GalleryResponseDTO>>> getCommonResponseResponseEntity(Page<GalleryResponseDTO> gallery) {
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(gallery.getTotalPages())
                .totalElement(gallery.getTotalElements())
                .page(gallery.getPageable().getPageNumber() + 1)
                .size(gallery.getPageable().getPageSize())
                .hasNext(gallery.hasNext())
                .hasPrevious(gallery.hasPrevious())
                .build();

        CommonResponse<List<GalleryResponseDTO>> response = CommonResponse.<List<GalleryResponseDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get all data")
                .data(gallery.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}