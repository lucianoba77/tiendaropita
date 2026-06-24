package ar.edu.davinci.dv_ds_20261c_g1.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.edu.davinci.dv_ds_20261c_g1.domain.EstadoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.service.PrendaService;

@Controller
@RequestMapping("/tienda/prendas")
public class PrendaController {

    @Autowired
    private PrendaService prendaService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("listPrendas", prendaService.list());
        return "list_prendas";
    }

    @GetMapping("/new")
    public String nuevo(Model model) {
        model.addAttribute("prenda", new Prenda());
        cargarCombos(model);
        return "new_prendas";
    }

    @PostMapping("/save")
    public String guardar(@ModelAttribute("prenda") Prenda prenda, Model model) throws BusinessException {
        prendaService.save(prenda);
        return "redirect:/tienda/prendas/list";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) throws BusinessException {
        model.addAttribute("prenda", prendaService.get(id));
        cargarCombos(model);
        return "edit_prendas";
    }

    @PostMapping("/update/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute("prenda") Prenda prenda)
            throws BusinessException {
        prendaService.update(id, prenda);
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
