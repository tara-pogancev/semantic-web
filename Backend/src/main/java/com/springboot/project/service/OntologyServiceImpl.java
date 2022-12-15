package com.springboot.project.service;

import com.springboot.project.model.AcmOntologyModel;
import com.springboot.project.model.BiboOntologyModel;
import com.springboot.project.service.interfaces.OntologyService;
import lombok.RequiredArgsConstructor;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OntologyServiceImpl implements OntologyService {

    private static final String ACM_ONTOLOGY = "sec_ontology.owl";
    private static final String BIBO_ONTOLOGY = "bibo.owl";
    private static final String ACM_URI_PREFIX = "http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#";
    private static final String BIBO_URI_PREFIX = "http://purl.org/ontology/bibo#";

    @Override
    public AcmOntologyModel getAcmOntologyModel(OntModel ontModel) throws IOException {
        AcmOntologyModel model = new AcmOntologyModel();

        // classes
        model.setCourse( ontModel.getResource(ACM_URI_PREFIX + "Course").as(OntClass.class));
        model.setKnowledgeArea(ontModel.getResource(ACM_URI_PREFIX + "KnowledgeArea").as(OntClass.class));
        model.setKnowledgeUnit(ontModel.getResource(ACM_URI_PREFIX + "KnowledgeUnit").as(OntClass.class));
        model.setLearningResource(ontModel.getResource(ACM_URI_PREFIX + "LearningResource").as(OntClass.class));
        model.setLearningOutcome(ontModel.getResource(ACM_URI_PREFIX + "LearningOutcome").as(OntClass.class));

        // data properties
        model.setNameProperty(ontModel.getProperty(ACM_URI_PREFIX + "name").as(OntProperty.class));
        model.setDescriptionProperty(ontModel.getProperty(ACM_URI_PREFIX + "description").as(OntProperty.class));
        model.setEstimatedContactHoursProperty(ontModel.getProperty(ACM_URI_PREFIX + "estimatedContactHours").as(OntProperty.class));
        model.setAuthorProperty(ontModel.getProperty(ACM_URI_PREFIX + "author").as(OntProperty.class));
        model.setDifficultyLevelProperty(ontModel.getProperty(ACM_URI_PREFIX + "description").as(OntProperty.class));
        model.setTeacherProperty(ontModel.getProperty(ACM_URI_PREFIX + "teacher").as(OntProperty.class));
        model.setFormatProperty(ontModel.getProperty(ACM_URI_PREFIX + "format").as(OntProperty.class));
        model.setLevelOfStudyProperty(ontModel.getProperty(ACM_URI_PREFIX + "levelOfStudyProperty").as(OntProperty.class));

        // object properties
        model.setConsistsOf(ontModel.getResource(ACM_URI_PREFIX + "consistsOf").as(OntProperty.class));
        model.setIncludes(ontModel.getResource(ACM_URI_PREFIX + "includes").as(OntProperty.class));
        model.setIsTaughtUsing(ontModel.getResource(ACM_URI_PREFIX + "isTaughtUsing").as(OntProperty.class));
        model.setObtainedBy(ontModel.getResource(ACM_URI_PREFIX + "obtainedBy").as(OntProperty.class));
        model.setTeaches(ontModel.getResource(ACM_URI_PREFIX + "teaches").as(OntProperty.class));

        return model;
    }

    @Override
    public BiboOntologyModel getBiboOntologyModel(OntModel ontModel) throws IOException {
        BiboOntologyModel model = new BiboOntologyModel();

        model.setContentProprety(ontModel.getProperty(ACM_URI_PREFIX + "content").as(OntProperty.class));
        model.setNumberProperty(ontModel.getProperty(ACM_URI_PREFIX + "number").as(OntProperty.class));
        model.setSectionProperty(ontModel.getProperty(ACM_URI_PREFIX + "section").as(OntProperty.class));
        model.setCitedBy(ontModel.getProperty(ACM_URI_PREFIX + "citedBy").as(OntProperty.class));

        return model;
    }

    @Override
    public OntModel getAcmOntModel() throws IOException {
        OntModel ontModel =  ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        ontModel.read(new FileInputStream(new ClassPathResource(ACM_ONTOLOGY).getFile()), "");
        ontModel.setStrictMode(false);
        ontModel.setNsPrefix("acm", ACM_URI_PREFIX);
        return  ontModel;
    }

    @Override
    public OntModel getBiboOntModel() throws IOException {
        OntModel ontModel =  ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        ontModel.read(new FileInputStream(new ClassPathResource(BIBO_ONTOLOGY).getFile()), "");
        ontModel.setStrictMode(false);
        ontModel.setNsPrefix("bibo", BIBO_URI_PREFIX);
        return ontModel;
    }

}
