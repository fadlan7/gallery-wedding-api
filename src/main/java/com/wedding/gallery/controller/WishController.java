package com.wedding.gallery.controller;

import com.wedding.gallery.constant.ApiUrl;
import com.wedding.gallery.dto.request.SearchRequest;
import com.wedding.gallery.dto.response.CommonResponse;
import com.wedding.gallery.dto.response.PagingResponse;
import com.wedding.gallery.entity.WishItem;
import com.wedding.gallery.service.WishService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = ApiUrl.API_WISHES)
public class WishController {
    private final WishService wishService;

    @PostMapping
    public ResponseEntity<CommonResponse<WishItem>> createWish(@RequestBody WishItem wish) {
        WishItem savedWish = wishService.createWish(wish);

        CommonResponse<WishItem> response = CommonResponse.<WishItem>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully created")
                .data(savedWish)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<CommonResponse<List<WishItem>>> getWishes(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "direction", defaultValue = "desc") String direction
    ){

        SearchRequest request = SearchRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .build();
        Page<WishItem> wishes = wishService.getAllWishes(request);


        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(wishes.getTotalPages())
                .totalElement(wishes.getTotalElements())
                .page(wishes.getPageable().getPageNumber() + 1)
                .size(wishes.getPageable().getPageSize())
                .hasNext(wishes.hasNext())
                .hasPrevious(wishes.hasPrevious())
                .build();

        CommonResponse<List<WishItem>> response = CommonResponse.<List<WishItem>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get all data")
                .data(wishes.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

}


