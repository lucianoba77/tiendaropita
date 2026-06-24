package ar.edu.davinci.dv_ds_20261c_g1.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ar.edu.davinci.dv_ds_20261c_g1.controller.response.ItemResponse;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "prendaId", source = "prenda.id")
    @Mapping(target = "prendaDescripcion", source = "prenda.descripcion")
    @Mapping(target = "precioUnitario", expression = "java(item.getPrenda() != null ? item.getPrenda().precioVenta() : null)")
    @Mapping(target = "importe", expression = "java(item.importe())")
    ItemResponse toResponse(Item item);

    List<ItemResponse> toResponseList(List<Item> items);
}
