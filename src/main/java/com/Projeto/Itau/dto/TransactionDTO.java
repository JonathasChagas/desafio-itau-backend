package com.Projeto.Itau.dto;


import jakarta.validation.constraints.Min;
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
    @Min(value = 0, message = "O valor deve ser maior ou igual a zero.")
    private Float valor;

    @NotNull (message = "O campo de data e hora precisa ser preenchido.")
    private OffsetDateTime dataHora;
}
