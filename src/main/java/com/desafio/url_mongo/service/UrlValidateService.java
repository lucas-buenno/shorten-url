package com.desafio.url_mongo.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Service
public class UrlValidateService {


    private String urlValidate;


    public boolean isValid(String url) throws MalformedURLException {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            urlValidate = "http://" + url;
        }
        try {
            URL urlObject = new URL(urlValidate);
            String host = urlObject.getHost();
            if (!host.matches(".*\\.(com|com\\.br|io|org)$")) {
                return false;
            }
            new URI(urlValidate);
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
