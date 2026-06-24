package ar.edu.davinci.dv_ds_20261c_g1.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Entidad raiz de las ventas. Implementa el patron Template Method:
 * {@link #calcularTotal()} define el algoritmo (bruto + recargo - descuento)
 * y delega los pasos variables en {@link #recargo(BigDecimal)} y
 * {@link #descuento(BigDecimal)}.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_venta")
@Table(name = "ventas")
@Getter
@Setter
@ToString(exclude = "items")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Venta implements Serializable {

    private static final long serialVersionUID = 4377003933780707501L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vta_id")
    private Long id;

    @ManyToOne(targetEntity = Cliente.class, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "vta_cli_id", referencedColumnName = "cli_id", nullable = false)
    private Cliente cliente;

    @Column(name = "vta_fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<Item> items;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vta_neg_id", referencedColumnName = "neg_id")
    private Negocio negocio;

    /**
     * Paso variable: recargo aplicado sobre el importe bruto.
     */
    public abstract BigDecimal recargo(BigDecimal importeBruto);

    /**
     * Paso variable: descuento aplicado sobre el importe bruto.
     */
    public abstract BigDecimal descuento(BigDecimal importeBruto);

    /**
     * Importe bruto = suma de los importes de todos los items.
     */
    @Transient
    public BigDecimal importeBruto() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(Item::importe)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Template Method: total = bruto + recargo - descuento.
     */
    @Transient
    public BigDecimal calcularTotal() {
        BigDecimal bruto = importeBruto();
        BigDecimal total = bruto.add(recargo(bruto)).subtract(descuento(bruto));
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public void addItem(Item item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        item.setVenta(this);
        this.items.add(item);
    }

    public void removeItem(Item item) {
        if (this.items != null) {
            this.items.remove(item);
            item.setVenta(null);
        }
    }
}
