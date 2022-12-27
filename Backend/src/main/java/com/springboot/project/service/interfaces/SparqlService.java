package com.springboot.project.service.interfaces;

import com.springboot.project.dto.RequestDTO;

public interface SparqlService {

    String getResourceForCourse(RequestDTO dto);

    String getResourceForKnowledgeUnit(RequestDTO dto);

}
