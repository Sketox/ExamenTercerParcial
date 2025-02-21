package ec.webmarket.restful.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.webmarket.restful.domain.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

	List<Paciente> findByNombre(String nombre);
	
	Optional<Paciente> findByCedula(Long cedula);

}