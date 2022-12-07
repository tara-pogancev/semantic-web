package com.springboot.project.service;

import com.springboot.project.service.interfaces.DataService;
import lombok.RequiredArgsConstructor;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private static final String ACM_ONTOLOGY = "sec_ontology.owl";
    private static final String BIBO_ONTOLOGY = "bibo.owl";
    private static final String DATA_FILE = "data.rdf";
    private static final String ACM_URI_PREFIX = "http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#";

    @Override
    public String generateRdfFile() throws IOException {
        String personURI  = "http://localhost/amitkumar";
        String givenName  = "Amit";
        String familyName = "Kumar";
        String fullName = givenName+familyName;

        Model model = ModelFactory.createDefaultModel();

        OntModel ontModel =  ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        ontModel.read(new FileInputStream(new ClassPathResource(ACM_ONTOLOGY).getFile()), "");
        ontModel.setStrictMode(false);

        // classes
        OntClass course = ontModel.getResource(ACM_URI_PREFIX + "Course").as(OntClass.class);
        OntClass knowledgeArea = ontModel.getResource(ACM_URI_PREFIX + "KnowledgeArea").as(OntClass.class);
        OntClass knowledgeUnit = ontModel.getResource(ACM_URI_PREFIX + "KnowledgeUnit").as(OntClass.class);
        OntClass learningResource = ontModel.getResource(ACM_URI_PREFIX + "LearningResource").as(OntClass.class);
        OntClass learningOutcome = ontModel.getResource(ACM_URI_PREFIX + "LearningOutcome").as(OntClass.class);

        // data properties
        OntProperty nameProperty = ontModel.getProperty(ACM_URI_PREFIX + "name").as(OntProperty.class);
        OntProperty descriptionProperty = ontModel.getProperty(ACM_URI_PREFIX + "description").as(OntProperty.class);
        OntProperty estimatedContactHoursProperty = ontModel.getProperty(ACM_URI_PREFIX + "estimatedContactHours").as(OntProperty.class);
        OntProperty authorProperty = ontModel.getProperty(ACM_URI_PREFIX + "author").as(OntProperty.class);
        OntProperty difficultyLevelProperty = ontModel.getProperty(ACM_URI_PREFIX + "description").as(OntProperty.class);
        OntProperty teacherProperty = ontModel.getProperty(ACM_URI_PREFIX + "teacher").as(OntProperty.class);
        OntProperty formatProperty = ontModel.getProperty(ACM_URI_PREFIX + "format").as(OntProperty.class);
        OntProperty levelOfStudyProperty = ontModel.getProperty(ACM_URI_PREFIX + "levelOfStudyProperty").as(OntProperty.class);

        // object properties
        OntProperty consistsOf = ontModel.getResource(ACM_URI_PREFIX + "consistsOf").as(OntProperty.class);
        OntProperty includes = ontModel.getResource(ACM_URI_PREFIX + "includes").as(OntProperty.class);
        OntProperty isTaughtUsing = ontModel.getResource(ACM_URI_PREFIX + "isTaughtUsing").as(OntProperty.class);
        OntProperty obtainedBy = ontModel.getResource(ACM_URI_PREFIX + "obtainedBy").as(OntProperty.class);
        OntProperty teaches = ontModel.getResource(ACM_URI_PREFIX + "teaches").as(OntProperty.class);

        Resource node = model.createResource(personURI)
                        .addProperty(nameProperty, "Tara Pogancev")
                        .addProperty(descriptionProperty, "tarin opis");

        model.write(System.out, "TURTLE");

        FileWriter out = new FileWriter(new ClassPathResource(DATA_FILE).getFile());
        model.write(out, "RDF/XML");
        out.close();

        return "hahaha";
    }

}
