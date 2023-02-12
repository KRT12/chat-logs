package com.example.chatlogs.controllers;

import com.example.chatlogs.models.AddMessageRequest;
import com.example.chatlogs.models.BaseResponse;
import com.example.chatlogs.services.ChatLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/chatlogs/")
public class ChatLogsController {

    private final ChatLogsService chatLogsService;

    @Autowired
    public ChatLogsController(ChatLogsService chatLogsService) {
        this.chatLogsService = chatLogsService;
    }

    @PostMapping("{userId}")
    public ResponseEntity<?> addMessage(@PathVariable Long userId, @Valid @RequestBody AddMessageRequest request,
                                        BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            BaseResponse response =new BaseResponse();
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            response.setSuccess(false);
            for (FieldError e : errors) {
                message.add( e.getField() + "-" + e.getDefaultMessage());
            }
            response.setMessage(message.toString());
            return new ResponseEntity<BaseResponse>(response, HttpStatus.BAD_REQUEST);
        }
        return chatLogsService.addMessage(userId,request);

    }

    @GetMapping("{userId}")
    public ResponseEntity<?> getMessagesOfUser(@PathVariable Long userId,
            @RequestParam(value = "limit",required = false) Integer limit,
            @RequestParam(value = "start",required = false) Long start){
        return chatLogsService.getMessagesOfUser(userId,limit,start);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<?> deleteMessagesOfUser(@PathVariable Long userId){
        return chatLogsService.deleteAllMessagesOfUser(userId);
    }
    @DeleteMapping("{userId}/{msgId}")
    public ResponseEntity<?> deleteMessageOfUser(@PathVariable Long userId,
                                               @PathVariable Long msgId){
        return chatLogsService.deleteMessageOfUser(userId,msgId);
    }
}
