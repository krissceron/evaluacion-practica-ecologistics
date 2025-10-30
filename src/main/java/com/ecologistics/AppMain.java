package com.ecologistics;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.main.Main;

import com.ecologistics.model.Envio;
import com.ecologistics.store.MemoryStore;

public class AppMain extends RouteBuilder {

  public static void main(String[] args) throws Exception {
    Main main = new Main();
    main.configure().addRoutesBuilder(new AppMain());
    main.run(args);
  }

  @Override
  public void configure() {

    // --- REST server + OpenAPI ---
    restConfiguration()
      .component("netty-http")
      .host("0.0.0.0")
      .port(8081)
      .enableCORS(true)
      .apiContextPath("/api-doc")
      .apiProperty("api.title", "API de Envíos EcoLogistics")
      .apiProperty("api.version", "1.0.0");

    // ---------- REST DSL: mapear a direct: ----------
    rest("/ping").produces("text/plain")
      .get().to("direct:ping");

    rest("/envios").description("Gestión de envíos").produces("application/json")
      .get().to("direct:listar")
      .post().consumes("application/json").to("direct:crear");

    rest("/envios/{id}").produces("application/json")
      .get().to("direct:obtener");

    // ---------- Implementación de rutas ----------
    from("direct:ping")
      .setBody(constant("pong"));

    from("direct:listar")
      .log("GET /envios")
      .setBody(e -> MemoryStore.all())
      .marshal().json();                 // serializa a JSON

    from("direct:obtener")
      .log("GET /envios/{id} id=${header.id}")
      .process(e -> {
        String id = e.getIn().getHeader("id", String.class);
        Envio env = MemoryStore.get(id);
        e.getMessage().setBody(env != null ? env : Map.of());
      })
      .marshal().json();

    from("direct:crear")
      .log("POST /envios body=${body}")
      .unmarshal().json(Envio.class)     // parsea JSON a POJO
      .process(e -> {
        Envio envio = e.getMessage().getBody(Envio.class);
        if (envio.getId() == null || envio.getId().isBlank()) {
          envio.setId(UUID.randomUUID().toString());
        }
        MemoryStore.add(envio);
        e.getMessage().setHeader("CamelHttpResponseCode", 201);
        e.getMessage().setBody(Map.of("mensaje","Envío registrado correctamente"));
      })
      .marshal().json();

    // ---------- Ingesta CSV inicial ----------
    CsvDataFormat csv = new CsvDataFormat();
    csv.setSkipHeaderRecord(true);
    csv.setUseMaps(true);

    from("file:input?fileName=envios.csv&noop=true")
      .routeId("file-transfer")
      .unmarshal(csv) // CSV -> List<Map<String,Object>>
      .process(e -> {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rows = e.getMessage().getBody(List.class);
        for (Map<String, Object> row : rows) {
          Envio en = new Envio();
          en.setId(Objects.toString(row.get("id_envio"), ""));
          en.setCliente(Objects.toString(row.get("cliente"), ""));
          en.setDireccion(Objects.toString(row.get("direccion"), ""));
          en.setEstado(Objects.toString(row.get("estado"), ""));
          if (!en.getId().isEmpty()) {
            MemoryStore.add(en);
          }
        }
      })
      .log("Archivo CSV cargado y datos en memoria.");
  }
}
