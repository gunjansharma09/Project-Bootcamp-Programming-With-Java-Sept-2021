package com.bootcampproject.bootcamp_project.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryMetaDataFieldValuesId implements Serializable{

  private static final long serialVersionUID = 1l;

  private Long categoryMetaDataField;

  private Long category;
}
