package ar.edu.davinci.dv_ds_20261c_g1.controller.web;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ar.edu.davinci.dv_ds_20261c_g1.service.NegocioService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tienda/negocio")
@RequiredArgsConstructor
public class NegocioController {

    private final NegocioService negocioService;

    @GetMapping("/ganancias")
    public String ganancias(
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha,
            Model model) {
        if (fecha != null) {
            model.addAttribute("fecha", fecha);
            var resumen = negocioService.calcularResumenDelDia(fecha);
            model.addAttribute("resumen", resumen);
            model.addAttribute("ganancias", resumen.getTotal());
            model.addAttribute("ventas", negocioService.ventasPorFecha(fecha));
        }
        return "ganancias";
    }
}
