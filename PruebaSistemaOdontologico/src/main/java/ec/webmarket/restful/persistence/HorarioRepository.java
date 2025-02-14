package ec.webmarket.restful.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.webmarket.restful.domain.Horario;
import ec.webmarket.restful.domain.Odontologo;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

	Optional<List<Horario>> findByOdontologo_Cedula(Long cedula);
	
	public Optional<Horario> findByOdontologoAndFechaAndHoraInicioAndHoraFin(Odontologo odontologo, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin);
	
	List<Horario> findByOdontologoAndFecha(Odontologo odontologo, LocalDate fecha);
	
	List<Horario> findByDisponible(Boolean disponible);
	
	List<Horario> findHorariosByFecha (LocalDate fecha);
		
}