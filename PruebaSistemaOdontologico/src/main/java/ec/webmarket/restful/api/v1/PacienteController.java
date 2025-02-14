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
import ec.webmarket.restful.domain.Paciente;
import ec.webmarket.restful.dto.v1.PacienteDTO;
import ec.webmarket.restful.security.ApiResponseDTO;
import ec.webmarket.restful.service.crud.PacienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = { ApiConstants.URI_API_V1_PACIENTE})
public class PacienteController {

	@Autowired
	private PacienteService entityService;

	@GetMapping
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.findAll(new PacienteDTO())), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody PacienteDTO dto) {
	    try {
	        PacienteDTO createdDto = entityService.create(dto);
	        return new ResponseEntity<>(new ApiResponseDTO<>(true, createdDto), HttpStatus.CREATED);
	    } catch (Exception e) {
	        return new ResponseEntity<>(new ApiResponseDTO<>(false, "Error al crear el paciente: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody PacienteDTO dto) {
		return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.update(dto)), HttpStatus.OK);
	}

	@GetMapping("/{id}/archivo/id")
	public ResponseEntity<?> getById(@Valid @PathVariable Long id) {
	    if (id == null) {
	        return new ResponseEntity<>(new ApiResponseDTO<>(false, "El ID no debe ser nulo"), HttpStatus.BAD_REQUEST);
	    }
	    PacienteDTO dto = new PacienteDTO();
	    dto.setId_paciente(id);
	    return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.find(dto)), HttpStatus.OK);
	}

	
	@DeleteMapping("/{id}/archivo/id")
	public ResponseEntity<?> deleteById(@Valid @PathVariable Long id) {
	    PacienteDTO dto = new PacienteDTO();
	    dto.setId_paciente(id);
	    // Llamar al servicio para eliminar el cliente
	    entityService.delete(dto);
	    return new ResponseEntity<>(new ApiResponseDTO<Void>(true, null), HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/cedula/{cedula}")
	public ResponseEntity<?> getClientePorCedula(@PathVariable Long cedula) {
	    Optional<Paciente> paciente = entityService.findByCedula(cedula);
	    if (paciente.isPresent()) {
	        return new ResponseEntity<>(new ApiResponseDTO<>(true, paciente.get()), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(new ApiResponseDTO<>(false, "No se encontró ningún cliente con la cédula proporcionada"), HttpStatus.NOT_FOUND
	        );
	    }
	}

}