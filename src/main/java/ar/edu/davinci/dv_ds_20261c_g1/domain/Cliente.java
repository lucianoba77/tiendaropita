package ar.edu.davinci.dv_ds_20261c_g1.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente implements Serializable {

    private static final long serialVersionUID = 6363777413501451503L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cli_id")
    private Long id;

    @Column(name = "cli_nombre")
    private String nombre;

    @Column(name = "cli_apellido")
    private String apellido;

    @Transient
    public String getRazonSocial() {
        return (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "");
    }
}
