package com.Projeto.Itau.services;

import com.Projeto.Itau.dto.TransactionDTO;
import com.Projeto.Itau.entities.Transaction;
import com.Projeto.Itau.mapper.TransactionMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Log4j2
@Service
public class TransactionService
{
    public static boolean isValueInvalid(float value) {
        return value <= 0;
    }

    public static boolean isTransactionTimeNegative(OffsetDateTime startMoment) {
        Duration duration = Duration.between(startMoment, OffsetDateTime.now());
        return duration.isNegative();
    }

    public static boolean isSecondsDefinedInvalid(int seconds) {
        return seconds <= 0;
    }

    public static void deleteAllTransactions(List<Transaction> transactionList) {
        transactionList.clear();
    }

    public static DoubleSummaryStatistics getStatistics(List<Transaction> transactionList, OffsetDateTime start, int timeToConsiderTransactionStatistics) {
        return transactionList
                .stream()
                .map(TransactionMapper::TransactionToTransactionDto)
                .filter(d -> TimeUnit.SECONDS.convert(Duration.between(d.getDataHora(), start)) <= TimeUnit.SECONDS.toSeconds(timeToConsiderTransactionStatistics))
                .collect(Collectors.summarizingDouble(TransactionDTO::getValor));
    }

    public static long calculateDurationInNanos(OffsetDateTime start) {
        OffsetDateTime end = OffsetDateTime.now();
        return Duration.between(start, end).getSeconds();
    }

    public static long logStatistics(DoubleSummaryStatistics statistics, long secondsTakenStatistics) {
        return (statistics.getCount() > 0)
                ? secondsTakenStatistics / statistics.getCount()
                : 0;
    }
}


