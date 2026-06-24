package ar.edu.davinci.dv_ds_20261c_g1.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Venta en efectivo. Sin recargo. Aplica un descuento del 15% si el importe
 * bruto supera el limite, o del 10% en caso contrario.
 */
@Entity
@PrimaryKeyJoinColumn(name = "vta_id")
@DiscriminatorValue("EFECTIVO")
@Table(name = "ventas_efectivo")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class VentaEfectivo extends Venta implements Serializable {

    private static final long serialVersionUID = 7549753306871297144L;

    private static final BigDecimal LIMITE_VENTA = new BigDecimal("1000.00");
    private static final BigDecimal DESCUENTO_ALTO = new BigDecimal("0.15");
    private static final BigDecimal DESCUENTO_BAJO = new BigDecimal("0.10");

    @Override
    public BigDecimal recargo(BigDecimal importeBruto) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal descuento(BigDecimal importeBruto) {
        if (importeBruto == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal porcentaje = importeBruto.compareTo(LIMITE_VENTA) > 0 ? DESCUENTO_ALTO : DESCUENTO_BAJO;
        return importeBruto.multiply(porcentaje);
    }
}
