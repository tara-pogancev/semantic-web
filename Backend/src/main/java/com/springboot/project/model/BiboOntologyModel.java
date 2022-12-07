package com.springboot.project.model;

import lombok.Data;
import org.apache.jena.ontology.OntProperty;

@Data
public class BiboOntologyModel {

    OntProperty contentProprety;
    OntProperty sectionProperty;
    OntProperty numberProperty;
    OntProperty citedBy;

}
