package com.sedientos.restapi.configuration;

import com.sedientos.restapi.model.String2BoxConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;

import javax.annotation.Resource;

@Configuration
public class ConvertersConfiguration {

    @Resource(name = "defaultConversionService")
    private GenericConversionService genericConversionService;

    @Bean
    public String2BoxConverter string2BoxConverter(){
        String2BoxConverter string2BoxConverter = new String2BoxConverter();
        genericConversionService.addConverter(string2BoxConverter);
        return string2BoxConverter;
    }

}
