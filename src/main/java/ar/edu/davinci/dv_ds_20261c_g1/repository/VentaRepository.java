package ar.edu.davinci.dv_ds_20261c_g1.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByFecha(LocalDate fecha);
}
