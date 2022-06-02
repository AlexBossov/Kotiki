package ru.itmo.kotiki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itmo.kotiki.dto.CatDTO;
import ru.itmo.kotiki.enums.Color;
import ru.itmo.kotiki.exception.KotikiException;

@RestController
@RequestMapping("api/cats")
public class CatController {
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public CatController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public ResponseEntity<?> findAll(Authentication authentication) {
        try {
            return ResponseEntity.ok(kafkaTemplate.send("findCats", authentication));
        } catch (KotikiException kotikiException) {
            return ResponseEntity.badRequest().body(kotikiException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @GetMapping("/findByBreed")
    public ResponseEntity<?> findAllByBreed(@RequestParam String breed, Authentication authentication) {
        try {
            return ResponseEntity.ok(kafkaTemplate.send("findCatsByBreed", authentication));
        } catch (KotikiException kotikiException) {
            return ResponseEntity.badRequest().body(kotikiException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @GetMapping("/findByColor")
    public ResponseEntity<?> findAllByColor(@RequestParam Color color, Authentication authentication) {
        try {
            return ResponseEntity.ok(kafkaTemplate.send("findCatsByColor", authentication));
        } catch (KotikiException kotikiException) {
            return ResponseEntity.badRequest().body(kotikiException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(kafkaTemplate.send("getCatById", id));
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @PostMapping
    public ResponseEntity<?> saveCat(@RequestBody CatDTO cat) {
        try {
            return ResponseEntity.ok(kafkaTemplate.send("saveCat", cat));
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCat(@RequestBody CatDTO cat) {
        try {
            return ResponseEntity.ok(kafkaTemplate.send("updateCat", cat));
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCat(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(kafkaTemplate.send("deleteCatById", id));
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }
}
