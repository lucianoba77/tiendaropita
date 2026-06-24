package ar.edu.davinci.dv_ds_20261c_g1.controller.response;

import java.time.LocalDateTime;

import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoMovimientoStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoStockResponse {

    private Long id;
    private Long prendaId;
    private Integer cantidad;
    private TipoMovimientoStock tipo;
    private LocalDateTime fecha;
    private Long referenciaVentaId;
    private String observacion;
}
