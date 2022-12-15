package com.springboot.project.service.interfaces;

import com.springboot.project.model.AcmOntologyModel;
import com.springboot.project.model.BiboOntologyModel;
import org.apache.jena.ontology.OntModel;

import java.io.IOException;

public interface OntologyService {

    AcmOntologyModel getAcmOntologyModel() throws IOException;

    BiboOntologyModel getBiboOntologyModel() throws IOException;

    OntModel getAcmOntModel() throws IOException;

    OntModel getBiboOntModel() throws IOException;

}
