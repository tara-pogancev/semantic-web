package com.springboot.project.service.interfaces;

import java.io.IOException;

public interface DataService {

    String generateRdfFile() throws IOException;

    String getUriFromName(String name);

}
