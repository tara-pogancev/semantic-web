package com.springboot.project.service;

import com.springboot.project.service.interfaces.ChatGPTService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ChatGPTServiceImpl implements ChatGPTService {

    @Autowired
    private Environment env;

    @Override
    public String getResponse(String input) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api.openai.com/v1/completions");
        httpPost.setHeader("Authorization", "Bearer " + env.getProperty("API_KEY"));
        httpPost.setHeader("Content-Type", "application/json");

        String promptText = "I have a Semantic web project and it uses a custom ACM Curriculum Ontology. Its entities are: KnlowledgeArea (described with estimatedContactHours and name). KnowledgeArea consists of KnowledgeUnit which has a property name. KnowledgeUnit includes LearningOutcome, which has a property description. LearningOutcome is obtainedBy a LearningResource which has difficultyLevel, format, name and author. A Course isTaughtUsing LearningResource. A course has leveOfStudy, difficultyLevel, name and teahcer. Course teaches one or more LearningOutcome.  The namespace prefix is acm:, and the namespace url is: http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#. If asked to connect the Course with the learning Outcome it teaches, please use the 'teaches' connector between them." +
                "I will give you some example queries for this Ontology." +
                "Example 1. What are learning resources of the course Semantic Web? Solution:" +
                "PREFIX acm: <http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#>" +
                "SELECT ?name" +
                "WHERE {" +
                "?course acm:name 'Semantic Web' ." +
                "?course acm:isTaughtUsing ?lr ." +
                "?lr acm:name ?name" +
                "}" +
                "Example 2. What are the learning resources for knowledge unit Basic Analysis? Solution:" +
                "PREFIX acm: <http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#>" +
                "SELECT ?name" +
                "WHERE {" +
                "?unit acm:name 'Basic Analysis' ." +
                "?unit acm:includes ?lo ." +
                "?lo acm:obtainedBy ?lr . " +
                "?lr acm:name ?name" +
                "}" +
                "Example 3. What are the learning resources made by the author Jelena Jelic?" +
                "PREFIX acm: <http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#>" +
                "SELECT ?name" +
                "WHERE {" +
                "?lr acm:author 'Jelena Jelic' ." +
                "?course acm:isTaughtUsing ?lr ." +
                "?course acm:name ?name" +
                "}" +
                "Example 4. What are the courses and authors for learning resources for the learning outcome 'Describe security concerns in designing applications for use over wireless networks'?" +
                "PREFIX acm: <http://www.semanticweb.org/sasaboros/ontologies/2020/11/sec_ontology#>" +
                " SELECT ?name" +
                " WHERE {" +
                "?course acm:name 'Describe security concerns in designing applications for use over wireless networks' ." +
                "?course acm:isTaughtUsing ?lr ." +
                "?lr acm:author ?name" +
                "}" +
                "I will provide you with a question, and your job is to provide me with the SPARQL query for getting said data in described ontology. Do not include any properties the user does not mention himself." +
                "The question is: " + input;

        String requestBody = "{\"prompt\": \"" + promptText + "\",\"max_tokens\": 200,\"temperature\": 0.7, \"model\": \"text-davinci-003\"}";

        StringEntity entity = new StringEntity(requestBody);
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        String result = "";
        try {
            HttpEntity responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity);
        } finally {
            response.close();
        }

        return result;
    }
}
