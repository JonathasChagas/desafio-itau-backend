package com.Projeto.Itau.entities;

import lombok.Data;
import java.time.OffsetDateTime;


@Data
public class Transaction {
    private float valor;
    private OffsetDateTime dataHora;
}