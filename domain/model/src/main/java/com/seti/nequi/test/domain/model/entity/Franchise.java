package com.seti.nequi.test.domain.model.entity;

import lombok.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Franchise {
    private String id;
    private String name;
    private List<Branch> branches;
}
