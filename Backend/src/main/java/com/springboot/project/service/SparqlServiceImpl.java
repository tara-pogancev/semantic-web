package com.springboot.project.service;

import com.springboot.project.dto.RequestDTO;
import com.springboot.project.service.interfaces.OntologyService;
import com.springboot.project.service.interfaces.SparqlService;
import lombok.RequiredArgsConstructor;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SparqlServiceImpl implements SparqlService {

    private static final String DATA_FILE = "src/main/resources/data.rdf";

    private final OntologyService ontologyService;

    @Override
    public String getResourceForCourse(RequestDTO dto) {
        OntModel om = ontologyService.getStarterModel();
        om.read(DATA_FILE);

        String queryString =
                "PREFIX acm: <http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#>\n" +
                        "\n" +
                        "SELECT ?name\n" +
                        "WHERE {\n" +
                        "    ?course acm:name \"" + dto.course + "\" .\n" +
                        "    ?course acm:isTaughtUsing ?lr .\n" +
                        "    ?lr acm:name ?name" +
                        "}";

        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, om);
        ResultSet results = qe.execSelect();

        String retVal = "Resources for course: " + dto.course + "\n\n";
        retVal += ResultSetFormatter.asText(results);
        qe.close();
        return retVal;
    }

    @Override
    public String getResourceForKnowledgeUnit(RequestDTO dto) {
        OntModel om = ontologyService.getStarterModel();
        om.read(DATA_FILE);

        String queryString =
                "PREFIX acm: <http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#>\n" +
                        "\n" +
                        "SELECT ?name\n" +
                        "WHERE {\n" +
                        "    ?unit acm:name \"" + dto.knowledgeUnit + "\" .\n" +
                        "    ?unit acm:includes ?lo .\n" +
                        "    ?lo acm:obtainedBy ?lr . \n" +
                        "    ?lr acm:name ?name" +
                        "}";

        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, om);
        ResultSet results = qe.execSelect();

        String retVal = "Resources for course: " + dto.course + "\n\n";
        retVal += ResultSetFormatter.asText(results);
        qe.close();
        return retVal;
    }

    @Override
    public String getCoursesWithResourcesByAuthor(RequestDTO dto) {
        OntModel om = ontologyService.getStarterModel();
        om.read(DATA_FILE);

        String queryString =
                "PREFIX acm: <http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#>\n" +
                        "\n" +
                        "SELECT ?name\n" +
                        "WHERE {\n" +
                        "    ?lr acm:author \"" + dto.author + "\" .\n" +
                        "    ?course acm:isTaughtUsing ?lr .\n" +
                        "    ?course acm:name ?name" +
                        "}";

        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, om);
        ResultSet results = qe.execSelect();

        String retVal = "Courses with resources written by:" + dto.course + "\n\n";
        retVal += ResultSetFormatter.asText(results);
        qe.close();
        return retVal;
    }

}
