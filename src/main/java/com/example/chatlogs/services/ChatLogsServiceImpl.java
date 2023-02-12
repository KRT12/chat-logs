package com.example.chatlogs.services;


import com.example.chatlogs.common.Utils;
import com.example.chatlogs.models.*;
import com.example.chatlogs.repository.ChatMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChatLogsServiceImpl implements ChatLogsService{

    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    @Autowired
    public ChatLogsServiceImpl(ChatMessageRepository chatMessageRepository, UserService userService) {
        this.chatMessageRepository = chatMessageRepository;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<?> addMessage(Long userId, AddMessageRequest request) {
        User user = userService.findById(userId);
        if(user == null){
            return this.getResponseForInvalidUser(userId);
        }
        ChatMessage message = this.buildChatMessage(user,request);
        AddMessageResponse response;
        try {
            ChatMessage savedMessage = chatMessageRepository.save(message);
            response = AddMessageResponse.builder().messageId(savedMessage.getId()).build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            BaseResponse errorResponse = BaseResponse.builder().
                                    success(false).
                                    message("Something Went Wrong").build();
            return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getMessagesOfUser(Long userId, Integer limit, Long start) {
        User user = userService.findById(userId);
        if(user == null){
            return this.getResponseForInvalidUser(userId);
        }
        limit = limit != null ? limit : 10;
        start = start != null ? start : this.getLastMessageIdOfUser(user);
        LocalDateTime lastDateTime = this.getSentDateTimeFromId(start,user);
        Pageable pageWithElements = PageRequest.of(0,limit);
        List<ChatMessage> messages = chatMessageRepository.
                findByUserAndSentDateTimeLessThanEqualOrderBySentDateTimeDesc(user,lastDateTime,pageWithElements);
        return new ResponseEntity<>(messages,HttpStatus.OK);
    }

    @Override
    public Long getLastMessageIdOfUser(User user) {
        Optional<ChatMessage> chatMessageOptional = chatMessageRepository.findTopByUserOrderBySentDateTimeDesc(user);
        if(chatMessageOptional.isPresent())
            return chatMessageOptional.get().getId();
        return Long.MAX_VALUE;
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAllMessagesOfUser(Long userId) {
        User user = userService.findById(userId);
        if(user == null){
            return this.getResponseForInvalidUser(userId);
        }
        try{
            chatMessageRepository.deleteAllByUser(user);
            return new ResponseEntity<>(new BaseResponse(true,"Chats deleted"),
                    HttpStatus.OK);
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            return new ResponseEntity<>(new BaseResponse(false,ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteMessageOfUser(Long userId, Long msgId) {
        User user = userService.findById(userId);
        if(user == null){
            return this.getResponseForInvalidUser(userId);
        }
        Optional<ChatMessage> messageOptional = chatMessageRepository.findById(msgId);
        if(messageOptional.isEmpty()){
            return new ResponseEntity<>(new BaseResponse(false,"Invalid Message Id"),
                    HttpStatus.BAD_REQUEST);
        }
        ChatMessage message = messageOptional.get();
        if(!message.getUser().equals(user)){
            return new ResponseEntity<>(new
                    BaseResponse(false,"Given Message Does not belongs to this user"),
                    HttpStatus.BAD_REQUEST);
        }
        try{
            chatMessageRepository.deleteById(msgId);
            return new ResponseEntity<>(new BaseResponse(true,"Message deleted"),
                    HttpStatus.OK);
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            return new ResponseEntity<>(new BaseResponse(false,ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public LocalDateTime getSentDateTimeFromId(Long id, User user) {
        Optional<ChatMessage> messageOptional = chatMessageRepository.findByUserAndId(user,id);
        if(messageOptional.isPresent())
            return messageOptional.get().getSentDateTime();
        return LocalDateTime.of(9999,12,31,23,59);
    }

    private ResponseEntity<?> getResponseForInvalidUser(Long userId) {
        BaseResponse response = BaseResponse.builder().
                message("Invalid User id").success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ChatMessage buildChatMessage(User user,AddMessageRequest request) {
        Long sentTime = request.getTimestamp();
        LocalDateTime sentLocalDateTime = Utils.getLocalDateTimeFromEpochTime(sentTime);
        LocalDateTime currentTime = Utils.getCurrentTime();
        ChatMessage message = ChatMessage.builder().
                                user(user).
                                message(request.getMessage()).
                                sentDateTime(sentLocalDateTime).
                                receivedDateTime(currentTime).
                                isReceived(true).build();
        return message;
    }
}
