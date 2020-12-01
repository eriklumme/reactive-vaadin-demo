package dev.lumme.reactivedemo.backend.service;

import dev.lumme.reactivedemo.backend.entity.City;
import dev.lumme.reactivedemo.common.dto.CityDTO;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<CityDTO> findCities() {
        return entityManager.createQuery("SELECT c FROM City c", City.class)
                .getResultList()
                .stream()
                .map(city -> new CityDTO(city.getName()))
                .collect(Collectors.toList());
    }
}
