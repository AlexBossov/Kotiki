package ru.itmo.kotiki.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.postgresql.core.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itmo.kotiki.dto.CatDTO;
import ru.itmo.kotiki.enums.Color;
import ru.itmo.kotiki.exception.KotikiException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/cats")
public class CatController {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    @Autowired
    public CatController(KafkaTemplate<String, Object> kafkaTemplate,
                         ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    @GetMapping
    public ResponseEntity<?> findAll(Authentication authentication) {
        try {
            var record = new ProducerRecord<>("getCats", "key-1", (Object) authentication);
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "reply_message".getBytes()));
            RequestReplyFuture<String, Object, Object> replyFuture = replyingKafkaTemplate.sendAndReceive(record);
            ConsumerRecord<String, Object> consumerRecord = replyFuture.get();
            return ResponseEntity.ok(consumerRecord.value());
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
            var record = new ProducerRecord<>("getCatByID", "key-1", (Object) id);
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "reply_message".getBytes()));
            RequestReplyFuture<String, Object, Object> replyFuture = replyingKafkaTemplate.sendAndReceive(record);
            ConsumerRecord<String, Object> consumerRecord = replyFuture.get();
            return ResponseEntity.ok(consumerRecord.value());
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @PostMapping
    public ResponseEntity<?> saveCat(@RequestBody CatDTO cat) {
        try {
            kafkaTemplate.send("saveCat", cat);
            return ResponseEntity.ok("Saved!");
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCat(@RequestBody CatDTO cat) {
        try {
            kafkaTemplate.send("updateCat", cat);
            return ResponseEntity.ok("Updated!");
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCat(@PathVariable Long id) {
        try {
            kafkaTemplate.send("deleteCatById", id);
            return ResponseEntity.ok("Deleted!");
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }
}
