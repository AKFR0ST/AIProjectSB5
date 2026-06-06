package com.sb5.aiprojectsb5.controller;

import com.sb5.aiprojectsb5.entity.Place;
import com.sb5.aiprojectsb5.repository.PlaceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
@Tag(name = "Places", description = "Информация о локациях ГЭС-2")
public class PlaceController {

    private final PlaceRepository placeRepository;

    @Operation(summary = "Получить все локации")
    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces() {
        return ResponseEntity.ok(placeRepository.findAll());
    }

    @Operation(summary = "Получить локацию по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Integer id) {
        return placeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить локации по типу", description = "exhibition, lecture_hall, cafe, shop, coworking, masterclass, space, kids, media")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Place>> getPlacesByType(@PathVariable String type) {
        return ResponseEntity.ok(placeRepository.findByType(type));
    }

    @Operation(summary = "Получить бесплатные локации")
    @GetMapping("/free")
    public ResponseEntity<List<Place>> getFreePlaces() {
        return ResponseEntity.ok(placeRepository.findByIsFreeTrue());
    }

    @Operation(summary = "Поиск локаций по тегам", description = "Поиск по ключевым словам в тегах")
    @GetMapping("/search")
    public ResponseEntity<List<Place>> searchPlaces(@RequestParam String query) {
        return ResponseEntity.ok(placeRepository.findByTagContaining(query));
    }

    @Operation(summary = "Получить локации по этажу")
    @GetMapping("/floor/{floor}")
    public ResponseEntity<List<Place>> getPlacesByFloor(@PathVariable Integer floor) {
        return ResponseEntity.ok(placeRepository.findByFloor(floor));
    }
}
