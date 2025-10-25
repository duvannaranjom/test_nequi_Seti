package com.seti.nequi.test.infrastructure.drivenadapters.mongo.document;

import lombok.*;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BranchDocument {
    private String id;
    private String name;
    private List<ProductDocument> products;
}
