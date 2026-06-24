package ar.edu.davinci.dv_ds_20261c_g1.controller.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Cliente;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaEfectivo;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaTarjeta;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.service.ClienteService;
import ar.edu.davinci.dv_ds_20261c_g1.service.PrendaService;
import ar.edu.davinci.dv_ds_20261c_g1.service.StockService;
import ar.edu.davinci.dv_ds_20261c_g1.service.VentaService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tienda/ventas")
@RequiredArgsConstructor
public class VentaController {

    private static final String REDIRECT_LIST = "redirect:/tienda/ventas/list";
    private static final String REDIRECT_SHOW_PREFIX = "redirect:/tienda/ventas/show/";

    private final VentaService ventaService;

    private final ClienteService clienteService;

    private final PrendaService prendaService;

    private final StockService stockService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("listVentas", ventaService.list());
        return "list_ventas";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable Long id, Model model) throws BusinessException {
        var prendas = prendaService.list();
        Map<Long, Integer> stockPorPrenda = new HashMap<>();
        prendas.forEach(p -> stockPorPrenda.put(p.getId(), stockService.cantidadDisponible(p.getId())));
        model.addAttribute("venta", ventaService.get(id));
        model.addAttribute("prendas", prendas);
        model.addAttribute("stockPorPrenda", stockPorPrenda);
        return "show_ventas";
    }

    @GetMapping("/new/efectivo")
    public String nuevaEfectivo(Model model) {
        model.addAttribute("clientes", clienteService.list());
        return "new_ventas_efectivo";
    }

    @PostMapping("/save/efectivo")
    public String guardarEfectivo(@RequestParam Long clienteId) throws BusinessException {
        VentaEfectivo venta = VentaEfectivo.builder()
                .cliente(Cliente.builder().id(clienteId).build())
                .items(new ArrayList<>())
                .build();
        VentaEfectivo guardada = ventaService.saveEfectivo(venta);
        return REDIRECT_SHOW_PREFIX + guardada.getId();
    }

    @GetMapping("/new/tarjeta")
    public String nuevaTarjeta(Model model) {
        model.addAttribute("clientes", clienteService.list());
        return "new_ventas_tarjeta";
    }

    @PostMapping("/save/tarjeta")
    public String guardarTarjeta(@RequestParam Long clienteId,
            @RequestParam Integer cantidadCuotas,
            @RequestParam BigDecimal coeficiente) throws BusinessException {
        VentaTarjeta venta = VentaTarjeta.builder()
                .cliente(Cliente.builder().id(clienteId).build())
                .cantidadCuotas(cantidadCuotas)
                .coeficiente(coeficiente)
                .items(new ArrayList<>())
                .build();
        VentaTarjeta guardada = ventaService.saveTarjeta(venta);
        return REDIRECT_SHOW_PREFIX + guardada.getId();
    }

    @PostMapping("/{id}/items/add")
    public String agregarItem(@PathVariable Long id,
            @RequestParam Long prendaId,
            @RequestParam Integer cantidad) throws BusinessException {
        ventaService.addItem(id, prendaId, cantidad);
        return REDIRECT_SHOW_PREFIX + id;
    }

    @PostMapping("/{id}/items/{itemId}/update")
    public String modificarItem(@PathVariable Long id,
            @PathVariable Long itemId,
            @RequestParam Integer cantidad) throws BusinessException {
        ventaService.updateItem(id, itemId, cantidad);
        return REDIRECT_SHOW_PREFIX + id;
    }

    @PostMapping("/{id}/items/{itemId}/delete")
    public String quitarItem(@PathVariable Long id, @PathVariable Long itemId) throws BusinessException {
        ventaService.removeItem(id, itemId);
        return REDIRECT_SHOW_PREFIX + id;
    }

    @PostMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) throws BusinessException {
        ventaService.delete(id);
        return REDIRECT_LIST;
    }
}
