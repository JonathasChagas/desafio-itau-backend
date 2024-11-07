package com.Projeto.Itau.services;

import com.Projeto.Itau.dto.TransactionDTO;
import com.Projeto.Itau.entities.Transaction;
import com.Projeto.Itau.mapper.TransactionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionService {

    public void processTransaction(TransactionDTO transaction) {
        validateTransaction(transaction);
    }

    public void isSecondsDefinedValid(int seconds) {
        if (seconds <= 0) throw new IllegalArgumentException("Tempo em segundos deve ser maior do que 0");
    }

    public void deleteAllTransactions(List<Transaction> transactionList) {
        log.info("Deletando todas as transações");
        transactionList.clear();
    }

    public DoubleSummaryStatistics getStatistics(List<Transaction> transactionList, OffsetDateTime start, int timeToConsiderTransactionStatistics) {
        return transactionList.stream()
                .map(TransactionMapper::transactionToTransactionDto)
                .filter(transactionDto -> {
                    long durationInSeconds = TimeUnit.SECONDS.convert(Duration.between(transactionDto.getDataHora(), start));
                    return durationInSeconds <= TimeUnit.SECONDS.toSeconds(timeToConsiderTransactionStatistics);
                })
                .collect(Collectors.summarizingDouble(TransactionDTO::getValor));
    }

    public long calculateDurationInNanos(OffsetDateTime start) {
        log.info("Obtendo o tempo que leva para calculas as estatísticas");

        var end = OffsetDateTime.now();
        return Duration.between(start, end).getSeconds();
    }

    public long logStatistics(DoubleSummaryStatistics statistics, long secondsTakenStatistics) {
        log.info("Tempo levado para calculas as estatísticas: '{}'", secondsTakenStatistics);
        log.info("Quantidade de transações calculadas: '{}'", statistics.getCount());

        return (statistics.getCount() > 0)
                ? secondsTakenStatistics / statistics.getCount()
                : 0;
    }

    private void validateTransaction(TransactionDTO transaction) {
        validateTransactionValueOrThrowIllegalArgument(transaction.getValor());

        validateTransactionTimeOrThrowIllegalArgument(transaction.getDataHora());
    }

    private void validateTransactionValueOrThrowIllegalArgument(float value) {
        if (value < 0) throw new IllegalArgumentException("Valor deve ser maior ou igual a 0.");
    }

    private void validateTransactionTimeOrThrowIllegalArgument(OffsetDateTime startMoment) {
        var duration = Duration.between(startMoment, OffsetDateTime.now());

        if (duration.isNegative()) throw new IllegalArgumentException("Transação deve ocorrer em algum momento do passado.");
    }
}


