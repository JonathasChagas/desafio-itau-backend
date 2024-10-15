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
import java.util.DoubleSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/")
public class TransactionController {

    private final List<Transaction> transactionList = new LinkedList<>();

    @PostMapping("/transacao")
    public ResponseEntity<HttpStatus> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        Duration duration = Duration.between(OffsetDateTime.now(), transactionDTO.getDataHora());

        if (transactionDTO.getValor() < 0) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }

        if (duration.isPositive()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }

        Transaction transaction = TransactionMapper.TransactionDtoToTransaction(transactionDTO);
        System.out.println(transaction);
        System.out.println(transactionDTO);
        transactionList.add(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @DeleteMapping("/transacao")
    public ResponseEntity<HttpStatus> deleteAllTransactions() {
        transactionList.clear();
        System.out.println("Deu CLEAR");
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping ("/estatistica")
    public ResponseEntity<DoubleSummaryStatistics> showAllLast60SecondsTransactionStatitics() {
        OffsetDateTime now = OffsetDateTime.now();

        DoubleSummaryStatistics statistics = transactionList
                .stream()
                .map(TransactionMapper::TransactionToTransactionDto)
                .filter(d -> TimeUnit.SECONDS.convert(Duration.between(d.getDataHora(), now)) <= TimeUnit.SECONDS.toSeconds(60))
                .collect(Collectors.summarizingDouble(TransactionDTO::getValor));

        return ResponseEntity.status(HttpStatus.OK).body(statistics);
    }
}
