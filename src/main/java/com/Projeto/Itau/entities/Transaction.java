package com.Projeto.Itau.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class Transaction {
    private Float valor;
    private OffsetDateTime dataHora;
}