package cl.duoc.dsy2207.function;

import cl.duoc.dsy2207.model.Usuario;
import cl.duoc.dsy2207.repository.UsuarioRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.util.List;
import java.util.Optional;

public class UsuarioFunction {

    private final UsuarioRepository repository =
            new UsuarioRepository();

    private final ObjectMapper mapper =
            new ObjectMapper();


    // =====================================================
    // GET /api/usuarios
    // =====================================================

    @FunctionName("listarUsuarios")
    public HttpResponseMessage listar(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "usuarios"
            )
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        try {

            List<Usuario> usuarios = repository.listar();

            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(mapper.writeValueAsString(usuarios))
                    .build();

        } catch (Exception e) {

            context.getLogger().severe(
                    "Error listar usuarios: " + e.getMessage()
            );

            return error500(request, e);
        }
    }


    // =====================================================
    // GET /api/usuarios/{id}
    // =====================================================

    @FunctionName("buscarUsuario")
    public HttpResponseMessage buscar(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "usuarios/{id}"
            )
            HttpRequestMessage<Optional<String>> request,

            @BindingName("id") String id,

            final ExecutionContext context) {

        try {

            Long idUsuario = Long.parseLong(id);

            Usuario usuario =
                    repository.buscarPorId(idUsuario);

            if (usuario == null) {

                return request
                        .createResponseBuilder(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json")
                        .body(
                                "{\"mensaje\":\"Usuario no encontrado\"}"
                        )
                        .build();
            }

            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(mapper.writeValueAsString(usuario))
                    .build();

        } catch (NumberFormatException e) {

            return request
                    .createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json")
                    .body("{\"mensaje\":\"ID inválido\"}")
                    .build();

        } catch (Exception e) {

            context.getLogger().severe(
                    "Error buscar usuario: " + e.getMessage()
            );

            return error500(request, e);
        }
    }


    // =====================================================
    // POST /api/usuarios
    // =====================================================

    @FunctionName("crearUsuario")
    public HttpResponseMessage crear(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "usuarios"
            )
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        try {

            String body = request.getBody().orElse("");

            Usuario usuario =
                    mapper.readValue(body, Usuario.class);

            String validacion = validarUsuario(usuario);

            if (validacion != null) {
                return badRequest(request, validacion);
            }

            // Validamos que el rol realmente exista
            if (!repository.existeRol(usuario.getIdRol())) {

                return badRequest(
                        request,
                        "El rol indicado no existe"
                );
            }

            Usuario creado =
                    repository.crear(usuario);

            return request
                    .createResponseBuilder(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(mapper.writeValueAsString(creado))
                    .build();

        } catch (Exception e) {

            context.getLogger().severe(
                    "Error crear usuario: " + e.getMessage()
            );

            return error500(request, e);
        }
    }


    // =====================================================
    // PUT /api/usuarios/{id}
    // =====================================================

    @FunctionName("actualizarUsuario")
    public HttpResponseMessage actualizar(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.PUT},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "usuarios/{id}"
            )
            HttpRequestMessage<Optional<String>> request,

            @BindingName("id") String id,

            final ExecutionContext context) {

        try {

            Long idUsuario = Long.parseLong(id);

            String body = request.getBody().orElse("");

            Usuario usuario =
                    mapper.readValue(body, Usuario.class);

            String validacion = validarUsuario(usuario);

            if (validacion != null) {
                return badRequest(request, validacion);
            }

            if (!repository.existeRol(usuario.getIdRol())) {

                return badRequest(
                        request,
                        "El rol indicado no existe"
                );
            }

            boolean actualizado =
                    repository.actualizar(
                            idUsuario,
                            usuario
                    );

            if (!actualizado) {

                return request
                        .createResponseBuilder(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json")
                        .body(
                                "{\"mensaje\":\"Usuario no encontrado\"}"
                        )
                        .build();
            }

            Usuario usuarioActualizado =
                    repository.buscarPorId(idUsuario);

            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(
                            mapper.writeValueAsString(
                                    usuarioActualizado
                            )
                    )
                    .build();

        } catch (NumberFormatException e) {

            return badRequest(
                    request,
                    "ID inválido"
            );

        } catch (Exception e) {

            context.getLogger().severe(
                    "Error actualizar usuario: "
                    + e.getMessage()
            );

            return error500(request, e);
        }
    }


    // =====================================================
    // DELETE /api/usuarios/{id}
    // =====================================================

    @FunctionName("eliminarUsuario")
    public HttpResponseMessage eliminar(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.DELETE},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "usuarios/{id}"
            )
            HttpRequestMessage<Optional<String>> request,

            @BindingName("id") String id,

            final ExecutionContext context) {

        try {

            Long idUsuario = Long.parseLong(id);

            boolean eliminado =
                    repository.eliminar(idUsuario);

            if (!eliminado) {

                return request
                        .createResponseBuilder(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json")
                        .body(
                                "{\"mensaje\":\"Usuario no encontrado\"}"
                        )
                        .build();
            }

            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(
                            "{\"mensaje\":\"Usuario eliminado correctamente\"}"
                    )
                    .build();

        } catch (NumberFormatException e) {

            return badRequest(
                    request,
                    "ID inválido"
            );

        } catch (Exception e) {

            context.getLogger().severe(
                    "Error eliminar usuario: "
                    + e.getMessage()
            );

            return error500(request, e);
        }
    }


    // =====================================================
    // VALIDACIONES
    // =====================================================

    private String validarUsuario(Usuario usuario) {

        if (usuario.getNombre() == null
                || usuario.getNombre().isBlank()) {

            return "El nombre es obligatorio";
        }

        if (usuario.getEmail() == null
                || usuario.getEmail().isBlank()) {

            return "El email es obligatorio";
        }

        if (!usuario.getEmail().contains("@")) {

            return "El email no es válido";
        }

        if (usuario.getActivo() == null
                || (usuario.getActivo() != 0
                && usuario.getActivo() != 1)) {

            return "Activo debe ser 0 o 1";
        }

        if (usuario.getIdRol() == null) {

            return "El rol es obligatorio";
        }

        return null;
    }


    private HttpResponseMessage badRequest(
            HttpRequestMessage<?> request,
            String mensaje) {

        return request
                .createResponseBuilder(HttpStatus.BAD_REQUEST)
                .header("Content-Type", "application/json")
                .body(
                        "{\"mensaje\":\""
                        + mensaje
                        + "\"}"
                )
                .build();
    }


    private HttpResponseMessage error500(
            HttpRequestMessage<?> request,
            Exception e) {

        String mensaje =
                e.getMessage() == null
                        ? "Error interno"
                        : e.getMessage()
                           .replace("\"", "'");

        return request
                .createResponseBuilder(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .header("Content-Type", "application/json")
                .body(
                        "{\"mensaje\":\""
                        + mensaje
                        + "\"}"
                )
                .build();
    }
}