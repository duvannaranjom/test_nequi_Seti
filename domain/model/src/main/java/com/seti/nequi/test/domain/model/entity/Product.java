package com.seti.nequi.test.domain.model.entity;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Product {
    private String id;
    private String name;
    private int count;
}
