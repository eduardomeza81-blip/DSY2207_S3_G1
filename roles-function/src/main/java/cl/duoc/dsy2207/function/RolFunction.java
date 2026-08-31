package cl.duoc.dsy2207.function;

import cl.duoc.dsy2207.model.Rol;
import cl.duoc.dsy2207.repository.RolRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.util.List;
import java.util.Optional;

public class RolFunction {

    private final RolRepository repository =
            new RolRepository();

    private final ObjectMapper mapper =
            new ObjectMapper();


    // =========================================================
    // GET - LISTAR TODOS LOS ROLES
    // =========================================================

    @FunctionName("listarRoles")
    public HttpResponseMessage listar(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "roles"
            )
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        try {

            List<Rol> roles = repository.listar();

            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(mapper.writeValueAsString(roles))
                    .build();

        } catch (Exception e) {

            context.getLogger().severe(
                    "Error listar roles: " + e.getMessage()
            );

            return error500(request, e);
        }
    }


    // =========================================================
    // GET - BUSCAR ROL POR ID
    // =========================================================

    @FunctionName("buscarRol")
    public HttpResponseMessage buscarPorId(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "roles/{id}"
            )
            HttpRequestMessage<Optional<String>> request,

            @BindingName("id")
            String id,

            final ExecutionContext context) {

        try {

            Rol rol =
                    repository.buscarPorId(
                            Long.parseLong(id)
                    );

            if (rol == null) {

                return request
                        .createResponseBuilder(
                                HttpStatus.NOT_FOUND
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .body(
                                "{\"mensaje\":\"Rol no encontrado\"}"
                        )
                        .build();
            }

            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .body(
                            mapper.writeValueAsString(rol)
                    )
                    .build();

        } catch (NumberFormatException e) {

            return request
                    .createResponseBuilder(
                            HttpStatus.BAD_REQUEST
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .body(
                            "{\"mensaje\":\"ID inválido\"}"
                    )
                    .build();

        } catch (Exception e) {

            context.getLogger().severe(
                    "Error buscar rol: " + e.getMessage()
            );

            return error500(request, e);
        }
    }


    // =========================================================
    // POST - CREAR ROL
    // =========================================================

    @FunctionName("crearRol")
    public HttpResponseMessage crear(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "roles"
            )
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        try {

            String body =
                    request.getBody().orElse("");

            Rol rol =
                    mapper.readValue(
                            body,
                            Rol.class
                    );

            if (
                    rol.getNombre() == null
                    || rol.getNombre().isBlank()
            ) {

                return request
                        .createResponseBuilder(
                                HttpStatus.BAD_REQUEST
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .body(
                                "{\"mensaje\":\"El nombre es obligatorio\"}"
                        )
                        .build();
            }

            Rol creado =
                    repository.crear(rol);

            return request
                    .createResponseBuilder(
                            HttpStatus.CREATED
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .body(
                            mapper.writeValueAsString(creado)
                    )
                    .build();

        } catch (Exception e) {

            context.getLogger().severe(
                    "Error crear rol: " + e.getMessage()
            );

            return error500(request, e);
        }
    }


    // =========================================================
    // PUT - ACTUALIZAR ROL
    // =========================================================

    @FunctionName("actualizarRol")
    public HttpResponseMessage actualizar(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.PUT},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "roles/{id}"
            )
            HttpRequestMessage<Optional<String>> request,

            @BindingName("id")
            String id,

            final ExecutionContext context) {

        try {

            Long idRol =
                    Long.parseLong(id);

            String body =
                    request.getBody().orElse("");

            Rol rol =
                    mapper.readValue(
                            body,
                            Rol.class
                    );

            if (
                    rol.getNombre() == null
                    || rol.getNombre().isBlank()
            ) {

                return request
                        .createResponseBuilder(
                                HttpStatus.BAD_REQUEST
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .body(
                                "{\"mensaje\":\"El nombre es obligatorio\"}"
                        )
                        .build();
            }

            boolean actualizado =
                    repository.actualizar(
                            idRol,
                            rol
                    );

            if (!actualizado) {

                return request
                        .createResponseBuilder(
                                HttpStatus.NOT_FOUND
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .body(
                                "{\"mensaje\":\"Rol no encontrado\"}"
                        )
                        .build();
            }

            Rol actualizadoRol =
                    repository.buscarPorId(idRol);

            return request
                    .createResponseBuilder(
                            HttpStatus.OK
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .body(
                            mapper.writeValueAsString(
                                    actualizadoRol
                            )
                    )
                    .build();

        } catch (NumberFormatException e) {

            return request
                    .createResponseBuilder(
                            HttpStatus.BAD_REQUEST
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .body(
                            "{\"mensaje\":\"ID inválido\"}"
                    )
                    .build();

        } catch (Exception e) {

            context.getLogger().severe(
                    "Error actualizar rol: "
                    + e.getMessage()
            );

            return error500(request, e);
        }
    }


    // =========================================================
    // DELETE - ELIMINAR ROL
    // =========================================================

    @FunctionName("eliminarRol")
    public HttpResponseMessage eliminar(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.DELETE},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "roles/{id}"
            )
            HttpRequestMessage<Optional<String>> request,

            @BindingName("id")
            String id,

            final ExecutionContext context) {

        try {

            Long idRol =
                    Long.parseLong(id);

            // 1. Validar que el rol exista
            Rol rol =
                    repository.buscarPorId(idRol);

            if (rol == null) {

                return request
                        .createResponseBuilder(
                                HttpStatus.NOT_FOUND
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .body(
                                "{\"mensaje\":\"Rol no encontrado\"}"
                        )
                        .build();
            }

            // 2. Validar si el rol tiene usuarios asociados
            if (repository.tieneUsuariosAsociados(idRol)) {

                return request
                        .createResponseBuilder(
                                HttpStatus.CONFLICT
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .body(
                                "{\"mensaje\":\"No se puede eliminar el rol porque tiene usuarios asociados\"}"
                        )
                        .build();
            }

            // 3. Eliminar el rol
            boolean eliminado =
                    repository.eliminar(idRol);

            if (!eliminado) {

                return request
                        .createResponseBuilder(
                                HttpStatus.NOT_FOUND
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .body(
                                "{\"mensaje\":\"Rol no encontrado\"}"
                        )
                        .build();
            }

            return request
                    .createResponseBuilder(
                            HttpStatus.OK
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .body(
                            "{\"mensaje\":\"Rol eliminado correctamente\"}"
                    )
                    .build();

        } catch (NumberFormatException e) {

            return request
                    .createResponseBuilder(
                            HttpStatus.BAD_REQUEST
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .body(
                            "{\"mensaje\":\"ID inválido\"}"
                    )
                    .build();

        } catch (Exception e) {

            context.getLogger().severe(
                    "Error eliminar rol: "
                    + e.getMessage()
            );

            return error500(request, e);
        }
    }


    // =========================================================
    // MANEJO GENERAL DE ERROR 500
    // =========================================================

    private HttpResponseMessage error500(
            HttpRequestMessage<?> request,
            Exception e) {

        String mensaje =
                e.getMessage() == null
                ? "Error interno"
                : e.getMessage().replace("\"", "'");

        return request
                .createResponseBuilder(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .header(
                        "Content-Type",
                        "application/json"
                )
                .body(
                        "{\"mensaje\":\""
                        + mensaje
                        + "\"}"
                )
                .build();
    }
}