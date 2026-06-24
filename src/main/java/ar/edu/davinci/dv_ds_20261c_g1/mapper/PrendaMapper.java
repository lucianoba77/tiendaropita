package ar.edu.davinci.dv_ds_20261c_g1.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ar.edu.davinci.dv_ds_20261c_g1.controller.request.PrendaInsertRequest;
import ar.edu.davinci.dv_ds_20261c_g1.controller.request.PrendaUpdateRequest;
import ar.edu.davinci.dv_ds_20261c_g1.controller.response.PrendaResponse;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;

@Mapper(componentModel = "spring")
public interface PrendaMapper {

    @Mapping(target = "id", ignore = true)
    Prenda toEntity(PrendaInsertRequest request);

    @Mapping(target = "id", ignore = true)
    Prenda toEntity(PrendaUpdateRequest request);

    @Mapping(target = "precioVenta", expression = "java(prenda.precioVenta())")
    @Mapping(target = "stockDisponible", ignore = true)
    PrendaResponse toResponse(Prenda prenda);

    List<PrendaResponse> toResponseList(List<Prenda> prendas);
}
