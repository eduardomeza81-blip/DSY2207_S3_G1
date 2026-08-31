package cl.duoc.dsy2207.model;

public class Usuario {

    private Long idUsuario;
    private String nombre;
    private String email;
    private Integer activo;
    private Long idRol;

    public Usuario() {
    }

    public Usuario(Long idUsuario, String nombre, String email,
                   Integer activo, Long idRol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.activo = activo;
        this.idRol = idRol;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }
}