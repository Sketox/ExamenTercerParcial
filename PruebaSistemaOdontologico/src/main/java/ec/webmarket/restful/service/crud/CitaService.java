package ec.webmarket.restful.service.crud;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.webmarket.restful.domain.Cita;
import ec.webmarket.restful.dto.v1.CitaDTO;

import ec.webmarket.restful.persistence.CitaRepository;
import ec.webmarket.restful.service.GenericCrudServiceImpl;

@Service
public class CitaService extends GenericCrudServiceImpl<Cita, CitaDTO> {

	@Autowired
	private CitaRepository repository;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public Optional<Cita> find(CitaDTO dto) {
		return repository.findById(dto.getId_cita());
	}

	@Override
	public Cita mapToDomain(CitaDTO dto) {
		return modelMapper.map(dto, Cita.class);
	}

	@Override
	public CitaDTO mapToDto(Cita domain) {
		return modelMapper.map(domain, CitaDTO.class);
	}
}