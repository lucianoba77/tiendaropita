package ar.edu.davinci.dv_ds_20261c_g1.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Linea de detalle de una {@link Venta}. Asocia una {@link Prenda} con una
 * cantidad y conoce su importe (precio de venta unitario por cantidad).
 */
@Entity
@Table(name = "items")
@Getter
@Setter
@ToString(exclude = "venta")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item implements Serializable {

    private static final long serialVersionUID = 7984512369874512369L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itm_prd_id", referencedColumnName = "prd_id", nullable = false)
    private Prenda prenda;

    @Column(name = "itm_cantidad", nullable = false)
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itm_vta_id", referencedColumnName = "vta_id")
    @JsonBackReference
    private Venta venta;

    /**
     * Importe de la linea = precio de venta unitario de la prenda * cantidad.
     */
    @Transient
    public BigDecimal importe() {
        if (prenda == null || cantidad == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal precioUnitario = prenda.precioVenta();
        if (precioUnitario == null) {
            return BigDecimal.ZERO;
        }
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
