package ru.itmo.kotiki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.itmo.kotiki.dto.OwnerDTO;
import ru.itmo.kotiki.exception.KotikiException;

@RestController
@RequestMapping("api/owners")
public class OwnerController {

    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public OwnerController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public ResponseEntity<?> getOwners() {
        try {
            return ResponseEntity.ok(kafkaTemplate.send("findAllOwners", 1));
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(kafkaTemplate.send("getOwnerById", id));
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @PostMapping
    public ResponseEntity<?> saveOwner(@RequestBody OwnerDTO owner) {
        try {
            return ResponseEntity.ok(kafkaTemplate.send("saveOwner", owner).completable().join());
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateOwner(@RequestBody OwnerDTO owner) {
        try {
            return ResponseEntity.ok(kafkaTemplate.send("updateOwner", owner));
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOwner(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(kafkaTemplate.send("deleteOwner", id));
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }
}
