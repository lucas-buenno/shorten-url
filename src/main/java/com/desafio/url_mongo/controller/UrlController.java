package com.desafio.url_mongo.controller;

import com.desafio.url_mongo.dto.OriginalUrlDTO;
import com.desafio.url_mongo.dto.ShortenUrlDTO;
import com.desafio.url_mongo.dto.ShortenUrlResponse;
import com.desafio.url_mongo.entities.UrlEntity;
import com.desafio.url_mongo.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.charset.MalformedInputException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping(value = "/shorten-url")
    ResponseEntity<?> shortenUrl(@RequestBody ShortenUrlDTO request,
                                    HttpServletRequest servletRequest) {

        try {
            ShortenUrlResponse response = urlService.shortenUrl(request, servletRequest);
            return ResponseEntity.ok(response);

        } catch (InputMismatchException | MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> redirect(@PathVariable("id") String id) {

        try {
            HttpHeaders headers = urlService.redirect(id);
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/urls")
    public ResponseEntity<?> getAllUrl() {

        try {
            List<UrlEntity> allUrlEntity = urlService.getAll();
            return ResponseEntity.status(HttpStatus.FOUND).body(allUrlEntity);
        } catch (IllegalArgumentException | NoSuchElementException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/url/{id}")
    public ResponseEntity<?> getUrlById(@PathVariable OriginalUrlDTO id) {

        try {
            OriginalUrlDTO getUrl = urlService.getOriginalUrlById(id.getOriginalUrl());
            return ResponseEntity.status(HttpStatus.FOUND).body(getUrl);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteAllUrl() {
        try{
            urlService.deleteAllShortenUrl();
           return ResponseEntity.status(HttpStatus.OK).body("Todas Urls foram deletadas");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
