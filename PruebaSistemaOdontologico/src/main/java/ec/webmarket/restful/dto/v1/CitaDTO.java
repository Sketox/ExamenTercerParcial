package ec.webmarket.restful.dto.v1;


import ec.webmarket.restful.domain.Horario;
import ec.webmarket.restful.domain.Paciente;
import lombok.Data;

@Data
public class CitaDTO {
	
    private Long id_cita = null;
    private Paciente pacienteAsignado;
    private Horario horarioAsignado;
    private String estado;
    private String motivo;
}

