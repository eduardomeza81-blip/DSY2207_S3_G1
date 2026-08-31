package cl.duoc.dsy2207.bff.controller;

import cl.duoc.dsy2207.bff.service.RolService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public ResponseEntity<String> listar() {
        return rolService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> buscar(
            @PathVariable Long id) {

        return rolService.buscar(id);
    }

    @PostMapping
    public ResponseEntity<String> crear(
            @RequestBody String body) {

        return rolService.crear(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(
            @PathVariable Long id,
            @RequestBody String body) {

        return rolService.actualizar(id, body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @PathVariable Long id) {

        return rolService.eliminar(id);
    }
}