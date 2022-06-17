package ru.itmo.kotiki.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;
import ru.itmo.kotiki.dto.OwnerDTO;
import ru.itmo.kotiki.exception.KotikiException;

import java.util.List;

@RestController
@RequestMapping("api/owners")
public class OwnerController {

//    private KafkaTemplate<String, Object> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, Long, OwnerDTO> replyingKafkaTemplate;


//    @Autowired
//    public OwnerController(KafkaTemplate<String, Object> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }

    @Autowired
    public OwnerController(ReplyingKafkaTemplate<String, Long, OwnerDTO> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

//    @GetMapping
//    public ResponseEntity<?> getOwners() {
//        try {
//            return ResponseEntity.ok(kafkaTemplate.send("findAllOwners", 1));
//        } catch (KotikiException ownerException) {
//            return ResponseEntity.badRequest().body(ownerException.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("An error has occurred");
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            var record = new ProducerRecord<>("asyn_message", "key-1", id);
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "reply_message".getBytes()));
            RequestReplyFuture<String, Long, OwnerDTO> replyFuture = replyingKafkaTemplate.sendAndReceive(record);
            ConsumerRecord<String, OwnerDTO> consumerRecord = replyFuture.get();
            return ResponseEntity.ok(consumerRecord.value());
        } catch (KotikiException ownerException) {
            return ResponseEntity.badRequest().body(ownerException.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

//    @PostMapping
//    public ResponseEntity<?> saveOwner(@RequestBody OwnerDTO owner) {
//        try {
//            return ResponseEntity.ok(kafkaTemplate.send("saveOwner", owner).completable().join());
//        } catch (KotikiException ownerException) {
//            return ResponseEntity.badRequest().body(ownerException.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("An error has occurred");
//        }
//    }
//
//    @PutMapping
//    public ResponseEntity<?> updateOwner(@RequestBody OwnerDTO owner) {
//        try {
//            return ResponseEntity.ok(kafkaTemplate.send("updateOwner", owner));
//        } catch (KotikiException ownerException) {
//            return ResponseEntity.badRequest().body(ownerException.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("An error has occurred");
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteOwner(@PathVariable Long id) {
//        try {
//            return ResponseEntity.ok(kafkaTemplate.send("deleteOwner", id));
//        } catch (KotikiException ownerException) {
//            return ResponseEntity.badRequest().body(ownerException.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("An error has occurred");
//        }
//    }
}
