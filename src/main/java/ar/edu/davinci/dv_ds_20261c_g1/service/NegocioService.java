package ar.edu.davinci.dv_ds_20261c_g1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import ar.edu.davinci.dv_ds_20261c_g1.controller.response.ResumenGananciasDia;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Venta;

public interface NegocioService {

    List<Venta> ventasPorFecha(LocalDate fecha);

    BigDecimal calcularGananciasDelDia(LocalDate fecha);

    ResumenGananciasDia calcularResumenDelDia(LocalDate fecha);
}
