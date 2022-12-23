package bookstore.productservice.core.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Getter private UUID id;

    @Column(nullable = false)
    @Getter private String isbn13;

    @Column(nullable = false)
    @Getter private String title;

    @Column(nullable = false)
    @Getter private String version;

    @Column(nullable = false)
    @Getter private String[] authors;

    @Column(nullable = false)
    @Getter private Date publishingDate;

    @Column(nullable = false)
    @Getter private String publishingHouse;

    @Column(nullable = false)
    @Getter private String description;

    @Column(nullable = false)
    @Getter private String language;

    @Column(nullable = false)
    @Getter private int pages;

    @Column(nullable = true)
    @Getter private String coverUrl;

    @Column(nullable = false)
    @Getter @Setter private float price;


}

