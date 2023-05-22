package com.springboot.project.controller;

import com.springboot.project.dto.RequestDTO;
import com.springboot.project.service.interfaces.SparqlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sparql")
@RequiredArgsConstructor
public class SparqlController {

    private final SparqlService sparqlService;

    @PostMapping("/resources-for-course")
    public ResponseEntity<?> getResourceForCourse(@RequestBody RequestDTO dto) {
        return ResponseEntity.ok(sparqlService.getResourceForCourse(dto));
    }

    @PostMapping("/resources-for-knowledge-unit")
    public ResponseEntity<?> getResourceForKnowledgeUnit(@RequestBody RequestDTO dto) {
        return ResponseEntity.ok(sparqlService.getResourceForKnowledgeUnit(dto));
    }

    @PostMapping("/courses-with-resources-by-author")
    public ResponseEntity<?> getCoursesWithResourcesByAuthor(@RequestBody RequestDTO dto) {
        return ResponseEntity.ok(sparqlService.getCoursesWithResourcesByAuthor(dto));
    }

    @PostMapping("/courses-and-authors-for-learning-outcome")
    public ResponseEntity<?> getCoursesAndAuthorsForLearningOutcome(@RequestBody RequestDTO dto) {
        return ResponseEntity.ok(sparqlService.getCoursesAndAuthorsForLearningOutcome(dto));
    }

    @PostMapping("/documents-cited-by-author-in-format")
    public ResponseEntity<?> getDocumentsCitedByAuthorInFormat(@RequestBody RequestDTO dto) {
        return ResponseEntity.ok(sparqlService.getDocumentsCitedByAuthorInFormat(dto));
    }

}
