package ar.edu.davinci.dv_ds_20261c_g1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Negocio;

public interface NegocioRepository extends JpaRepository<Negocio, Long> {
}
