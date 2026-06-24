package ar.edu.davinci.dv_ds_20261c_g1.controller.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteWebForm {

    private Long id;
    private String nombre;
    private String apellido;
}
