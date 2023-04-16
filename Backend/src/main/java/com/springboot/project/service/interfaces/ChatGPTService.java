package com.springboot.project.service.interfaces;

import java.io.IOException;

public interface ChatGPTService {


    String getResponse(String input) throws IOException;
}
