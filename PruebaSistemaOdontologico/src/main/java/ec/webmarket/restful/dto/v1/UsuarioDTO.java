package ec.webmarket.restful.dto.v1;


import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id_usuario;
    private String nombreUsuario;
    private String clave;
    private String tipoUsuario;
}