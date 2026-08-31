package cl.duoc.dsy2207.bff.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

@Service
public class UsuarioService {

    private final RestClient restClient;

    @Value("${usuarios.function.url}")
    private String usuariosUrl;

    public UsuarioService() {
        this.restClient = RestClient.create();
    }

    public ResponseEntity<String> listar() {

        try {
            return restClient
                    .get()
                    .uri(usuariosUrl)
                    .retrieve()
                    .toEntity(String.class);

        } catch (HttpStatusCodeException e) {
            return construirRespuestaError(e);
        }
    }

    public ResponseEntity<String> buscar(Long id) {

        try {
            return restClient
                    .get()
                    .uri(usuariosUrl + "/" + id)
                    .retrieve()
                    .toEntity(String.class);

        } catch (HttpStatusCodeException e) {
            return construirRespuestaError(e);
        }
    }

    public ResponseEntity<String> crear(String body) {

        try {
            return restClient
                    .post()
                    .uri(usuariosUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toEntity(String.class);

        } catch (HttpStatusCodeException e) {
            return construirRespuestaError(e);
        }
    }

    public ResponseEntity<String> actualizar(Long id, String body) {

        try {
            return restClient
                    .put()
                    .uri(usuariosUrl + "/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toEntity(String.class);

        } catch (HttpStatusCodeException e) {
            return construirRespuestaError(e);
        }
    }

    public ResponseEntity<String> eliminar(Long id) {

        try {
            return restClient
                    .delete()
                    .uri(usuariosUrl + "/" + id)
                    .retrieve()
                    .toEntity(String.class);

        } catch (HttpStatusCodeException e) {
            return construirRespuestaError(e);
        }
    }

    private ResponseEntity<String> construirRespuestaError(
            HttpStatusCodeException e) {

        return ResponseEntity
                .status(e.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.getResponseBodyAsString());
    }
}