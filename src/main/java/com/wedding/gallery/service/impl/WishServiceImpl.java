package com.wedding.gallery.service.impl;

import com.wedding.gallery.dto.request.SearchRequest;
import com.wedding.gallery.entity.WishItem;
import com.wedding.gallery.repository.WishItemRepository;
import com.wedding.gallery.service.WishService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WishServiceImpl implements WishService {

    private final WishItemRepository wishItemRepository;

    @Override
    public WishItem createWish(WishItem wishItem) {
        return wishItemRepository.save(wishItem);
    }

    @Override
    public Page<WishItem> getAllWishes(SearchRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<WishItem> wishItems = null;

        wishItems = wishItemRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<WishItem> wishItemList = wishItems.getContent().stream().map(wishItem -> WishItem.builder()
                .id(wishItem.getId())
                .name(wishItem.getName())
                .message(wishItem.getMessage())
                .attendance(wishItem.getAttendance())
                .createdAt(wishItem.getCreatedAt())
                .build()).toList();

        return new PageImpl<>(wishItemList, pageable, wishItems.getTotalElements());
    }
}
