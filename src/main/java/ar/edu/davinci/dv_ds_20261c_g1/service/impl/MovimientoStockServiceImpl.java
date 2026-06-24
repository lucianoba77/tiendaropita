package ar.edu.davinci.dv_ds_20261c_g1.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.davinci.dv_ds_20261c_g1.domain.MovimientoStock;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoMovimientoStock;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.repository.MovimientoStockRepository;
import ar.edu.davinci.dv_ds_20261c_g1.repository.PrendaRepository;
import ar.edu.davinci.dv_ds_20261c_g1.service.MovimientoStockService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovimientoStockServiceImpl implements MovimientoStockService {

    private static final ZoneId ZONA_HORARIA = ZoneId.of("America/Argentina/Buenos_Aires");

    private final MovimientoStockRepository movimientoStockRepository;

    private final PrendaRepository prendaRepository;

    @Override
    @Transactional
    public void registrar(Long prendaId, Integer cantidad, TipoMovimientoStock tipo,
            Long referenciaVentaId, String observacion) throws BusinessException {
        if (prendaId == null) {
            throw new BusinessException("La prenda es obligatoria para registrar un movimiento de stock");
        }
        if (cantidad == null || cantidad <= 0) {
            throw new BusinessException("La cantidad del movimiento debe ser mayor a cero");
        }
        if (tipo == null) {
            throw new BusinessException("El tipo de movimiento es obligatorio");
        }
        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new BusinessException("No existe la prenda con id " + prendaId));
        MovimientoStock movimiento = MovimientoStock.builder()
                .prenda(prenda)
                .cantidad(cantidad)
                .tipo(tipo)
                .fecha(LocalDateTime.now(ZONA_HORARIA))
                .referenciaVentaId(referenciaVentaId)
                .observacion(observacion)
                .build();
        movimientoStockRepository.save(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoStock> listarPorPrenda(Long prendaId) throws BusinessException {
        if (prendaId == null) {
            throw new BusinessException("El id de la prenda es obligatorio");
        }
        if (!prendaRepository.existsById(prendaId)) {
            throw new BusinessException("No existe la prenda con id " + prendaId);
        }
        return movimientoStockRepository.findByPrenda_IdOrderByFechaDesc(prendaId);
    }
}
