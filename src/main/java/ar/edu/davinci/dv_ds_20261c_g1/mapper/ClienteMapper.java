package ar.edu.davinci.dv_ds_20261c_g1.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ar.edu.davinci.dv_ds_20261c_g1.controller.request.ClienteRequest;
import ar.edu.davinci.dv_ds_20261c_g1.controller.response.ClienteResponse;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    Cliente toEntity(ClienteRequest request);

    @Mapping(target = "razonSocial", expression = "java(cliente.getRazonSocial())")
    ClienteResponse toResponse(Cliente cliente);

    List<ClienteResponse> toResponseList(List<Cliente> clientes);
}
