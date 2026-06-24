package ar.edu.davinci.dv_ds_20261c_g1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByPrendaId(Long prendaId);

    void deleteByPrendaId(Long prendaId);
}
