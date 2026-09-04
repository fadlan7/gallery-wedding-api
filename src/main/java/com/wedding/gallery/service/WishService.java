package com.wedding.gallery.service;

import com.wedding.gallery.dto.request.SearchRequest;
import com.wedding.gallery.entity.WishItem;
import org.springframework.data.domain.Page;

public interface WishService {
    WishItem createWish(WishItem wishItem);
    Page<WishItem> getAllWishes(SearchRequest request);
}
