package com.Projeto.Itau.controllers;

import com.Projeto.Itau.dto.TransactionDTO;
import com.Projeto.Itau.entities.Transaction;
import com.Projeto.Itau.mapper.TransactionMapper;
import com.Projeto.Itau.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final List<Transaction> transactionList = new LinkedList<>();

    private int timeToTransactionStatistics = 60;

    @PostMapping("/transacao")
    public ResponseEntity<HttpStatus> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        log.info("Recebendo transação");

        transactionService.processTransaction(transactionDTO);
        var transaction = TransactionMapper.transactionDtoToTransaction(transactionDTO);

        transactionList.add(transaction);

        log.info("Salvando transação");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/tempo-estatistica")
    public ResponseEntity<HttpStatus> changeStatisticLastXSeconds(@RequestBody int seconds) {
        transactionService.isSecondsDefinedValid(seconds);

        timeToTransactionStatistics = seconds;

        log.info("Tempo para cálculo de estatísticas alterado para '{}' segundos", seconds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/transacao")
    public ResponseEntity<HttpStatus> deleteAllTransactions() {
        transactionService.deleteAllTransactions(transactionList);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/estatistica")
    public ResponseEntity<DoubleSummaryStatistics> getLastXSecondsTransactionStatistics() {
        log.info("Calculando as estatísticas das transações dos ultimos 60 segundos");

        OffsetDateTime start = OffsetDateTime.now();
        var statistics = transactionService.getStatistics(transactionList, start, timeToTransactionStatistics);

        var secondsTakenStatistics = transactionService.calculateDurationInNanos(start);
        transactionService.logStatistics(statistics, secondsTakenStatistics);


        return ResponseEntity.ok().body(statistics);
    }
}

