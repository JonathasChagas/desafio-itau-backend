package com.Projeto.Itau.entities;

import lombok.Data;
import lombok.ToString;

import java.time.OffsetDateTime;


@Data
public class Transaction {
    private float valor;
    private OffsetDateTime dataHora;
}