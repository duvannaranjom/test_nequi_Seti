package com.seti.nequi.test.infrastructure.entrypoints.web.mapper;

import com.seti.nequi.test.domain.model.entity.*;
import com.seti.nequi.test.infrastructure.entrypoints.web.dto.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DtoMapperTest {
    @Test
    void roundTrip() {
        ProductDto pd = new ProductDto("p1","Prod",7);
        BranchDto bd = new BranchDto("b1","Branch", List.of(pd));
        FranchiseDto fd = new FranchiseDto("f1","Fran", List.of(bd));

        Franchise domain = DtoMapper.toDomain(fd);
        FranchiseDto back = DtoMapper.toDto(domain);

        assertEquals("f1", back.id());
        assertEquals("Fran", back.name());
        assertEquals("b1", back.branches().get(0).id());
        assertEquals(7, back.branches().get(0).products().get(0).count());
    }
}
