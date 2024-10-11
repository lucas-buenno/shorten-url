package com.desafio.url_mongo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "urls")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UrlEntity {

    @Id
    private String id;


    private String originalUrl;

    private String shortenUrl;

    @Indexed(expireAfterSeconds = 0)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy - HH:mm:ss")
    private LocalDateTime expiresAt;

    public UrlEntity(String id, String originalUrl, LocalDateTime expiresAt) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.expiresAt = expiresAt;
    }
}
