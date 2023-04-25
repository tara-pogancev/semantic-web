package com.springboot.project.controller;

import com.springboot.project.service.interfaces.ChatGPTService;
import com.springboot.project.service.interfaces.SparqlService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatGPTController {

    private final SparqlService sparqlService;
    private final ChatGPTService chatGPTService;

    @PostMapping()
    public ResponseEntity<?> chat(@RequestBody String input) throws IOException {
        String chatResponse = chatGPTService.getResponse(input);
        System.out.println(chatResponse);
        String query = StringUtils.substringBetween(chatResponse, "\"text\":\"", "\",\"index");
        String retVal = sparqlService.chatGPTPrompt(query);
        return ResponseEntity.ok((retVal));
    }

}
