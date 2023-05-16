package com.qwict.isbin.service;

import com.qwict.isbin.dto.LocationDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.model.Location;
import com.qwict.isbin.repository.LocationRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {
    private LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location saveLocation(LocationDto locationDto, Book book) {
        Location location = new Location(
                locationDto.getName(), locationDto.getPlaceCode1(),
                locationDto.getPlaceCode2(), book
        );
        if (locationRepository.findByNameAndPlaceCode1AndPlaceCode2(
                locationDto.getName(), locationDto.getPlaceCode1(), locationDto.getPlaceCode2()) == null
        ) {
            return location;
        } else {
            throw new DuplicateKeyException("Location already taken!");
        }
    }

    @Override
    public boolean locationAlreadyExists(String name, Integer placeCode1, Integer placeCode2) {
        return locationRepository.findByNameAndPlaceCode1AndPlaceCode2(name, placeCode1, placeCode2) != null;
    }
}
