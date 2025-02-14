package ec.webmarket.restful.service.crud;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.webmarket.restful.domain.Paciente;
import ec.webmarket.restful.dto.v1.PacienteDTO;
import ec.webmarket.restful.persistence.PacienteRepository;
import ec.webmarket.restful.service.GenericCrudServiceImpl;

@Service
public class PacienteService extends GenericCrudServiceImpl<Paciente, PacienteDTO> {

	@Autowired
	private PacienteRepository repository;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public Optional<Paciente> find(PacienteDTO dto) {
		return repository.findById(dto.getId_paciente());
	}

	public List<Paciente> findByNombre(String nombre) {
		return repository.findByNombre(nombre);
	}
	
	   public Optional<Paciente> findById(Long id) {
	        return repository.findById(id);
	    }

	@Override
	public void delete(PacienteDTO dto) {
		repository.deleteById(dto.getId_paciente());
	}
	
	@Override
	public Paciente mapToDomain(PacienteDTO dto) {
		return modelMapper.map(dto, Paciente.class);
	}

	@Override
	public PacienteDTO mapToDto(Paciente domain) {
		return modelMapper.map(domain, PacienteDTO.class);
	}
	
	public Optional<Paciente> findByCedula(Long cedula) {
	    return repository.findByCedula(cedula);
	}

}