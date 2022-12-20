package com.springboot.project.service.interfaces;

import com.springboot.project.dto.FileUploadDTO;

import java.io.IOException;

public interface DataService {

    String uploadFile(FileUploadDTO dto) throws IOException;

    String generateStarterRdf() throws IOException;

    String getUriId(String name);

    String generateAcmUri(String name);

}
