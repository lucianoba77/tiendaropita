package ar.edu.davinci.dv_ds_20261c_g1.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidad Negocio/Tienda. Posee una lista de ventas y permite calcular las
 * ganancias de un dia (suma de los totales de las ventas de esa fecha).
 */
@Entity
@Table(name = "negocios")
@Getter
@Setter
@ToString(exclude = "ventas")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Negocio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "neg_id")
    private Long id;

    @Column(name = "neg_nombre")
    private String nombre;

    @OneToMany(mappedBy = "negocio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Venta> ventas;

    public void addVenta(Venta venta) {
        if (this.ventas == null) {
            this.ventas = new ArrayList<>();
        }
        venta.setNegocio(this);
        this.ventas.add(venta);
    }

    /**
     * Ganancias de un dia = total de las ventas cuya fecha coincide con la fecha indicada.
     */
    @Transient
    public BigDecimal calcularGananciasDelDia(Date fecha) {
        if (ventas == null || fecha == null) {
            return BigDecimal.ZERO;
        }
        LocalDate objetivo = aLocalDate(fecha);
        return ventas.stream()
                .filter(v -> v.getFecha() != null && aLocalDate(v.getFecha()).equals(objetivo))
                .map(Venta::calcularTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private LocalDate aLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
