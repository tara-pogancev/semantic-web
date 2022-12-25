package com.springboot.project.controller;

import com.springboot.project.dto.RequestDTO;
import com.springboot.project.service.interfaces.SparqlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sparql")
@RequiredArgsConstructor
public class SparqlController {

    private final SparqlService sparqlService;

    @GetMapping("/resources-for-course")
    public ResponseEntity<?> getResourceForCourse(@RequestBody RequestDTO dto) {
        return ResponseEntity.ok(sparqlService.getResourceForCourse(dto));
    }

}
