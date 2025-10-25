package com.seti.nequi.test.infrastructure.drivenadapters.mongo.document;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductDocument {
    private String id;
    private String name;
    private int count;
}
