package com.springboot.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class FileUploadDTO {

    public Integer difficultyLevel;
    public String name;
    public String format;
    public String author;
    public List<BiboReferencesDTO> cites;
    public List<String> obtainedBy;
    public List<String> teachesCourses;

}
