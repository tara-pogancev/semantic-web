package com.springboot.project.model;

import lombok.Data;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;

@Data
public class BiboOntologyModel {

    public OntClass document;
    public OntProperty contentProprety;
    public OntProperty sectionProperty;
    public OntProperty numberProperty;
    public OntProperty citedBy;

}
