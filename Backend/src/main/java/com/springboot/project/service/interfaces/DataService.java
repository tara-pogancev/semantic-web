package com.springboot.project.service.interfaces;

import com.springboot.project.dto.FileUploadDTO;

import java.io.IOException;

public interface DataService {

    String uploadFile(FileUploadDTO dto) throws IOException;

    String generateStarterRdf() throws IOException;

    String getUriLearningResource(String name);

    String getUriBiboDocument(String name);

}
