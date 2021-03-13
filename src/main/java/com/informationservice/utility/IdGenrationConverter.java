package com.informationservice.utility;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.UUID;

import static com.informationservice.utility.StringDataUtils.OPEN_API_INIT;

public class IdGenrationConverter implements DynamoDBTypeConverter<String, String> {

    @Override
    public String convert(String input) {
        return OPEN_API_INIT.concat(UUID.randomUUID().toString());

    }

    @Override
    public String unconvert(String input) {
        return input.replaceFirst(OPEN_API_INIT, "");
    }
}
