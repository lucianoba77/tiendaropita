package ar.edu.davinci.dv_ds_20261c_g1.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prendas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prenda implements Serializable {

    private static final long serialVersionUID = -8359168975855133954L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prd_id")
    private Long id;

    @Column(name = "prd_descripcion", nullable = false)
    private String descripcion;

    @Column(name = "prd_precio_base", precision = 19, scale = 2)
    private BigDecimal precioBase;

    @Enumerated(EnumType.STRING)
    @Column(name = "prd_tipo_prenda")
    private TipoPrenda tipoPrenda;

    @Enumerated(EnumType.STRING)
    @Column(name = "prd_estado_prenda")
    private EstadoPrenda estadoPrenda;

    /**
     * Valor fijo de descuento utilizado cuando el estado es PROMOCION.
     */
    @Column(name = "prd_valor_promocion", precision = 19, scale = 2)
    private BigDecimal valorPromocion;

    /**
     * Precio de venta calculado segun el estado de la prenda (patron Strategy).
     */
    @Transient
    public BigDecimal precioVenta() {
        EstadoPrenda estado = (estadoPrenda != null) ? estadoPrenda : EstadoPrenda.NUEVA;
        return estado.getStrategy().precioVenta(this);
    }
}
