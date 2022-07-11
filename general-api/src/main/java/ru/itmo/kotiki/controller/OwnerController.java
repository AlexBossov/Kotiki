package ru.itmo.kotiki.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.kotiki.dto.OwnerDTO;
import ru.itmo.kotiki.exception.KotikiException;

@RestController
@RequestMapping("api/owners")
public class OwnerController {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    @Autowired
    public OwnerController(ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate,
                           KafkaTemplate<String, Object> kafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public ResponseEntity<?> getOwners() {
        try {
            var record = new ProducerRecord<>("getOwners", "key-1", new Object());
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

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            var record = new ProducerRecord<>("ownerGetById", "key-1", (Object) id);
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
    public ResponseEntity<?> saveOwner(@RequestBody OwnerDTO owner) {
        try {
            kafkaTemplate.send("saveOwner", owner);
            return ResponseEntity.ok("Saved!");
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateOwner(@RequestBody OwnerDTO owner) {
        try {
            kafkaTemplate.send("updateOwner", owner);
            return ResponseEntity.ok("Updated!");
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOwner(@PathVariable Long id) {
        try {
            kafkaTemplate.send("deleteOwnerById", id);
            return ResponseEntity.ok("Deleted!");
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }
}
