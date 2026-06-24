package ar.edu.davinci.dv_ds_20261c_g1.controller.response;

import java.math.BigDecimal;

import ar.edu.davinci.dv_ds_20261c_g1.domain.EstadoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoPrenda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrendaResponse {

    private Long id;
    private String descripcion;
    private BigDecimal precioBase;
    private TipoPrenda tipoPrenda;
    private EstadoPrenda estadoPrenda;
    private BigDecimal valorPromocion;
    private BigDecimal precioVenta;
}
