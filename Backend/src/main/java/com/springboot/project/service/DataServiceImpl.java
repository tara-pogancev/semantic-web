package com.springboot.project.service;

import com.springboot.project.dto.BiboReferencesDTO;
import com.springboot.project.dto.FileUploadDTO;
import com.springboot.project.model.AcmOntologyModel;
import com.springboot.project.model.BiboOntologyModel;
import com.springboot.project.service.interfaces.DataService;
import com.springboot.project.service.interfaces.OntologyService;
import lombok.RequiredArgsConstructor;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private static final String DATA_FILE = ".\\src\\main\\resources\\data.rdf";
    private static final String ACM_INDIVIDUALS_FEED_FILE = "sec_ontology_individuals.xlsx";
    private static final String ACM_URI_PREFIX = "http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#";
    private static final String BIBO_URI_PREFIX = "http://purl.org/ontology/bibo#";

    public static final String OK_UPLOAD_FILE = "Successfully written into RDF file";
    public static final String OK_GENERATE_RDF = "Successfully generated starter RDF file";

    private final OntologyService ontologyService;

    @Override
    public String uploadFile(FileUploadDTO dto) throws IOException {
        AcmOntologyModel acmOntologyModel = ontologyService.getAcmOntologyModel();
        BiboOntologyModel biboOntologyModel = ontologyService.getBiboOntologyModel();

        // TODO: Uraditi preko individuala i ponovo upisati u fajl

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

        return OK_UPLOAD_FILE;
    }

    @Override
    public String generateStarterRdf() throws IOException {
        OntModel om = ontologyService.getAcmOntModel();
        AcmOntologyModel model = ontologyService.getAcmOntologyModel();

        FileInputStream excelFile = new FileInputStream(new ClassPathResource(ACM_INDIVIDUALS_FEED_FILE).getFile());
        Workbook workbook = new XSSFWorkbook(excelFile);

        // KNOWLEDGE AREAS
        Iterator<Row> knowledgeAreaIterator = workbook.getSheet("KnowledgeAreas").rowIterator();
        Map<String, Individual> knowledgeAreas = new HashMap<>();
        while (knowledgeAreaIterator.hasNext()) {
            Row currentRow = knowledgeAreaIterator.next();

            String id = currentRow.getCell(0).getStringCellValue();
            String name = currentRow.getCell(1).getStringCellValue();
            int estimatedContactHours = (int) currentRow.getCell(2).getNumericCellValue();

            Individual individual = model.knowledgeArea.createIndividual(ACM_URI_PREFIX + id);
            individual.addLiteral(model.nameProperty,  om.createTypedLiteral(name));
            individual.addLiteral(model.estimatedContactHoursProperty,  om.createTypedLiteral(estimatedContactHours));
            knowledgeAreas.put(id, individual);
            om.createIndividual(individual);
        }

        // KNOWLEDGE UNITS
        Iterator<Row> knowledgeUnitIterator = workbook.getSheet("KnowledgeUnits").rowIterator();
        Map<String, Individual> knowledgeUnits = new HashMap<>();
        while (knowledgeUnitIterator.hasNext()) {
            Row currentRow = knowledgeUnitIterator.next();

            String id = currentRow.getCell(0).getStringCellValue();
            String name = currentRow.getCell(1).getStringCellValue();
            String knowledgeAreaId = currentRow.getCell(2).getStringCellValue();

            Individual individual = model.knowledgeUnit.createIndividual(ACM_URI_PREFIX + id);
            individual.addLiteral(model.nameProperty,  om.createTypedLiteral(name));

            Individual knowledgeAreaIndividual = knowledgeAreas.get(knowledgeAreaId);
            knowledgeAreaIndividual.addProperty(model.consistsOf, individual);

            knowledgeUnits.put(id, individual);
            om.createIndividual(individual);
        }

        // LEARNING OUTCOMES
        Iterator<Row> learningOutcomeIterator = workbook.getSheet("LearningOutcomes").rowIterator();
        Map<String, Individual> learningOutcomes = new HashMap<>();
        while (learningOutcomeIterator.hasNext()) {
            Row currentRow = learningOutcomeIterator.next();

            String id = currentRow.getCell(0).getStringCellValue();
            String description = currentRow.getCell(1).getStringCellValue();
            String knowledgeUnitId = currentRow.getCell(2).getStringCellValue();

            Individual individual = model.learningOutcome.createIndividual(ACM_URI_PREFIX + id);
            individual.addLiteral(model.descriptionProperty,  om.createTypedLiteral(description));

            Individual knowledgeUnitIndividual = knowledgeUnits.get(knowledgeUnitId);
            knowledgeUnitIndividual.addProperty(model.includes, individual);

            learningOutcomes.put(id, individual);
            om.createIndividual(individual);
        }

        // COURSES
        Iterator<Row> courseIterator = workbook.getSheet("Courses").rowIterator();
        Map<String, Individual> courses = new HashMap<>();
        while (courseIterator.hasNext()) {
            Row currentRow = courseIterator.next();

            String id = currentRow.getCell(0).getStringCellValue();
            String name = currentRow.getCell(1).getStringCellValue();
            String[] learningOutcomeIds = currentRow.getCell(2).getStringCellValue().split(";");
            int difficultyLevel = (int) currentRow.getCell(3).getNumericCellValue();
            int levelOfStudy = (int) currentRow.getCell(4).getNumericCellValue();
            String teacher = currentRow.getCell(5).getStringCellValue();

            Individual individual = model.course.createIndividual(ACM_URI_PREFIX + id);
            individual.addLiteral(model.nameProperty,  om.createTypedLiteral(name));
            individual.addLiteral(model.difficultyLevelProperty,  om.createTypedLiteral(difficultyLevel));
            individual.addLiteral(model.levelOfStudyProperty,  om.createTypedLiteral(levelOfStudy));
            individual.addLiteral(model.teacherProperty,  om.createTypedLiteral(teacher));

            for (String loId : learningOutcomeIds) {
                Individual learningOutcomeIndividual = learningOutcomes.get(loId);
                individual.addProperty(model.teaches, learningOutcomeIndividual);
            }

            courses.put(id, individual);
            om.createIndividual(individual);
        }

        // LEARNING RESOURCES
        Iterator<Row> learningResourceIterator = workbook.getSheet("LearningResources").rowIterator();
        Map<String, Individual> learningResources = new HashMap<>();
        while (learningResourceIterator.hasNext()) {
            Row currentRow = learningResourceIterator.next();

            String id = currentRow.getCell(0).getStringCellValue();
            String author = currentRow.getCell(1).getStringCellValue();
            String[] coursesIds = currentRow.getCell(2).getStringCellValue().split(";");
            String[] learningOutcomeIds = currentRow.getCell(3).getStringCellValue().split(";");
            int difficultyLevel = (int) currentRow.getCell(4).getNumericCellValue();
            String format = currentRow.getCell(5).getStringCellValue();

            Individual individual = model.learningResource.createIndividual(ACM_URI_PREFIX + id);
            individual.addLiteral(model.authorProperty,  om.createTypedLiteral(author));
            individual.addLiteral(model.difficultyLevelProperty,  om.createTypedLiteral(difficultyLevel));
            individual.addLiteral(model.formatProperty,  om.createTypedLiteral(format));

            for (String cId : coursesIds) {
                Individual courseIndividual = courses.get(cId);
                courseIndividual.addProperty(model.isTaughtUsing, individual);
            }

            for (String loId : learningOutcomeIds) {
                Individual learningOutcomeIndividual = learningOutcomes.get(loId);
                learningOutcomeIndividual.addProperty(model.obtainedBy, individual);
            }

            learningResources.put(id, individual);
            om.createIndividual(individual);
        }

        FileWriter out = new FileWriter(DATA_FILE, false);
        om.write(out, "RDF/XML");
        out.write("\n\n");
        out.close();

        return OK_GENERATE_RDF;
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
