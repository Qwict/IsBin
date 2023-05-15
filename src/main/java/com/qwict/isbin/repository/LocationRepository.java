package com.qwict.isbin.repository;

import com.qwict.isbin.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByNameAndPlaceCode1AndPlaceCode2(String name, Integer placeCode1, Integer placeCode2);
}
