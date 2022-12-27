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

    @GetMapping("/resources-for-knowledge-unit")
    public ResponseEntity<?> getResourceForKnowledgeUnit(@RequestBody RequestDTO dto) {
        return ResponseEntity.ok(sparqlService.getResourceForKnowledgeUnit(dto));
    }

    @GetMapping("/courses-with-resources-by-author")
    public ResponseEntity<?> getCoursesWithResourcesByAuthor(@RequestBody RequestDTO dto) {
        return ResponseEntity.ok(sparqlService.getCoursesWithResourcesByAuthor(dto));
    }

    @GetMapping("/courses-and-authors-for-learning-outcome")
    public ResponseEntity<?> getCoursesAndAuthorsForLearningOutcome (@RequestBody RequestDTO dto) {
        return ResponseEntity.ok(sparqlService.getCoursesAndAuthorsForLearningOutcome(dto));
    }

    @GetMapping("/documents-cited-by-author-in-format")
    public ResponseEntity<?> getDocumentsCitedByAuthorInFormat (@RequestBody RequestDTO dto) {
        return ResponseEntity.ok(sparqlService.getDocumentsCitedByAuthorInFormat(dto));
    }


}
