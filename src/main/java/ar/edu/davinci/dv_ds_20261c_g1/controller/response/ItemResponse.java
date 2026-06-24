package ar.edu.davinci.dv_ds_20261c_g1.controller.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponse {

    private Long id;
    private Long prendaId;
    private String prendaDescripcion;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal importe;
}
