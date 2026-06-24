package ar.edu.davinci.dv_ds_20261c_g1.controller.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaResponse {

    private Long id;
    private String tipo;
    private LocalDate fecha;
    private Long clienteId;
    private String clienteRazonSocial;
    private List<ItemResponse> items;
    private BigDecimal importeBruto;
    private BigDecimal total;
}
