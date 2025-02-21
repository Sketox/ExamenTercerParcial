package ec.webmarket.restful.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.webmarket.restful.domain.Cita;
import ec.webmarket.restful.domain.Horario;
import ec.webmarket.restful.domain.Odontologo;
import ec.webmarket.restful.domain.Paciente;

public interface CitaRepository extends JpaRepository<Cita, Long> {
	
	Optional<Cita> findByHorarioAsignado(Horario horario);
	
	public List<Cita> findByPacienteAsignado(Paciente paciente);
	
    List<Cita> findByHorarioAsignado_Odontologo(Odontologo odontologo);


}