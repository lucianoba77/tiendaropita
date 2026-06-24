package ar.edu.davinci.dv_ds_20261c_g1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
