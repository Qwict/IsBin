package com.qwict.isbin.service;

import com.qwict.isbin.dto.LocationDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.model.Location;

public interface LocationService {
    Location saveLocation(LocationDto locationDto, Book book);
}
