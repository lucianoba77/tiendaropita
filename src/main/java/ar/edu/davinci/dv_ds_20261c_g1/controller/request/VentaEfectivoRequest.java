package ar.edu.davinci.dv_ds_20261c_g1.controller.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaEfectivoRequest {

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @Valid
    private List<ItemRequest> items;
}
