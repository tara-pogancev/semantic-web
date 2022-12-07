package com.springboot.project.service;

import com.springboot.project.dto.FileUploadDTO;
import com.springboot.project.model.AcmOntologyModel;
import com.springboot.project.service.interfaces.DataService;
import com.springboot.project.service.interfaces.OntologyService;
import lombok.RequiredArgsConstructor;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private static final String ACM_ONTOLOGY = "sec_ontology.owl";
    private static final String BIBO_ONTOLOGY = "bibo.owl";
    private static final String DATA_FILE = ".\\src\\main\\resources\\data.rdf";
    private static final String ACM_URI_PREFIX = "http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#";

    private final OntologyService ontologyService;

    @Override
    public String uploadFile(FileUploadDTO dto) throws IOException {
        AcmOntologyModel ontologyModel = ontologyService.getAcmOntologyModel();

        Model model = ModelFactory.createDefaultModel();
        Resource node = model.createResource(getUriFromName(dto.name))
                .addProperty(ontologyModel.getDifficultyLevelProperty(), dto.difficultyLevel.toString())
                .addProperty(ontologyModel.getFormatProperty(), dto.format)
                .addProperty(ontologyModel.getNameProperty(), dto.name)
                .addProperty(ontologyModel.getAuthorProperty(), dto.author);


        FileWriter out = new FileWriter(DATA_FILE, true);
        model.write(out, "RDF/XML");
        out.write("\n\n");
        out.close();

        return "Successfully written into RDF file";
    }

    @Override
    public String getUriFromName(String name) {
        String prefix = ACM_URI_PREFIX.replace("#", "/");
        return prefix + name.toLowerCase().replace(" ", "+");
    }

}
