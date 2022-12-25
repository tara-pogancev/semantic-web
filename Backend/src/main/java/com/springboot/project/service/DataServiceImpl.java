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

    private static final String DATA_FILE = "src/main/resources/data.rdf";
    private static final String ACM_INDIVIDUALS_FEED_FILE = "sec_ontology_individuals.xlsx";
    private static final String ACM_URI_PREFIX = "http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#";
    private static final String BIBO_URI_PREFIX = "http://purl.org/ontology/bibo/";

    public static final String OK_UPLOAD_FILE = "Successfully written into RDF file";
    public static final String OK_GENERATE_RDF = "Successfully generated starter RDF file";

    private final OntologyService ontologyService;

    @Override
    public String uploadFile(FileUploadDTO dto) throws IOException {
        OntModel om = ontologyService.getStarterModel();
        om.read(DATA_FILE);

        AcmOntologyModel acmOntology = ontologyService.getAcmOntologyModel(om);
        BiboOntologyModel biboOntology = ontologyService.getBiboOntologyModel(om);

        String id = ACM_URI_PREFIX + getUriId(dto.name);
        Individual fileIndividual = acmOntology.learningResource.createIndividual(id);
        fileIndividual.addLiteral(acmOntology.difficultyLevelProperty, (dto.difficultyLevel.toString()))
                .addLiteral(acmOntology.formatProperty, (dto.format))
                .addLiteral(acmOntology.nameProperty, (dto.name))
                .addLiteral(acmOntology.authorProperty, (dto.author));

        for (BiboReferencesDTO biboReference : dto.cites) {
            String biboId = BIBO_URI_PREFIX + getUriId(biboReference.content);
            Individual referenceIndividual = biboOntology.document.createIndividual(biboId);
            referenceIndividual.addLiteral(biboOntology.contentProprety, (biboReference.content))
                    .addLiteral(biboOntology.sectionProperty, (biboReference.section.toString()))
                    .addLiteral(biboOntology.numberProperty, (biboReference.number.toString()))
                    .addLiteral(biboOntology.getCitedBy(), id);
        }

        for (String teachesCourses : dto.teachesCourses) {
            Individual courseIndividual = om.getIndividual(generateAcmUri(teachesCourses));
            System.out.println(generateAcmUri(teachesCourses));
            if (courseIndividual != null) {
                courseIndividual.addProperty(acmOntology.isTaughtUsing, fileIndividual);
            }
        }

        for (String obtainedBy : dto.obtainedBy) {
            Individual learningOutcome = om.getIndividual(generateAcmUri(obtainedBy));
            System.out.println(generateAcmUri(obtainedBy));
            if (learningOutcome != null) {
                learningOutcome.addProperty(acmOntology.obtainedBy, fileIndividual);
            }
        }

        FileWriter out = new FileWriter(DATA_FILE, false);
        om.write(out, "RDF/XML");
        out.close();

        return OK_UPLOAD_FILE;
    }

    /***
     * Code source from:
     * https://github.com/sasa-boros/acm-curriculum-explorer/blob/master/metahand-server/src/main/resources/sec_ontology.owl
     * @throws IOException
     */
    @Override
    public String generateStarterRdf() throws IOException {
        OntModel om = ontologyService.getStarterModel();
        AcmOntologyModel acmOntology = ontologyService.getAcmOntologyModel(om);

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

            Individual individual = acmOntology.knowledgeArea.createIndividual(ACM_URI_PREFIX + id);
            individual.addLiteral(acmOntology.nameProperty, om.createTypedLiteral(name));
            individual.addLiteral(acmOntology.estimatedContactHoursProperty, om.createTypedLiteral(estimatedContactHours));
            knowledgeAreas.put(id, individual);
        }

        // KNOWLEDGE UNITS
        Iterator<Row> knowledgeUnitIterator = workbook.getSheet("KnowledgeUnits").rowIterator();
        Map<String, Individual> knowledgeUnits = new HashMap<>();
        while (knowledgeUnitIterator.hasNext()) {
            Row currentRow = knowledgeUnitIterator.next();

            String id = currentRow.getCell(0).getStringCellValue();
            String name = currentRow.getCell(1).getStringCellValue();
            String knowledgeAreaId = currentRow.getCell(2).getStringCellValue();

            Individual individual = acmOntology.knowledgeUnit.createIndividual(ACM_URI_PREFIX + id);
            individual.addLiteral(acmOntology.nameProperty, om.createTypedLiteral(name));

            Individual knowledgeAreaIndividual = knowledgeAreas.get(knowledgeAreaId);
            knowledgeAreaIndividual.addProperty(acmOntology.consistsOf, individual);

            knowledgeUnits.put(id, individual);
        }

        // LEARNING OUTCOMES
        Iterator<Row> learningOutcomeIterator = workbook.getSheet("LearningOutcomes").rowIterator();
        Map<String, Individual> learningOutcomes = new HashMap<>();
        while (learningOutcomeIterator.hasNext()) {
            Row currentRow = learningOutcomeIterator.next();

            String id = currentRow.getCell(0).getStringCellValue();
            String description = currentRow.getCell(1).getStringCellValue();
            String knowledgeUnitId = currentRow.getCell(2).getStringCellValue();

            Individual individual = acmOntology.learningOutcome.createIndividual(ACM_URI_PREFIX + id);
            individual.addLiteral(acmOntology.descriptionProperty, om.createTypedLiteral(description));

            Individual knowledgeUnitIndividual = knowledgeUnits.get(knowledgeUnitId);
            knowledgeUnitIndividual.addProperty(acmOntology.includes, individual);

            learningOutcomes.put(id, individual);
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

            Individual individual = acmOntology.course.createIndividual(ACM_URI_PREFIX + id);
            individual.addLiteral(acmOntology.nameProperty, om.createTypedLiteral(name));
            individual.addLiteral(acmOntology.difficultyLevelProperty, om.createTypedLiteral(difficultyLevel));
            individual.addLiteral(acmOntology.levelOfStudyProperty, om.createTypedLiteral(levelOfStudy));
            individual.addLiteral(acmOntology.teacherProperty, om.createTypedLiteral(teacher));

            for (String loId : learningOutcomeIds) {
                Individual learningOutcomeIndividual = learningOutcomes.get(loId);
                individual.addProperty(acmOntology.teaches, learningOutcomeIndividual);
            }

            courses.put(id, individual);
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
            String name = currentRow.getCell(6).getStringCellValue();

            Individual individual = acmOntology.learningResource.createIndividual(ACM_URI_PREFIX + id);
            individual.addLiteral(acmOntology.authorProperty, om.createTypedLiteral(author));
            individual.addLiteral(acmOntology.difficultyLevelProperty, om.createTypedLiteral(difficultyLevel));
            individual.addLiteral(acmOntology.formatProperty, om.createTypedLiteral(format));
            individual.addLiteral(acmOntology.nameProperty, om.createTypedLiteral(name));

            for (String cId : coursesIds) {
                Individual courseIndividual = courses.get(cId);
                courseIndividual.addProperty(acmOntology.isTaughtUsing, individual);
            }

            for (String loId : learningOutcomeIds) {
                Individual learningOutcomeIndividual = learningOutcomes.get(loId);
                learningOutcomeIndividual.addProperty(acmOntology.obtainedBy, individual);
            }

            learningResources.put(id, individual);
        }

        FileWriter out = new FileWriter(DATA_FILE, false);
        om.write(out, "RDF/XML");
        out.close();

        return OK_GENERATE_RDF;
    }

    @Override
    public String getUriId(String name) {
        return name.toUpperCase().replace(" ", "-");
    }

    @Override
    public String generateAcmUri(String name) {
        return ACM_URI_PREFIX + getUriId(name);
    }

}
