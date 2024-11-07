package com.Projeto.Itau.mapper;

import com.Projeto.Itau.dto.TransactionDTO;
import com.Projeto.Itau.entities.Transaction;
import org.springframework.beans.BeanUtils;

public abstract class TransactionMapper {
    public static Transaction transactionDtoToTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionDTO, transaction);
        return transaction;
    }

    public static TransactionDTO transactionToTransactionDto(Transaction transaction) {
        TransactionDTO transactionDto = new TransactionDTO();
        BeanUtils.copyProperties(transaction, transactionDto);
        return transactionDto;
    }
}
