package ar.edu.davinci.dv_ds_20261c_g1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.davinci.dv_ds_20261c_g1.domain.MovimientoStock;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {

    List<MovimientoStock> findByPrenda_IdOrderByFechaDesc(Long prendaId);
}
