package ar.edu.davinci.dv_ds_20261c_g1.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Cliente;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.service.ClienteService;

@Controller
@RequestMapping("/tienda/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("listClientes", clienteService.list());
        return "list_clientes";
    }

    @GetMapping("/new")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "new_clientes";
    }

    @PostMapping("/save")
    public String guardar(@ModelAttribute("cliente") Cliente cliente) throws BusinessException {
        clienteService.save(cliente);
        return "redirect:/tienda/clientes/list";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) throws BusinessException {
        model.addAttribute("cliente", clienteService.get(id));
        return "edit_clientes";
    }

    @PostMapping("/update/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute("cliente") Cliente cliente)
            throws BusinessException {
        clienteService.update(id, cliente);
        return "redirect:/tienda/clientes/list";
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) throws BusinessException {
        clienteService.delete(id);
        return "redirect:/tienda/clientes/list";
    }
}
