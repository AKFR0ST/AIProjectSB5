package com.sb5.aiprojectsb5.repository;

import com.sb5.aiprojectsb5.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Integer> {

    Optional<Place> findByName(String name);

    List<Place> findByType(String type);

    List<Place> findByIsFreeTrue();

    List<Place> findByFloor(Integer floor);

    // Временно убираем сложные запросы с массивами
    // @Query("SELECT p FROM Place p WHERE :tag = ANY (p.tags)")
    // List<Place> findByTag(@Param("tag") String tag);

    // Вместо этого используем LIKE для поиска по строке tags
    @Query("SELECT p FROM Place p WHERE p.tags LIKE %:tag%")
    List<Place> findByTagContaining(@Param("tag") String tag);

    @Query("SELECT p FROM Place p ORDER BY p.type")
    List<Place> findAllOrderByType();
}