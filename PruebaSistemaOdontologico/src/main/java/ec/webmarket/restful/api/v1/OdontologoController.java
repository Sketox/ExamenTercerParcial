package ec.webmarket.restful.api.v1;

import java.util.List;
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
import ec.webmarket.restful.domain.Odontologo;
import ec.webmarket.restful.domain.Usuario;
import ec.webmarket.restful.dto.v1.CitaDTO;
import ec.webmarket.restful.dto.v1.OdontologoDTO;
import ec.webmarket.restful.security.ApiResponseDTO;
import ec.webmarket.restful.service.crud.CitaService;
import ec.webmarket.restful.service.crud.OdontologoService;
import ec.webmarket.restful.service.crud.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = { ApiConstants.URI_API_V1_ODONTOLOGO})
public class OdontologoController {

	@Autowired
	private OdontologoService entityService;
	
	@Autowired
	private UsuarioService entityServiceUsuario;
	
	@Autowired
	private CitaService entityServiceCita;
	

	@GetMapping
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.findAll(new OdontologoDTO())), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody OdontologoDTO dto) {
	    try {
	        // Log para depuración
	        System.out.println("DTO recibido: " + dto);
	        System.out.println("ID de usuario en DTO: " + dto.getId_usuario());

	        // Verificación del ID de usuario
	        if (dto.getId_usuario() == null || dto.getId_usuario().getId_usuario() == null) {
	            return new ResponseEntity<>(new ApiResponseDTO<>(false, "El ID de usuario no debe ser nulo"), HttpStatus.BAD_REQUEST);
	        }

	        // Buscar el usuario en la base de datos
	        Optional<Usuario> usuarioOpt = entityServiceUsuario.findById(dto.getId_usuario().getId_usuario());

	        if (usuarioOpt.isEmpty()) {
	            return new ResponseEntity<>(new ApiResponseDTO<>(false, "El usuario no existe"), HttpStatus.BAD_REQUEST);
	        }

	        Usuario usuario = usuarioOpt.get();

	        // Verificar que el usuario sea de tipo "odontólogo"
	        if (!"odontologo".equals(usuario.getTipoUsuario())) {
	            return new ResponseEntity<>(new ApiResponseDTO<>(false, "El usuario debe ser de tipo 'odontólogo'"), HttpStatus.BAD_REQUEST);
	        }
	        
	        dto.setId_usuario(usuario);

	        // Crear el odontólogo
	        OdontologoDTO createdDto = entityService.create(dto);
	        return new ResponseEntity<>(new ApiResponseDTO<>(true, createdDto), HttpStatus.CREATED);
	        
	    } catch (Exception e) {
	        return new ResponseEntity<>(new ApiResponseDTO<>(false, "Error al crear el odontólogo: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody OdontologoDTO dto) {
		return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.update(dto)), HttpStatus.OK);
	}

	@GetMapping("/{id}/archivo/id")
	public ResponseEntity<?> getById(@Valid @PathVariable Long id) {
	    if (id == null) {
	        return new ResponseEntity<>(new ApiResponseDTO<>(false, "El ID no debe ser nulo"), HttpStatus.BAD_REQUEST);
	    }
	    OdontologoDTO dto = new OdontologoDTO();
	    dto.setId_odontologo(id);
	    return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.find(dto)), HttpStatus.OK);
	}
	
	

	
	@DeleteMapping("/{id}/archivo/id")
	public ResponseEntity<?> deleteById(@Valid @PathVariable Long id) {
	    OdontologoDTO dto = new OdontologoDTO();
	    dto.setId_odontologo(id);
	    // Llamar al servicio para eliminar el cliente
	    entityService.delete(dto);
	    return new ResponseEntity<>(new ApiResponseDTO<Void>(true, null), HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/cedula/{cedula}")
	public ResponseEntity<?> getOdontolofoPorCedula(@PathVariable Long cedula) {
	    Optional<Odontologo> odontologo = entityService.findByCedula(cedula);
	    if (odontologo.isPresent()) {
	        return new ResponseEntity<>(new ApiResponseDTO<>(true, odontologo.get()), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(new ApiResponseDTO<>(false, "No se encontró ningún cliente con la cédula proporcionada"), HttpStatus.NOT_FOUND
	        );
	    }
	}
	
	
    @GetMapping("/odontologo/{id}")
    public ResponseEntity<?> getCitasByOdontologo(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(new ApiResponseDTO<>(false, "El ID del odontólogo no debe ser nulo"), HttpStatus.BAD_REQUEST);
        }

        List<CitaDTO> citas = entityServiceCita.obtenerCitasPorOdontologo(id);

        if (citas.isEmpty()) {
            return new ResponseEntity<>(new ApiResponseDTO<>(false, "No se encontraron citas para este odontólogo"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ApiResponseDTO<>(true, citas), HttpStatus.OK);
    }

}