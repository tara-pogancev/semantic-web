package com.springboot.project.service.interfaces;

import com.springboot.project.dto.RequestDTO;

public interface SparqlService {

    String getResourceForCourse(RequestDTO dto);

    String getResourceForKnowledgeUnit(RequestDTO dto);

    String getCoursesWithResourcesByAuthor(RequestDTO dto);

    String getCoursesAndAuthorsForLearningOutcome(RequestDTO dto);

    String getDocumentsCitedByAuthorInFormat(RequestDTO dto);
}
