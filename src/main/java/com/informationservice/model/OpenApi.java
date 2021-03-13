package com.informationservice.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.informationservice.utility.IdGenrationConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "TB_OPEN_API")
public class OpenApi {

    /**
     * primary key
     */
    @DynamoDBHashKey(attributeName = "id")
    @DynamoDBTypeConverted(converter = IdGenrationConverter.class)
    private String id;

    private Coord coord;

    @DynamoDBAttribute(attributeName = "weather")
    private List<Weather> weather;

    @DynamoDBAttribute(attributeName = "base")
    private String base;

    @DynamoDBAttribute(attributeName = "main")
    private Main main;

    /**
     * sort key
     */
    private Integer visibility;

    private Wind wind;

    private Cloud clouds;

    private double dt;

    private Sys sys;

    private Double timezone;

    /**
     * partition key="NAME" + sortKey = "Visibility"
     */
    @DynamoDBIndexHashKey(attributeName = "name", globalSecondaryIndexName = "name-visibility-index")
    private String name;

    private Double cod;
}
