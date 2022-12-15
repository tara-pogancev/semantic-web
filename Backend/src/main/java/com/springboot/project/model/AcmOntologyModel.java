package com.springboot.project.model;

import lombok.Data;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;

@Data
public class AcmOntologyModel {

    public OntClass course;
    public OntClass knowledgeArea;
    public OntClass knowledgeUnit;
    public OntClass learningResource;
    public OntClass learningOutcome;

    // data properties
    public OntProperty nameProperty;
    public OntProperty descriptionProperty;
    public OntProperty estimatedContactHoursProperty;
    public OntProperty authorProperty;
    public OntProperty difficultyLevelProperty;
    public OntProperty teacherProperty;
    public OntProperty formatProperty;
    public OntProperty levelOfStudyProperty;

    // object properties
    public OntProperty consistsOf;
    public OntProperty includes;
    public OntProperty isTaughtUsing;
    public OntProperty obtainedBy;
    public OntProperty teaches;

}
