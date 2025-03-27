package com.xcr.orange.oa.common.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * 自定义jack反序列化器，使得 null 变成 ""
 *
 * @author ZhangDong X7450
 * @description
 * @date 2023-01-11 17:06
 */
public class CustomStringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return jsonParser.getText();
    }

    @Override
    public String getNullValue() {
        return "";
    }

}