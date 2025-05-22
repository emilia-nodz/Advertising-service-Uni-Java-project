package com.demo.models;

import jakarta.persistence.*;
import java.util.UUID;

@MappedSuperclass
public class AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Transient
    private String uid = UUID.randomUUID().toString();

    public AbstractModel() {}

    public AbstractModel(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }
}
