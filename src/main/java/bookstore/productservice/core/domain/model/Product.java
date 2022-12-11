package bookstore.productservice.core.domain.model;

import java.util.Date;
import java.util.UUID;

public class Product {

    private UUID id;
    private String isbn10;
    private String isbn13;
    private String title;
    private String version;
    private String[] authors;
    private Date publishingDate;
    private String publishingHouse;
    private String description;
    private String language;
    private int pages;
    private String coverUrl;
    private float price;

}

