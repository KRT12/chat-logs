package com.example.chatlogs.repository;

import com.example.chatlogs.models.ChatMessage;
import com.example.chatlogs.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {


    public List<ChatMessage> findByUserAndIdLessThanEqualOrderByIdDesc(User user,Long id,Pageable pageable);
    public List<ChatMessage> findByUserAndSentDateTimeLessThanEqualOrderBySentDateTimeDesc(User user,
                                                                                           LocalDateTime sentDateTime,
                                                                                           Pageable pageable);

    Optional<ChatMessage> findTopByUserOrderById(User user);
    Optional<ChatMessage> findTopByUserOrderByIdDesc(User user);
    Optional<ChatMessage> findTopByUserOrderBySentDateTimeDesc(User user);

    void deleteAllByUser(User user);

    Optional<ChatMessage> findByUserAndId(User user, Long id);
}
