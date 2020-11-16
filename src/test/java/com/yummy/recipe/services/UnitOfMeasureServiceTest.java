package com.yummy.recipe.services;

import com.yummy.recipe.models.UnitOfMeasure;
import com.yummy.recipe.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnitOfMeasureServiceTest {

    @InjectMocks
    UnitOfMeasureService unitOfMeasureService;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        unitOfMeasureService=new UnitOfMeasureService(unitOfMeasureRepository);
    }

    @Test
    void listAllUnitOfMeasures() {
        UnitOfMeasure unitOfMeasure1= new UnitOfMeasure();
        unitOfMeasure1.setId(3L);
        UnitOfMeasure unitOfMeasure2= new UnitOfMeasure();
        unitOfMeasure2.setId(4L);
        UnitOfMeasure unitOfMeasure3= new UnitOfMeasure();
        unitOfMeasure3.setId(5L);
        List<UnitOfMeasure> unitOfMeasures= new ArrayList<>();
        unitOfMeasures.add(unitOfMeasure1);
        unitOfMeasures.add(unitOfMeasure2);
        unitOfMeasures.add(unitOfMeasure3);

        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);


        assertEquals(unitOfMeasures.size(),unitOfMeasureService.ListAllUnitOfMeasures().size() );
        verify(unitOfMeasureRepository, times(1)).findAll();
    }
}