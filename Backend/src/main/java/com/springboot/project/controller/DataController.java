package com.springboot.project.controller;


import com.springboot.project.dto.FileUploadDTO;
import com.springboot.project.service.interfaces.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    @GetMapping("/upload")
    public ResponseEntity<?> generateRdfFile(@RequestBody FileUploadDTO dto) throws IOException {
        return ResponseEntity.ok(dataService.uploadFile(dto));
    }

}
