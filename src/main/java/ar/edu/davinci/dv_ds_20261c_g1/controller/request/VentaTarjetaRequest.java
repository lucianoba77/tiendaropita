package ar.edu.davinci.dv_ds_20261c_g1.controller.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaTarjetaRequest {

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "La cantidad de cuotas es obligatoria")
    @Positive(message = "La cantidad de cuotas debe ser mayor a cero")
    private Integer cantidadCuotas;

    @NotNull(message = "El coeficiente es obligatorio")
    private BigDecimal coeficiente;

    @Valid
    private List<ItemRequest> items;
}
