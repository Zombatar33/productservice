package bookstore.productservice.core.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

public class Product {

    @Getter private UUID id;
    @Getter private final String isbn10;
    @Getter private final String isbn13;
    @Getter private final String title;
    @Getter private final String version;
    @Getter private final String[] authors;
    @Getter private final Date publishingDate;
    @Getter private final String publishingHouse;
    @Getter private final String description;
    @Getter private final String language;
    @Getter private final int pages;
    //@Getter private String coverUrl;
    @Getter @Setter private float price;


    public Product(String isbn10, String isbn13, String title, String version, String[] authors, Date publishingDate,
                   String publishingHouse, String description, String language, int pages, float price) {
        //TODO: UUID auto gen
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
        this.title = title;
        this.version = version;
        this.authors = authors;
        this.publishingDate = publishingDate;
        this.publishingHouse = publishingHouse;
        this.description = description;
        this.language = language;
        this.pages = pages;
        this.price = price;
    }



}

