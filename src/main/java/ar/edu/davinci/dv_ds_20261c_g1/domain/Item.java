package ar.edu.davinci.dv_ds_20261c_g1.domain;

import java.io.Serializable;
import java.math.BigDecimal;

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

@Entity
@Table(name = "items")
@Getter
@Setter
@ToString(exclude = "venta")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itm_id")
    private Long id;

    @Column(name = "itm_cantidad")
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itm_prd_id", referencedColumnName = "prd_id")
    private Prenda prenda;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itm_vta_id", referencedColumnName = "vta_id")
    @JsonBackReference
    private Venta venta;

    /**
     * Importe del item = precio de venta de la prenda * cantidad.
     */
    @Transient
    public BigDecimal importe() {
        if (prenda == null || cantidad == null) {
            return BigDecimal.ZERO;
        }
        return prenda.precioVenta().multiply(BigDecimal.valueOf(cantidad));
    }
}
