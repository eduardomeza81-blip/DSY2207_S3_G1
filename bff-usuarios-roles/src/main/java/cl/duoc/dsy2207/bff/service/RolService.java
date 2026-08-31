package cl.duoc.dsy2207.bff.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

@Service
public class RolService {

    private final RestClient restClient;

    @Value("${roles.function.url}")
    private String rolesUrl;

    public RolService() {
        this.restClient = RestClient.create();
    }

    public ResponseEntity<String> listar() {

        try {
            return restClient
                    .get()
                    .uri(rolesUrl)
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
                    .uri(rolesUrl + "/" + id)
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
                    .uri(rolesUrl)
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
                    .uri(rolesUrl + "/" + id)
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
                    .uri(rolesUrl + "/" + id)
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