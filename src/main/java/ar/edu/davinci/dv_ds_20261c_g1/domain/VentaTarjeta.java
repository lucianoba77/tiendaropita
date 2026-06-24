package ar.edu.davinci.dv_ds_20261c_g1.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Venta con tarjeta. Aplica un recargo en funcion de las cuotas y un
 * coeficiente: recargo = cantidadCuotas * coeficiente * importeBruto.
 * Sin descuento.
 */
@Entity
@PrimaryKeyJoinColumn(name = "vta_id")
@DiscriminatorValue("TARJETA")
@Table(name = "ventas_tarjeta")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class VentaTarjeta extends Venta implements Serializable {

    private static final long serialVersionUID = 7549753306871297143L;

    @Column(name = "vtt_cantidad_cuotas")
    private Integer cantidadCuotas;

    @Column(name = "vtt_coeficiente", precision = 19, scale = 4)
    private BigDecimal coeficiente;

    @Override
    public BigDecimal recargo(BigDecimal importeBruto) {
        if (importeBruto == null || cantidadCuotas == null || coeficiente == null) {
            return BigDecimal.ZERO;
        }
        return coeficiente
                .multiply(BigDecimal.valueOf(cantidadCuotas))
                .multiply(importeBruto);
    }

    @Override
    public BigDecimal descuento(BigDecimal importeBruto) {
        return BigDecimal.ZERO;
    }
}
