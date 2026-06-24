package ar.edu.davinci.dv_ds_20261c_g1.documentation;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.davinci.dv_ds_20261c_g1.domain.Cliente;
import ar.edu.davinci.dv_ds_20261c_g1.domain.EstadoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.Prenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.TipoPrenda;
import ar.edu.davinci.dv_ds_20261c_g1.domain.VentaEfectivo;
import ar.edu.davinci.dv_ds_20261c_g1.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20261c_g1.repository.ClienteRepository;
import ar.edu.davinci.dv_ds_20261c_g1.repository.PrendaRepository;
import ar.edu.davinci.dv_ds_20261c_g1.service.StockService;
import ar.edu.davinci.dv_ds_20261c_g1.service.VentaService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class ApiDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PrendaRepository prendaRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private VentaService ventaService;

    private Long clienteId;
    private Long prendaId;
    private Long ventaId;

    @BeforeEach
    void setUp() throws BusinessException {
        Cliente cliente = clienteRepository.save(Objects.requireNonNull(Cliente.builder()
                .nombre("Maria")
                .apellido("Doc")
                .build()));

        Prenda prenda = prendaRepository.save(Objects.requireNonNull(Prenda.builder()
                .descripcion("Campera Doc")
                .precioBase(new BigDecimal("1500.00"))
                .tipoPrenda(TipoPrenda.CAMPERA)
                .estadoPrenda(EstadoPrenda.NUEVA)
                .build()));

        stockService.establecer(prenda, 2, 1);

        VentaEfectivo venta = Objects.requireNonNull(ventaService.saveEfectivo(VentaEfectivo.builder()
                .cliente(cliente)
                .items(Collections.emptyList())
                .build()));

        clienteId = Objects.requireNonNull(cliente.getId());
        prendaId = Objects.requireNonNull(prenda.getId());
        ventaId = Objects.requireNonNull(venta.getId());
    }

    @Test
    void documentCreateVentaEfectivo() throws Exception {
        String body = objectMapper.writeValueAsString(
                java.util.Map.of("clienteId", clienteId, "items", Collections.emptyList()));

        mockMvc.perform(Objects.requireNonNull(post("/api/ventas/efectivo")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(body))))
                .andExpect(status().isCreated())
                .andDo(Objects.requireNonNull(document("ventas/efectivo-create",
                        requestFields(
                                fieldWithPath("clienteId").description("ID del cliente"),
                                fieldWithPath("items").description("Items de la venta (puede ser vacio)")),
                        responseFields(
                                fieldWithPath("id").description("ID de la venta creada"),
                                fieldWithPath("tipo").description("Tipo de venta"),
                                fieldWithPath("fecha").description("Fecha de la venta"),
                                fieldWithPath("clienteId").description("ID del cliente"),
                                fieldWithPath("clienteRazonSocial").description("Nombre del cliente"),
                                fieldWithPath("items").description("Items de la venta"),
                                fieldWithPath("importeBruto").description("Importe bruto"),
                                fieldWithPath("total").description("Total de la venta")))));
    }

    @Test
    void documentAddItemStockInsuficiente() throws Exception {
        String body = objectMapper.writeValueAsString(
                java.util.Map.of("prendaId", prendaId, "cantidad", 5));

        mockMvc.perform(Objects.requireNonNull(post("/api/ventas/{ventaId}/items", ventaId)
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(body))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andDo(Objects.requireNonNull(document("ventas/add-item-stock-insuficiente",
                        pathParameters(
                                parameterWithName("ventaId").description("ID de la venta")),
                        requestFields(
                                fieldWithPath("prendaId").description("ID de la prenda"),
                                fieldWithPath("cantidad").description("Cantidad solicitada")),
                        responseFields(
                                fieldWithPath("error").description("Mensaje de error de negocio")))));
    }

    @Test
    void documentGetStock() throws Exception {
        mockMvc.perform(Objects.requireNonNull(get("/api/prendas/{prendaId}/stock", prendaId)))
                .andExpect(status().isOk())
                .andDo(Objects.requireNonNull(document("stock/get",
                        pathParameters(
                                parameterWithName("prendaId").description("ID de la prenda")),
                        responseFields(
                                fieldWithPath("prendaId").description("ID de la prenda"),
                                fieldWithPath("prendaDescripcion").description("Descripcion de la prenda"),
                                fieldWithPath("cantidad").description("Stock disponible"),
                                fieldWithPath("stockMinimo").description("Stock minimo configurado"),
                                fieldWithPath("bajoMinimo").description("Indica si el stock esta bajo el minimo")))));
    }
}
