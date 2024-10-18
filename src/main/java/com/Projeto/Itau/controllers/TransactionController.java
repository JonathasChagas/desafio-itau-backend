package com.Projeto.Itau.controllers;

import com.Projeto.Itau.dto.TransactionDTO;
import com.Projeto.Itau.entities.Transaction;
import com.Projeto.Itau.mapper.TransactionMapper;
import com.Projeto.Itau.services.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedList;
import java.util.List;

@Log4j2
@RestController
@Validated
@RequestMapping("/")
public class TransactionController {
    private final List<Transaction> transactionList = new LinkedList<>();
    private int timeToConsiderTransactionStatistics = 60;

    @PostMapping("/transacao")
    public ResponseEntity<HttpStatus> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        log.info("Recebendo transação");

        if (isValueInvalid(transactionDTO.getValor())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        if (isTimeNegative(transactionDTO.getDataHora())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        Transaction transaction = TransactionMapper.TransactionDtoToTransaction(transactionDTO);
        transactionList.add(transaction);
        log.info("Salvando transação");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/tempo-estatistica")
    public ResponseEntity<HttpStatus> changeStatisticLastXSeconds(@RequestBody int seconds) {
        if (isSecondsDefinedInvalid(seconds)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        timeToConsiderTransactionStatistics = seconds;
        log.info("Tempo para cálculo de estatísticas alterado para '{}' segundos", seconds);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/transacao")
    public ResponseEntity<HttpStatus> deleteAllTransactions() {
        deleteAllTransactions(transactionList);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping ("/estatistica")
    public ResponseEntity<DoubleSummaryStatistics> getLastXSecondsTransactionStatistics() {
        log.info("Calculando as estatísticas das transações dos ultimos 60 segundos");

        OffsetDateTime start = OffsetDateTime.now();
        DoubleSummaryStatistics statistics = getStatistics(transactionList, start, timeToConsiderTransactionStatistics);
        
        long secondsTakenStatistics = calculateDurationInNanos(start);
        logStatistics(statistics, secondsTakenStatistics);
        
        return ResponseEntity.status(HttpStatus.OK).body(statistics);
    }

    private boolean isValueInvalid(float valor) {
        if (TransactionService.isValueInvalid(valor)){
            log.error("VALOR INVÁLIDO, MENOR DO QUE 0");
            return true;
        }
        return false;
    }

    private boolean isTimeNegative(OffsetDateTime dataHora) {
        if (TransactionService.isTransactionTimeNegative(dataHora)){
            log.error("VALOR INVÁLIDO, TRANSAÇÃO ENVIADA OCORRE NO FUTURO");
            return true;
        }
        return false;
    }

    private boolean isSecondsDefinedInvalid(int seconds) {
        if (TransactionService.isSecondsDefinedInvalid(seconds)){
            log.error("TEMPO INVALIDO, VALOR NEGATIVO INSERIDO");
            return true;
        }
        return false;
    }

    private void deleteAllTransactions(List<Transaction> transactionList) {
        log.info("Deletando todas as transações");
        TransactionService.deleteAllTransactions(transactionList);
    }

    private static DoubleSummaryStatistics getStatistics(List<Transaction> transactionList, OffsetDateTime start, int timeToConsiderTransactionStatistics) {
        log.info("Calculando estatísticas");
        return TransactionService.getStatistics(transactionList, start, timeToConsiderTransactionStatistics);
    }

    private static long calculateDurationInNanos(OffsetDateTime start) {
        log.info("Obtendo o tempo que leva para calculas as estatísticas");
        return TransactionService.calculateDurationInNanos(start);
    }

    private static void logStatistics(DoubleSummaryStatistics statistics, long secondsTakenStatistics) {
        log.info("Tempo levado para calculas as estatísticas: '{}'", secondsTakenStatistics);
        log.info("Quantidade de transações calculadas: '{}'", statistics.getCount());

        long averageTimePerTransaction = TransactionService.logStatistics(statistics, secondsTakenStatistics);

        log.info("Tempo médio por transação calculada: '{}' nanos", averageTimePerTransaction);
    }
}
