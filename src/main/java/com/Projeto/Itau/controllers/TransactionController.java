package com.Projeto.Itau.controllers;

import com.Projeto.Itau.dto.TransactionDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.OffsetDateTime;

@RestController
@Validated
@RequestMapping("/transacao")
public class TransactionController {

    @PostMapping
    public ResponseEntity<HttpStatus> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO){
        Duration duration = Duration.between(OffsetDateTime.now(), transactionDTO.getDataHora());

        if (transactionDTO.getValor() < 0) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }

        if (duration.isPositive()){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
