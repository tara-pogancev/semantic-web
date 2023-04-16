package com.springboot.project.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.springboot.project.dto.RequestDTO;
import com.springboot.project.service.interfaces.ChatGPTService;
import com.springboot.project.service.interfaces.SparqlService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.jena.atlas.json.JsonObject;
import org.apache.poi.util.StringUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatGPTController {

    private final SparqlService sparqlService;
    private final ChatGPTService chatGPTService;

    @PostMapping()
    public ResponseEntity<?> chat(@RequestBody String input) throws IOException {
        String chatResponse = chatGPTService.getResponse(input);
        String query = StringUtils.substringBetween(chatResponse, "\"text\":\"", "\",\"index");
        String retVal = sparqlService.chatGPTPrompt(query);
        return ResponseEntity.ok((retVal));
    }

}
