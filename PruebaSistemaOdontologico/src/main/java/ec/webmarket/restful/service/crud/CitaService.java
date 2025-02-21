package ec.webmarket.restful.service.crud;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.webmarket.restful.domain.Cita;
import ec.webmarket.restful.domain.Horario;
import ec.webmarket.restful.domain.Odontologo;
import ec.webmarket.restful.domain.Paciente;
import ec.webmarket.restful.dto.v1.CitaDTO;
import ec.webmarket.restful.persistence.CitaRepository;
import ec.webmarket.restful.persistence.HorarioRepository;
import ec.webmarket.restful.persistence.OdontologoRepository;
import ec.webmarket.restful.persistence.PacienteRepository;
import ec.webmarket.restful.service.GenericCrudServiceImpl;

@Service
public class CitaService extends GenericCrudServiceImpl<Cita, CitaDTO> {

	@Autowired
	private CitaRepository repository;
	
    @Autowired
	private PacienteRepository pacienteRepository;

    @Autowired
    private HorarioRepository horarioRepository;
    
    @Autowired
    private OdontologoRepository odontologoRepository;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public Optional<Cita> find(CitaDTO dto) {
		return repository.findById(dto.getId_cita());
	}
	
	@Override
	public CitaDTO create(CitaDTO dto) {
	    // Buscar el paciente en la base de datos
	    Paciente paciente = pacienteRepository.findById(dto.getPacienteAsignado().getId_paciente())
	        .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

	    // Buscar el horario en la base de datos
	    Horario horario = horarioRepository.findById(dto.getHorarioAsignado().getId_horario())
	        .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
	    
	    // Buscar si ya existe una cita con el mismo horario asignado
	    Optional<Cita> citaAsignada = repository.findByHorarioAsignado(horario);
	    
	    // Si ya existe una cita con ese horario, lanzamos una excepción
	    if (citaAsignada.isPresent()) {
	        throw new RuntimeException("El horario ya está asignado a otra cita");
	    }

	    // Crear la entidad Cita con los objetos obtenidos
	    Cita nuevaCita = new Cita();
	    nuevaCita.setEstado(dto.getEstado());
	    nuevaCita.setMotivo(dto.getMotivo());
	    nuevaCita.setPacienteAsignado(paciente);
	    nuevaCita.setHorarioAsignado(horario);

	    // Guardar la cita en la base de datos
	    nuevaCita = repository.save(nuevaCita);

	    // Mapear la entidad Cita a CitaDTO
	    CitaDTO createdDto = mapToDto(nuevaCita);

	    // Retornar el DTO con la cita creada
	    return createdDto;
	}
	
	
	public List<CitaDTO> obtenerCitasPorPaciente(Long idPaciente) {
	    // Buscar al paciente en la base de datos
	    Paciente paciente = pacienteRepository.findById(idPaciente)
	        .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

	    // Obtener todas las citas asignadas al paciente
	    List<Cita> citas = repository.findByPacienteAsignado(paciente);

	    // Mapear las citas a DTOs
	    List<CitaDTO> citasDTO = citas.stream()
	        .map(this::mapToDto)
	        .collect(Collectors.toList());

	    return citasDTO;
	}
	
    public List<CitaDTO> obtenerCitasPorOdontologo(Long idOdontologo) {
        // Buscar el odontólogo en la base de datos
        Odontologo odontologo = odontologoRepository.findById(idOdontologo)
            .orElseThrow(() -> new RuntimeException("Odontólogo no encontrado"));

        // Obtener todas las citas del odontólogo usando la relación HorarioAsignado
        List<Cita> citas = repository.findByHorarioAsignado_Odontologo(odontologo);

        // Mapear las citas a DTOs y devolver la lista
        return citas.stream().map(this::mapToDto).collect(Collectors.toList());
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