package ar.edu.davinci.dv_ds_20261c_g1.controller.web;

import java.math.BigDecimal;

import ar.edu.davinci.dv_ds_20261c_g1.domain.EstadoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoPrenda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrendaWebForm {

    private Long id;
    private String descripcion;
    private BigDecimal precioBase;
    private TipoPrenda tipoPrenda;
    private EstadoPrenda estadoPrenda;
    private BigDecimal valorPromocion;
    private Integer stockInicial;
    private Integer stockMinimo;

    public static PrendaWebForm from(Prenda prenda) {
        return PrendaWebForm.builder()
                .id(prenda.getId())
                .descripcion(prenda.getDescripcion())
                .precioBase(prenda.getPrecioBase())
                .tipoPrenda(prenda.getTipoPrenda())
                .estadoPrenda(prenda.getEstadoPrenda())
                .valorPromocion(prenda.getValorPromocion())
                .build();
    }

    public Prenda toPrenda() {
        return Prenda.builder()
                .descripcion(descripcion)
                .precioBase(precioBase)
                .tipoPrenda(tipoPrenda)
                .estadoPrenda(estadoPrenda)
                .valorPromocion(valorPromocion)
                .build();
    }
}
