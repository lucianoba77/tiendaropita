package ar.edu.davinci.dv_ds_20261c_g1.controller.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ar.edu.davinci.dv_ds_20261c_g1.domain.EstadoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.service.MovimientoStockService;
import ar.edu.davinci.dv_ds_20261c_g1.service.PrendaService;
import ar.edu.davinci.dv_ds_20261c_g1.service.StockService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tienda/prendas")
@RequiredArgsConstructor
public class PrendaController {

    private static final String REDIRECT_LIST = "redirect:/tienda/prendas/list";

    private final PrendaService prendaService;

    private final StockService stockService;

    private final MovimientoStockService movimientoStockService;

    @GetMapping("/list")
    public String list(Model model) {
        var prendas = prendaService.list();
        Map<Long, Integer> stockPorPrenda = new HashMap<>();
        Map<Long, Integer> stockMinimoPorPrenda = new HashMap<>();
        Map<Long, Boolean> stockBajoPorPrenda = new HashMap<>();
        prendas.forEach(p -> {
            Long id = p.getId();
            stockPorPrenda.put(id, stockService.cantidadDisponible(id));
            stockMinimoPorPrenda.put(id, stockService.stockMinimo(id));
            stockBajoPorPrenda.put(id, stockService.estaBajoMinimo(id));
        });
        model.addAttribute("listPrendas", prendas);
        model.addAttribute("stockPorPrenda", stockPorPrenda);
        model.addAttribute("stockMinimoPorPrenda", stockMinimoPorPrenda);
        model.addAttribute("stockBajoPorPrenda", stockBajoPorPrenda);
        return "list_prendas";
    }

    @GetMapping("/new")
    public String nuevo(Model model) {
        model.addAttribute("prenda", new PrendaWebForm());
        cargarCombos(model);
        return "new_prendas";
    }

    @PostMapping("/save")
    public String guardar(@ModelAttribute("prenda") PrendaWebForm form) throws BusinessException {
        Prenda guardada = prendaService.save(form.toPrenda());
        stockService.establecer(guardada, form.getStockInicial(), form.getStockMinimo());
        return REDIRECT_LIST;
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) throws BusinessException {
        PrendaWebForm form = PrendaWebForm.from(prendaService.get(id));
        form.setStockMinimo(stockService.stockMinimo(id));
        model.addAttribute("prenda", form);
        model.addAttribute("stockActual", stockService.cantidadDisponible(id));
        cargarCombos(model);
        return "edit_prendas";
    }

    @PostMapping("/update/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute("prenda") PrendaWebForm form,
            @RequestParam(name = "stock", required = false) Integer stock)
            throws BusinessException {
        Prenda actualizada = prendaService.update(id, form.toPrenda());
        Integer cantidad = stock != null ? stock : stockService.cantidadDisponible(id);
        stockService.establecer(actualizada, cantidad, form.getStockMinimo());
        return REDIRECT_LIST;
    }

    @GetMapping("/{id}/stock/historial")
    public String historialStock(@PathVariable Long id, Model model) throws BusinessException {
        Prenda prenda = prendaService.get(id);
        model.addAttribute("prenda", prenda);
        model.addAttribute("stockActual", stockService.cantidadDisponible(id));
        model.addAttribute("movimientos", movimientoStockService.listarPorPrenda(id));
        return "historial_stock";
    }

    @PostMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) throws BusinessException {
        prendaService.delete(id);
        return REDIRECT_LIST;
    }

    private void cargarCombos(Model model) {
        model.addAttribute("tipos", TipoPrenda.values());
        model.addAttribute("estados", EstadoPrenda.values());
    }
}
