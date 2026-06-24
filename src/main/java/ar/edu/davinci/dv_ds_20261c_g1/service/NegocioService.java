package ar.edu.davinci.dv_ds_20261c_g1.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Venta;

public interface NegocioService {

    List<Venta> ventasPorFecha(Date fecha);

    BigDecimal calcularGananciasDelDia(Date fecha);
}
