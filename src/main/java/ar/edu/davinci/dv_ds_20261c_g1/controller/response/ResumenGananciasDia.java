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
public class ResumenGananciasDia {

    private BigDecimal total;
    private int cantidadVentas;
    private BigDecimal totalEfectivo;
    private BigDecimal totalTarjeta;
    private String prendaMasVendidaDescripcion;
    private Integer prendaMasVendidaUnidades;
}
