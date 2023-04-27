package com.qwict.isbin.repository;

import com.qwict.isbin.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {
}
