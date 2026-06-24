package ar.edu.davinci.dv_ds_20261c_g1.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByFecha(Date fecha);
}
