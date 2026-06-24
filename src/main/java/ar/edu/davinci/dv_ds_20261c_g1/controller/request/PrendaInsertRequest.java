package ar.edu.davinci.dv_ds_20261c_g1.controller.request;

import java.math.BigDecimal;

import ar.edu.davinci.dv_ds_20261c_g1.domain.EstadoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoPrenda;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrendaInsertRequest {

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    @NotNull(message = "El precio base es obligatorio")
    @PositiveOrZero(message = "El precio base debe ser mayor o igual a cero")
    private BigDecimal precioBase;

    @NotNull(message = "El tipo de prenda es obligatorio")
    private TipoPrenda tipoPrenda;

    @NotNull(message = "El estado de prenda es obligatorio")
    private EstadoPrenda estadoPrenda;

    private BigDecimal valorPromocion;

    @PositiveOrZero(message = "El stock inicial debe ser mayor o igual a cero")
    private Integer stockInicial;

    @PositiveOrZero(message = "El stock minimo debe ser mayor o igual a cero")
    private Integer stockMinimo;
}
