package com.Projeto.Itau.controllers;

import com.Projeto.Itau.dto.TransactionDTO;
import com.Projeto.Itau.entities.Transaction;
import com.Projeto.Itau.mapper.TransactionMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

@RestController
@Validated
@RequestMapping("/")
public class TransactionController {

    List<Transaction> transactionList = new LinkedList<>();

    @PostMapping("/transacao")
    public ResponseEntity<HttpStatus> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        Duration duration = Duration.between(OffsetDateTime.now(), transactionDTO.getDataHora());

        if (transactionDTO.getValor() < 0) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }

        if (duration.isPositive()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }

        transactionList.add(TransactionMapper.TransactionDtoToTransaction(transactionDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @DeleteMapping("/transacao")
    public ResponseEntity<HttpStatus> deleteAllTransactions() {
        transactionList.clear();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
