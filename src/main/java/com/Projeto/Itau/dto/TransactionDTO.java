package com.Projeto.Itau.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    @NotNull(message = "O campo valor precisa ser preenchido.")
    private float valor;

    @NotNull (message = "O campo de data e hora precisa ser preenchido.")
    private OffsetDateTime dataHora;
}
