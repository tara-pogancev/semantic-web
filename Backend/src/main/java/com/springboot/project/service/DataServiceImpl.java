package com.springboot.project.service;

import com.springboot.project.dto.BiboReferencesDTO;
import com.springboot.project.dto.FileUploadDTO;
import com.springboot.project.model.AcmOntologyModel;
import com.springboot.project.model.BiboOntologyModel;
import com.springboot.project.service.interfaces.DataService;
import com.springboot.project.service.interfaces.OntologyService;
import lombok.RequiredArgsConstructor;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private static final String DATA_FILE = ".\\src\\main\\resources\\data.rdf";
    private static final String ACM_URI_PREFIX = "http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#";
    private static final String BIBO_URI_PREFIX = "http://purl.org/ontology/bibo/";

    private final OntologyService ontologyService;

    @Override
    public String uploadFile(FileUploadDTO dto) throws IOException {
        AcmOntologyModel acmOntologyModel = ontologyService.getAcmOntologyModel();
        BiboOntologyModel biboOntologyModel = ontologyService.getBiboOntologyModel();

        // TODO: Uraditi preko individuala i ponovo upisati u fajl
        // TODO: popuniti rdf fajl podacima iz acm

        Model model = ModelFactory.createDefaultModel();
        model.createResource(getUriLearningResource(dto.name))
                .addProperty(acmOntologyModel.getDifficultyLevelProperty(), dto.difficultyLevel.toString())
                .addProperty(acmOntologyModel.getFormatProperty(), dto.format)
                .addProperty(acmOntologyModel.getNameProperty(), dto.name)
                .addProperty(acmOntologyModel.getAuthorProperty(), dto.author);

        for (BiboReferencesDTO biboReference : dto.cites) {
            model.createResource(getUriBiboDocument(biboReference.content))
                    .addProperty(biboOntologyModel.getContentProprety(), biboReference.content)
                    .addProperty(biboOntologyModel.getSectionProperty(), biboReference.section.toString())
                    .addProperty(biboOntologyModel.getNumberProperty(), biboReference.number.toString())
                    .addProperty(biboOntologyModel.getCitedBy(), getUriLearningResource(biboReference.content))
                    ;
        }

        FileWriter out = new FileWriter(DATA_FILE, true);
        model.write(out, "RDF/XML");
        out.write("\n\n");
        out.close();

        return "Successfully written into RDF file";
    }

    @Override
    public String getUriLearningResource(String name) {
        String prefix = ACM_URI_PREFIX.replace("#", "/");
        return prefix + "learning-resource/" + name.toLowerCase().replace(" ", "+");
    }

    @Override
    public String getUriBiboDocument(String name) {
        String prefix = BIBO_URI_PREFIX.replace("#", "/");
        return prefix + "bibo-document/" + name.toLowerCase().replace(" ", "+");
    }

}
