package ec.webmarket.restful.api.v1;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ec.webmarket.restful.common.ApiConstants;
import ec.webmarket.restful.domain.Horario;
import ec.webmarket.restful.domain.Odontologo;
import ec.webmarket.restful.dto.v1.HorarioDTO;
import ec.webmarket.restful.dto.v1.OdontologoDTO;
import ec.webmarket.restful.security.ApiResponseDTO;
import ec.webmarket.restful.service.crud.HorarioService;
import ec.webmarket.restful.service.crud.OdontologoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = { ApiConstants.URI_API_V1_HORARIO })
public class HorarioController {

	@Autowired
	private HorarioService entityService;
	
	@Autowired
	private OdontologoService entityServiceOdontologo;

	@GetMapping
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.findAll(new HorarioDTO())),
				HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody HorarioDTO dto) {
	    try {
	        // Validar ID del odontólogo
	        if (dto.getOdontologoAsignadoHorario().getId_odontologo() == null) {
	            return new ResponseEntity<>(new ApiResponseDTO<>(false, "Error: El ID del odontólogo no puede ser nulo"), HttpStatus.BAD_REQUEST);
	        }

	        // Buscar el odontólogo
	        Optional<Odontologo> optionalOdontologo = entityServiceOdontologo.find(dto.getOdontologoAsignadoHorario());
	        if (optionalOdontologo.isEmpty()) {
	            return new ResponseEntity<>(new ApiResponseDTO<>(false, "Error: Odontólogo no encontrado"), HttpStatus.BAD_REQUEST);
	        }

	        // Validar fecha y horas
	        if (dto.getFecha().isBefore(LocalDate.now())) {
	            return new ResponseEntity<>(new ApiResponseDTO<>(false, "Error: La fecha no puede ser en el pasado"), HttpStatus.BAD_REQUEST);
	        }

	        if (dto.getHoraInicio().isAfter(dto.getHoraFin()) || dto.getHoraInicio().equals(dto.getHoraFin())) {
	            return new ResponseEntity<>(new ApiResponseDTO<>(false, "Error: La hora de inicio debe ser anterior a la hora de fin"), HttpStatus.BAD_REQUEST);
	        }

	        // Verificar que el odontólogo no tenga un horario superpuesto en la misma fecha
	        List<Horario> existingHorarios = entityService.findByOdontologoAndFecha(
	            optionalOdontologo.get().getId_odontologo(), dto.getFecha()
	        );

	        for (Horario horario : existingHorarios) {
	            if (!(dto.getHoraFin().isBefore(horario.getHoraInicio()) || dto.getHoraInicio().isAfter(horario.getHoraFin()))) {
	                return new ResponseEntity<>(new ApiResponseDTO<>(false, "Error: El horario se superpone con otro existente"), HttpStatus.BAD_REQUEST);
	            }
	        }

	        // Obtener el odontólogo del Optional
	        Odontologo odontologo = optionalOdontologo.get();

	        // Convertir Odontologo a OdontologoDTO usando mapToDto
	        OdontologoDTO odontologoDTO = entityServiceOdontologo.mapToDto(odontologo);

	        // Asignar el OdontologoDTO al HorarioDTO
	        dto.setOdontologoAsignadoHorario(odontologoDTO);

	        // Crear el horario
	        HorarioDTO createdDto = entityService.create(dto);
	        createdDto.setOdontologoAsignadoHorario(odontologoDTO);
	        

	        return new ResponseEntity<>(new ApiResponseDTO<>(true, createdDto), HttpStatus.CREATED);

	    } catch (Exception e) {
	        return new ResponseEntity<>(new ApiResponseDTO<>(false, "Error al crear el horario: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}



	@PutMapping
	public ResponseEntity<?> update(@RequestBody HorarioDTO dto) {
		return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.update(dto)), HttpStatus.OK);
	}

	@GetMapping("/{id}/archivo/id")
	public ResponseEntity<?> getById(@Valid @PathVariable Long id) {
		HorarioDTO dto = new HorarioDTO();
		dto.setId_horario(id);
		return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.find(dto)), HttpStatus.OK);
	}

	@GetMapping("/{cedula}/archivo")
	public ResponseEntity<?> getHorarioByOdontologo(@PathVariable Long cedula) {
	    return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.findByOdontologo(cedula)), HttpStatus.OK);
	}
	

    // Método para obtener horarios filtrados por disponibilidad
    @GetMapping("/disponibles")
    public ResponseEntity<?> obtenerHorarios(@RequestParam("disponible") Boolean disponible) {
        List<Horario> horarios = entityService.obtenerHorariosDisponibles(disponible);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, horarios));
    }
    
    @GetMapping("/{fecha}/archivo/")
    public ResponseEntity<?> getHorariosByFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Horario> horarios = entityService.findHorariosByFecha(fecha);

        if (horarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "No hay horarios disponibles para la fecha seleccionada"));
        }

        return ResponseEntity.ok(new ApiResponseDTO<>(true, horarios));
    }




	   

	
}