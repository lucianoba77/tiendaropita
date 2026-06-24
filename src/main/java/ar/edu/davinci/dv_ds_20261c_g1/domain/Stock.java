package ar.edu.davinci.dv_ds_20261c_g1.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Administra el stock (cantidad disponible) de una {@link Prenda}.
 * Punto 5 del TP: al vender un articulo se descuenta del stock la cantidad vendida.
 */
@Entity
@Table(name = "stocks")
@Getter
@Setter
@ToString(exclude = "prenda")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock implements Serializable {

    private static final long serialVersionUID = 3148907654321987654L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stk_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stk_prd_id", referencedColumnName = "prd_id", unique = true, nullable = false)
    @JsonIgnore
    private Prenda prenda;

    @Column(name = "stk_cantidad", nullable = false)
    private Integer cantidad;

    /**
     * Indica si hay stock suficiente para descontar la cantidad pedida.
     */
    @Transient
    public boolean tieneStockSuficiente(int cantidadPedida) {
        return cantidad != null && cantidad >= cantidadPedida;
    }

    /**
     * Descuenta la cantidad vendida del stock disponible.
     * La validacion de suficiencia es responsabilidad del servicio.
     */
    public void descontar(int cantidadVendida) {
        int actual = (cantidad != null) ? cantidad : 0;
        this.cantidad = actual - cantidadVendida;
    }

    /**
     * Repone unidades al stock (por ejemplo al quitar un item de una venta).
     */
    public void reponer(int cantidadRepuesta) {
        int actual = (cantidad != null) ? cantidad : 0;
        this.cantidad = actual + cantidadRepuesta;
    }
}
