package ar.edu.davinci.dv_ds_20261c_g1.controller.web;

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
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tienda/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private static final String REDIRECT_LIST = "redirect:/tienda/clientes/list";

    private final ClienteService clienteService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("listClientes", clienteService.list());
        return "list_clientes";
    }

    @GetMapping("/new")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new ClienteWebForm());
        return "new_clientes";
    }

    @PostMapping("/save")
    public String guardar(@ModelAttribute("cliente") ClienteWebForm form) throws BusinessException {
        clienteService.save(toCliente(form));
        return REDIRECT_LIST;
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) throws BusinessException {
        Cliente cliente = clienteService.get(id);
        model.addAttribute("cliente", ClienteWebForm.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .build());
        return "edit_clientes";
    }

    @PostMapping("/update/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute("cliente") ClienteWebForm form)
            throws BusinessException {
        clienteService.update(id, toCliente(form));
        return REDIRECT_LIST;
    }

    @PostMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) throws BusinessException {
        clienteService.delete(id);
        return REDIRECT_LIST;
    }

    private Cliente toCliente(ClienteWebForm form) {
        return Cliente.builder()
                .nombre(form.getNombre())
                .apellido(form.getApellido())
                .build();
    }
}
