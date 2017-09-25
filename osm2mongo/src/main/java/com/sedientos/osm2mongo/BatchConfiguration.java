package com.sedientos.osm2mongo;

import com.sedientos.data.model.Place;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.io.IOException;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<NodeDTO> reader() throws IOException {
        StaxEventItemReader<NodeDTO> xmlFileReader = new StaxEventItemReader<>();
        xmlFileReader.setResource(new ClassPathResource("data.xml"));
        xmlFileReader.setFragmentRootElementName("node");

        Jaxb2Marshaller nodeMarshaller = new Jaxb2Marshaller();
        nodeMarshaller.setClassesToBeBound(NodeDTO.class);
        xmlFileReader.setUnmarshaller(nodeMarshaller);

        return xmlFileReader;
    }

    @Autowired
    public MongoTemplate mongoTemplate;
    // tag::readerwriterprocessor[]

    @Bean
    public PlaceItemProcessor processor() {
        return new PlaceItemProcessor();
    }

    @Bean
    public MongoItemWriter<Place> writer() {
        MongoItemWriter<Place> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob() throws IOException {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() throws IOException {
        return stepBuilderFactory.get("step1")
                .<NodeDTO, Place> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    // end::jobstep[]
}
