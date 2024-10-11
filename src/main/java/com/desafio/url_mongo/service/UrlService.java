package com.desafio.url_mongo.service;

import com.desafio.url_mongo.dto.OriginalUrlDTO;
import com.desafio.url_mongo.dto.ShortenUrlDTO;
import com.desafio.url_mongo.dto.ShortenUrlResponse;
import com.desafio.url_mongo.entities.UrlEntity;
import com.desafio.url_mongo.repositories.UrlRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UrlService {

    @Autowired
    UrlRepository urlRepository;

    @Autowired
    UrlValidateService validateUrlService;

    public ShortenUrlResponse shortenUrl(ShortenUrlDTO request, HttpServletRequest servletRequest) throws MalformedURLException {
        if (request == null) {
            throw new InputMismatchException("Por favor, insira uma URL");
        }

        boolean validatedUrl = validateUrlService.isValid(request.url());

        String newOriginalUrl = validateUrlService.getUrlValidate();

        if (!validatedUrl) {
            throw new MalformedURLException("Por favor, informe uma URL válida");
        }


        String id;
        do {
            id = RandomStringUtils.randomAlphanumeric(5,10);
        } while (urlRepository.existsById(id));



        String shortenUrl = servletRequest.getRequestURL().toString().replace("shorten-url", id);

        urlRepository.save(new UrlEntity(id, newOriginalUrl, shortenUrl, LocalDateTime.now().plusMinutes(10)));


        return new ShortenUrlResponse(shortenUrl);
    }

    public HttpHeaders redirect(String id) {

        UrlEntity urlEntity = urlRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Esta url não existe ou expirou")
        );

        HttpHeaders headers = new HttpHeaders();

        headers.setLocation(URI.create(urlEntity.getOriginalUrl()));

        return headers;
    }

    public List<UrlEntity> getAll(){
        List<UrlEntity> allUrlEntity = urlRepository.findAll();

        if (allUrlEntity.isEmpty()) {
            throw new NoSuchElementException("Não há nenhuma url encurtada no banco de dados.");
        }
        return allUrlEntity;
    }

    public OriginalUrlDTO getOriginalUrlById(String id) {


        if (id.isEmpty()) {
            throw new IllegalArgumentException("Por favor, informe o Id");
        }

        OriginalUrlDTO originalUrl = new OriginalUrlDTO();
        UrlEntity getUrlEntity = urlRepository.getOriginalUrlById(id);

        originalUrl.setOriginalUrl(getUrlEntity.getOriginalUrl());

        return originalUrl;
    }

    public void deleteAllShortenUrl() {
        List<UrlEntity> allUrlEntity = urlRepository.findAll();
        if (allUrlEntity.isEmpty()) {
            throw new NoSuchElementException("Não há nenhuma url encurtada salva no banco de dados");
        }
        urlRepository.deleteAll(allUrlEntity);
    }



}
