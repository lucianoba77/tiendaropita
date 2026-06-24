package ar.edu.davinci.dv_ds_20261c_g1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByPrendaId(Long prendaId);

    void deleteByPrendaId(Long prendaId);

    @Query("SELECT s FROM Stock s WHERE s.stockMinimo > 0 AND s.cantidad < s.stockMinimo")
    List<Stock> findStockBajo();
}
