package ar.edu.davinci.dv_ds_20261c_g1.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ar.edu.davinci.dv_ds_20261c_g1.controller.response.ItemResponse;
import ar.edu.davinci.dv_ds_20261c_g1.controller.response.VentaResponse;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Item;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Venta;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface VentaMapper {

    @Mapping(target = "tipo", expression = "java(venta.getClass().getSimpleName())")
    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "clienteRazonSocial", expression = "java(venta.getCliente() != null ? venta.getCliente().getRazonSocial() : null)")
    @Mapping(target = "importeBruto", expression = "java(venta.importeBruto())")
    @Mapping(target = "total", expression = "java(venta.calcularTotal())")
    VentaResponse toResponse(Venta venta);

    List<VentaResponse> toResponseList(List<Venta> ventas);

    @Mapping(target = "prendaId", source = "prenda.id")
    @Mapping(target = "prendaDescripcion", source = "prenda.descripcion")
    @Mapping(target = "precioUnitario", expression = "java(item.getPrenda() != null ? item.getPrenda().precioVenta() : null)")
    @Mapping(target = "importe", expression = "java(item.importe())")
    ItemResponse itemToResponse(Item item);
}
