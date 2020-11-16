package com.yummy.recipe.services;


import com.yummy.recipe.models.UnitOfMeasure;
import com.yummy.recipe.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public UnitOfMeasureService(UnitOfMeasureRepository unitOfMeasureRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    public Set<UnitOfMeasure> ListAllUnitOfMeasures(){
        return unitOfMeasureRepository.findAll()
                .stream()
                .collect(Collectors.toSet());
    }
}
