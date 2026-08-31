package cl.duoc.dsy2207.bff.controller;

import cl.duoc.dsy2207.bff.service.UsuarioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<String> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> buscar(
            @PathVariable Long id) {

        return usuarioService.buscar(id);
    }

    @PostMapping
    public ResponseEntity<String> crear(
            @RequestBody String body) {

        return usuarioService.crear(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(
            @PathVariable Long id,
            @RequestBody String body) {

        return usuarioService.actualizar(id, body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @PathVariable Long id) {

        return usuarioService.eliminar(id);
    }
}