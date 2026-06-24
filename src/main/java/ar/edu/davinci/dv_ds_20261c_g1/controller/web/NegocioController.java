package ar.edu.davinci.dv_ds_20261c_g1.controller.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ar.edu.davinci.dv_ds_20261c_g1.service.NegocioService;

@Controller
@RequestMapping("/tienda/negocio")
public class NegocioController {

    @Autowired
    private NegocioService negocioService;

    @GetMapping("/ganancias")
    public String ganancias(
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
            Model model) {
        if (fecha != null) {
            model.addAttribute("fecha", fecha);
            model.addAttribute("ganancias", negocioService.calcularGananciasDelDia(fecha));
            model.addAttribute("ventas", negocioService.ventasPorFecha(fecha));
        }
        return "ganancias";
    }
}
