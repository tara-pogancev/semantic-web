package com.springboot.project.model;

import lombok.Data;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;

@Data
public class AcmOntologyModel {

    OntClass course;
    OntClass knowledgeArea;
    OntClass knowledgeUnit;
    OntClass learningResource;
    OntClass learningOutcome;

    // data properties
    OntProperty nameProperty;
    OntProperty descriptionProperty;
    OntProperty estimatedContactHoursProperty;
    OntProperty authorProperty;
    OntProperty difficultyLevelProperty;
    OntProperty teacherProperty;
    OntProperty formatProperty;
    OntProperty levelOfStudyProperty;

    // object properties
    OntProperty consistsOf;
    OntProperty includes;
    OntProperty isTaughtUsing;
    OntProperty obtainedBy;
    OntProperty teaches;

}
