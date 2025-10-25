package com.seti.nequi.test.domain.model.entity;

import lombok.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Branch {
    private String id;
    private String name;
    private List<Product> products;
}
