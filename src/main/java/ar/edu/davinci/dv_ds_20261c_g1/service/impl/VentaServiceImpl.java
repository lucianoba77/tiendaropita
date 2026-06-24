package ar.edu.davinci.dv_ds_20261c_g1.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Cliente;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Item;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Venta;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaEfectivo;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaTarjeta;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.repository.VentaRepository;
import ar.edu.davinci.dv_ds_20261c_g1.service.ClienteService;
import ar.edu.davinci.dv_ds_20261c_g1.service.PrendaService;
import ar.edu.davinci.dv_ds_20261c_g1.service.StockService;
import ar.edu.davinci.dv_ds_20261c_g1.service.VentaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private static final ZoneId ZONA_HORARIA = ZoneId.of("America/Argentina/Buenos_Aires");

    private final VentaRepository ventaRepository;

    private final ClienteService clienteService;

    private final PrendaService prendaService;

    private final StockService stockService;

    @Override
    @Transactional(readOnly = true)
    public List<Venta> list() {
        return ventaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Venta get(Long id) throws BusinessException {
        return buscarVenta(id);
    }

    @Override
    @Transactional
    public VentaEfectivo saveEfectivo(VentaEfectivo venta) throws BusinessException {
        if (venta == null) {
            throw new BusinessException("La venta es obligatoria");
        }
        prepararVenta(venta);
        return guardarVentaEfectivo(Objects.requireNonNull(venta));
    }

    @Override
    @Transactional
    public VentaTarjeta saveTarjeta(VentaTarjeta venta) throws BusinessException {
        if (venta == null) {
            throw new BusinessException("La venta es obligatoria");
        }
        if (venta.getCantidadCuotas() == null || venta.getCantidadCuotas() <= 0) {
            throw new BusinessException("La cantidad de cuotas debe ser mayor a cero");
        }
        if (venta.getCoeficiente() == null) {
            throw new BusinessException("El coeficiente de la tarjeta es obligatorio");
        }
        prepararVenta(venta);
        return guardarVentaTarjeta(Objects.requireNonNull(venta));
    }

    @Override
    @Transactional
    public void delete(Long id) throws BusinessException {
        Venta venta = buscarVenta(id);
        if (venta.getItems() != null) {
            for (Item item : venta.getItems()) {
                if (item.getPrenda() != null && item.getPrenda().getId() != null) {
                    stockService.reponer(item.getPrenda().getId(), item.getCantidad(), id);
                }
            }
        }
        ventaRepository.delete(venta);
    }

    @Override
    @Transactional
    public Venta addItem(Long ventaId, Long prendaId, Integer cantidad) throws BusinessException {
        if (cantidad == null || cantidad <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a cero");
        }
        Venta venta = buscarVenta(ventaId);
        Prenda prenda = prendaService.get(prendaId);
        stockService.descontar(prendaId, cantidad, ventaId);
        Item item = Item.builder()
                .prenda(prenda)
                .cantidad(cantidad)
                .build();
        venta.addItem(item);
        return guardarVenta(venta);
    }

    @Override
    @Transactional
    public Venta updateItem(Long ventaId, Long itemId, Integer cantidad) throws BusinessException {
        if (cantidad == null || cantidad <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a cero");
        }
        Venta venta = buscarVenta(ventaId);
        Item item = buscarItem(venta, itemId);
        Long prendaId = (item.getPrenda() != null) ? item.getPrenda().getId() : null;
        int cantidadAnterior = (item.getCantidad() != null) ? item.getCantidad() : 0;
        int diferencia = cantidad - cantidadAnterior;
        if (prendaId != null) {
            if (diferencia > 0) {
                stockService.descontar(prendaId, diferencia, ventaId);
            } else if (diferencia < 0) {
                stockService.reponer(prendaId, -diferencia, ventaId);
            }
        }
        item.setCantidad(cantidad);
        return guardarVenta(venta);
    }

    @Override
    @Transactional
    public Venta removeItem(Long ventaId, Long itemId) throws BusinessException {
        Venta venta = buscarVenta(ventaId);
        Item item = buscarItem(venta, itemId);
        if (item.getPrenda() != null && item.getPrenda().getId() != null) {
            stockService.reponer(item.getPrenda().getId(), item.getCantidad(), ventaId);
        }
        venta.removeItem(item);
        return guardarVenta(venta);
    }

    private VentaEfectivo guardarVentaEfectivo(VentaEfectivo venta) {
        return Objects.requireNonNull(ventaRepository.save(Objects.requireNonNull(venta)));
    }

    private VentaTarjeta guardarVentaTarjeta(VentaTarjeta venta) {
        return Objects.requireNonNull(ventaRepository.save(Objects.requireNonNull(venta)));
    }

    private Venta guardarVenta(Venta venta) {
        return Objects.requireNonNull(ventaRepository.save(Objects.requireNonNull(venta)));
    }

    private @NonNull Venta buscarVenta(Long id) throws BusinessException {
        if (id == null) {
            throw new BusinessException("El id de la venta es obligatorio");
        }
        return Objects.requireNonNull(ventaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("No existe la venta con id " + id)));
    }

    private Item buscarItem(Venta venta, Long itemId) throws BusinessException {
        if (venta.getItems() == null) {
            throw new BusinessException("La venta no tiene items");
        }
        return venta.getItems().stream()
                .filter(i -> i.getId() != null && i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new BusinessException("El item " + itemId + " no pertenece a la venta " + venta.getId()));
    }

    /**
     * Resuelve el cliente, asigna la fecha y vincula los items y sus prendas a la venta.
     */
    private void prepararVenta(Venta venta) throws BusinessException {
        if (venta.getCliente() == null || venta.getCliente().getId() == null) {
            throw new BusinessException("El cliente es obligatorio");
        }
        Cliente cliente = clienteService.get(venta.getCliente().getId());
        venta.setCliente(cliente);

        if (venta.getFecha() == null) {
            venta.setFecha(LocalDate.now(ZONA_HORARIA));
        }

        List<Item> itemsResueltos = new ArrayList<>();
        if (venta.getItems() != null) {
            for (Item item : venta.getItems()) {
                if (item.getPrenda() == null || item.getPrenda().getId() == null) {
                    throw new BusinessException("Cada item debe referenciar una prenda valida");
                }
                if (item.getCantidad() == null || item.getCantidad() <= 0) {
                    throw new BusinessException("La cantidad de cada item debe ser mayor a cero");
                }
                Prenda prenda = prendaService.get(item.getPrenda().getId());
                stockService.descontar(prenda.getId(), item.getCantidad());
                item.setPrenda(prenda);
                item.setVenta(venta);
                itemsResueltos.add(item);
            }
        }
        venta.setItems(itemsResueltos);
    }
}
