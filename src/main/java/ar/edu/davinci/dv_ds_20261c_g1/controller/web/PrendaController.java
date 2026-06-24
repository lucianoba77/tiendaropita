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
import ar.edu.davinci.dv_ds_20261c_g1.service.PrendaService;
import ar.edu.davinci.dv_ds_20261c_g1.service.StockService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tienda/prendas")
@RequiredArgsConstructor
public class PrendaController {

    private final PrendaService prendaService;

    private final StockService stockService;

    @GetMapping("/list")
    public String list(Model model) {
        var prendas = prendaService.list();
        Map<Long, Integer> stockPorPrenda = new HashMap<>();
        prendas.forEach(p -> stockPorPrenda.put(p.getId(), stockService.cantidadDisponible(p.getId())));
        model.addAttribute("listPrendas", prendas);
        model.addAttribute("stockPorPrenda", stockPorPrenda);
        return "list_prendas";
    }

    @GetMapping("/new")
    public String nuevo(Model model) {
        model.addAttribute("prenda", new Prenda());
        cargarCombos(model);
        return "new_prendas";
    }

    @PostMapping("/save")
    public String guardar(@ModelAttribute("prenda") Prenda prenda,
            @RequestParam(name = "stockInicial", required = false) Integer stockInicial,
            Model model) throws BusinessException {
        Prenda guardada = prendaService.save(prenda);
        stockService.establecer(guardada, stockInicial);
        return "redirect:/tienda/prendas/list";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) throws BusinessException {
        model.addAttribute("prenda", prendaService.get(id));
        model.addAttribute("stockActual", stockService.cantidadDisponible(id));
        cargarCombos(model);
        return "edit_prendas";
    }

    @PostMapping("/update/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute("prenda") Prenda prenda,
            @RequestParam(name = "stock", required = false) Integer stock)
            throws BusinessException {
        Prenda actualizada = prendaService.update(id, prenda);
        if (stock != null) {
            stockService.establecer(actualizada, stock);
        }
        return "redirect:/tienda/prendas/list";
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) throws BusinessException {
        prendaService.delete(id);
        return "redirect:/tienda/prendas/list";
    }

    private void cargarCombos(Model model) {
        model.addAttribute("tipos", TipoPrenda.values());
        model.addAttribute("estados", EstadoPrenda.values());
    }
}
