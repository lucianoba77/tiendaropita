package ar.edu.davinci.dv_ds_20261c_g1.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Registro de auditoria de cada cambio de stock (Observer simplificado).
 */
@Entity
@Table(name = "movimientos_stock")
@Getter
@Setter
@ToString(exclude = "prenda")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mst_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mst_prd_id", referencedColumnName = "prd_id", nullable = false)
    private Prenda prenda;

    @Column(name = "mst_cantidad", nullable = false)
    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "mst_tipo", nullable = false)
    private TipoMovimientoStock tipo;

    @Column(name = "mst_fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "mst_vta_id")
    private Long referenciaVentaId;

    @Column(name = "mst_observacion")
    private String observacion;
}
