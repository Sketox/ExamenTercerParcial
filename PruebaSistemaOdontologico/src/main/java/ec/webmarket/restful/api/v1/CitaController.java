package ec.webmarket.restful.api.v1;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ec.webmarket.restful.common.ApiConstants;
import ec.webmarket.restful.domain.Cita;
import ec.webmarket.restful.domain.Horario;
import ec.webmarket.restful.domain.Paciente;
import ec.webmarket.restful.dto.v1.CitaDTO;
import ec.webmarket.restful.security.ApiResponseDTO;
import ec.webmarket.restful.service.crud.CitaService;
import ec.webmarket.restful.service.crud.HorarioService;
import ec.webmarket.restful.service.crud.PacienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = { ApiConstants.URI_API_V1_CITA })
public class CitaController {

	@Autowired
	private CitaService entityService;
	
	@Autowired
	private HorarioService entityServiceHorario;
	
	@Autowired
	private PacienteService entityServicePaciente;

	@GetMapping
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.findAll(new CitaDTO())), HttpStatus.OK);
	}

	 @PostMapping
	    public ResponseEntity<?> create(@Valid @RequestBody CitaDTO dto) {
	        try {
	            if (dto.getHorarioAsignado() == null || dto.getHorarioAsignado().getId_horario() == null) {
	                return new ResponseEntity<>(new ApiResponseDTO<>(false, "El horario asignado no puede ser nulo"), HttpStatus.BAD_REQUEST);
	            }

	            if (dto.getPacienteAsignado() == null || dto.getPacienteAsignado().getId_paciente() == null) {
	                return new ResponseEntity<>(new ApiResponseDTO<>(false, "El paciente no puede ser nulo"), HttpStatus.BAD_REQUEST);
	            }

	            // Guardar la cita con validaciones
	            CitaDTO createdDto = entityService.create(dto);

	            return new ResponseEntity<>(new ApiResponseDTO<>(true, createdDto), HttpStatus.CREATED);
	        } catch (Exception e) {
	            return new ResponseEntity<>(new ApiResponseDTO<>(false, "Error al crear la cita: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }



	@PutMapping
	public ResponseEntity<?> update(@RequestBody CitaDTO dto) {
		return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.update(dto)), HttpStatus.OK);
	}

	@GetMapping("/{id}/archivo/id")
	public ResponseEntity<?> getById(@Valid @PathVariable Long id) {
	    if (id == null) {
	        return new ResponseEntity<>(new ApiResponseDTO<>(false, "El ID no debe ser nulo"), HttpStatus.BAD_REQUEST);
	    }
	    CitaDTO dto = new CitaDTO();
	    dto.setId_cita(id);
	    return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.find(dto)), HttpStatus.OK);
	}
	 
	
	
	@DeleteMapping("/{id}/archivo/id")
	public ResponseEntity<?> deleteById(@Valid @PathVariable Long id) {
	   CitaDTO dto = new CitaDTO();
	    dto.setId_cita(id);
	    // Llamar al servicio para eliminar el producto
	    entityService.delete(dto);
	    return new ResponseEntity<>(new ApiResponseDTO<Void>(true, null), HttpStatus.NO_CONTENT);
	}

}