package dev.lumme.reactivedemo.backend.datagenerator;

import com.github.javafaker.Faker;
import dev.lumme.reactivedemo.backend.entity.City;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DataGenerator {

    private final EntityManager em;
    private final TransactionTemplate transactionTemplate;
    private final Faker faker = new Faker();

    public DataGenerator(EntityManager entityManager, PlatformTransactionManager platformTransactionManager) {
        this.em = entityManager;
        transactionTemplate = new TransactionTemplate(platformTransactionManager);
    }

    @PostConstruct
    private void init() {
        transactionTemplate.executeWithoutResult(status -> generateCities().forEach(em::persist));
    }

    private List<City> generateCities() {
        return IntStream.range(0, 50)
                .mapToObj(i -> new City(faker.address().city()))
                .collect(Collectors.toList());
    }
}
