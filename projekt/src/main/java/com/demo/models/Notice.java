package com.demo.models;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import com.demo.models.User;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "NOTICE")
public class Notice {
    // Pola w tabeli
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate = new Date();

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    // jakaś cena jeżeli to produkt, stawka jeżeli ogłoszenie o prace
    @Column()
    private int amount;

    @ManyToOne(optional = false)
    private User author;

    @ManyToOne(optional = false)
    private Category category;

    // zmienna określająca czy ogłoszenie trafiło już do moderatora
    // domyślnie false
    @Column(nullable = false)
    private boolean wasModerated = false;

    // Podczas tworzenia updateDate to null
    @PrePersist
    protected void onCreate() {
        this.updateDate = null;
    }

    // Po aktualizacji wiersza data aktualizacji zmienia się
    @PreUpdate
    protected void onUpdate() {
        this.updateDate = new Date();
    }

    // Konstruktor domyślny
    public Notice() {}

    // Konstruktor z parametrami
    public Notice(String title, String description, int amount, User author, Category category) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.author = author;
        this.category = category;
        this.publishDate = new Date();
        this.wasModerated = false;
        this.updateDate = null;
    }

    // Gettery i settery
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublishDate() {
        return this.publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean getWasModerated() {
        return this.wasModerated;
    }

    public void setWasModerated(boolean wasModerated) {
        this.wasModerated = wasModerated;
    }
}
