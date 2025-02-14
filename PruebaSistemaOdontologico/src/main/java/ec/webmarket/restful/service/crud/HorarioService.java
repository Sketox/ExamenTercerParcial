package ec.webmarket.restful.service.crud;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.webmarket.restful.common.ApiException;
import ec.webmarket.restful.domain.Horario;
import ec.webmarket.restful.domain.Odontologo;
import ec.webmarket.restful.dto.v1.HorarioDTO;
import ec.webmarket.restful.dto.v1.OdontologoDTO;
import ec.webmarket.restful.persistence.HorarioRepository;
import ec.webmarket.restful.persistence.OdontologoRepository;
import ec.webmarket.restful.service.GenericCrudServiceImpl;

@Service
public class HorarioService extends GenericCrudServiceImpl<Horario, HorarioDTO> {

	@Autowired
	private HorarioRepository repository;
	
	@Autowired
	private OdontologoRepository odontologoRepository;

	

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public Optional<Horario> find(HorarioDTO dto) {
		return repository.findById(dto.getId_horario());
	}

	public List<Horario> findByOdontologo(Long cedula) {
	    return repository.findByOdontologo_Cedula(cedula)
	        .orElseThrow(() -> new ApiException("Odontólogo no encontrado"));
	}
	

    public Optional<Horario> findById(Long id) {
        return repository.findById(id);
    }


	@Override
	public Horario mapToDomain(HorarioDTO dto) {
		return modelMapper.map(dto, Horario.class);
	}

	@Override
	public HorarioDTO mapToDto(Horario domain) {
	    System.out.println("📌 Iniciando mapeo de Horario a HorarioDTO...");

	    if (domain == null) {
	        System.out.println("❌ El objeto Horario es nulo. Retornando null.");
	        return null;
	    }

	    System.out.println("🔹 Fecha: " + domain.getFecha());
	    System.out.println("🔹 Hora de inicio: " + domain.getHoraInicio());
	    System.out.println("🔹 Hora de fin: " + domain.getHoraFin());
	    System.out.println("🔹 Disponible: " + domain.getDisponible());

	    // Convertir el Horario a HorarioDTO usando ModelMapper
	    HorarioDTO dto = modelMapper.map(domain, HorarioDTO.class);

	    if (dto == null) {
	        System.out.println("❌ El objeto HorarioDTO resultante es nulo.");
	        return null;
	    }

	    System.out.println("✅ Mapeo inicial con ModelMapper completado.");

	    // Verificar que el odontólogo no sea nulo y mapearlo explícitamente
	    if (domain.getOdontologo() != null) {
	        System.out.println("🔹 Odontólogo asignado encontrado. Mapeando...");

	        OdontologoDTO odontologoDTO = mapToDto(domain.getOdontologo()); // Llamar al mapToDto para Odontologo
	        dto.setOdontologoAsignadoHorario(odontologoDTO);

	        System.out.println("✅ Mapeo de odontólogo completado. ID Odontólogo: " + odontologoDTO.getId_odontologo());
	    } else {
	        System.out.println("⚠️ No hay odontólogo asignado.");
	    }

	    System.out.println("🎯 Mapeo finalizado. Retornando HorarioDTO.");
	    return dto;
	}


	public OdontologoDTO mapToDto(Odontologo odontologo) {
	    return modelMapper.map(odontologo, OdontologoDTO.class);
	}
	


	public Optional<Horario> obtenerHorario(Long odontologoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
	    Odontologo odontologo = odontologoRepository.findById(odontologoId)
	            .orElseThrow(() -> new ApiException("Odontologo no encontrado"));
	    return repository.findByOdontologoAndFechaAndHoraInicioAndHoraFin(odontologo, fecha, horaInicio, horaFin);

	}
	
	public List<Horario> findByOdontologoAndFecha(Long odontologoId, LocalDate fecha) {
	    Odontologo odontologo = odontologoRepository.findById(odontologoId)
	        .orElseThrow(() -> new ApiException("Odontólogo no encontrado"));
	    return repository.findByOdontologoAndFecha(odontologo, fecha);
	}
	
	
   public List<Horario> obtenerHorariosDisponibles(Boolean disponible) {
        return repository.findByDisponible(disponible);
    }
   
   public List<Horario> findHorariosByFecha (LocalDate fecha){
	   return repository.findHorariosByFecha(fecha);
   }
   

	
}
