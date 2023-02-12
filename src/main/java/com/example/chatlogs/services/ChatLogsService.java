package com.example.chatlogs.services;

import com.example.chatlogs.models.AddMessageRequest;
import com.example.chatlogs.models.User;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface ChatLogsService {
    ResponseEntity<?> addMessage(Long userId, AddMessageRequest request);

    ResponseEntity<?> getMessagesOfUser(Long userId, Integer limit, Long start);

    Long getLastMessageIdOfUser(User user);

    ResponseEntity<?> deleteAllMessagesOfUser(Long userId);

    ResponseEntity<?> deleteMessageOfUser(Long userId, Long msgId);

    LocalDateTime getSentDateTimeFromId(Long id, User user);
}
