package ec.webmarket.restful.dto.v1;


import ec.webmarket.restful.domain.Usuario;
import lombok.Data;

@Data
public class OdontologoDTO {
	
    private Long id_odontologo = null;
    private Usuario id_usuario;
    private Long cedula;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String telefono;
    private String email;
}