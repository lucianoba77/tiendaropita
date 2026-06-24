package ar.edu.davinci.dv_ds_20261c_g1.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({ "/", "/tienda", "/tienda/" })
    public String index() {
        return "index";
    }
}
