package com.akshay.electronic.store.ElectronicStore.helper;

import com.akshay.electronic.store.ElectronicStore.dtos.PageableResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public class Helper {

    public static <U, V> PageableResponse<V> getPageableResponse(
            Page<U> page,
            Function<U, V> converter) {

        List<V> dtoList = page.getContent()
                .stream()
                .map(converter)
                .toList();

        PageableResponse<V> response = new PageableResponse<>();

        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElement(page.getTotalElements());
        response.setLastPage(page.isLast());

        return response;
    }
}