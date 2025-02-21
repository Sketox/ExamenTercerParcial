package ec.webmarket.restful.api.v1;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.webmarket.restful.common.ApiConstants;
import ec.webmarket.restful.dto.v1.UsuarioDTO;
import ec.webmarket.restful.security.ApiResponseDTO;
import ec.webmarket.restful.service.crud.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = { ApiConstants.URI_API_V1_USUARIO})
public class UsuarioController {

    @Autowired
    private UsuarioService entityService;

    // 1. Registro de nuevos usuarios
    @PostMapping
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            // Validar que el rol sea válido (paciente u odontólogo)
        	usuarioDTO.setTipoUsuario(usuarioDTO.getTipoUsuario().toLowerCase());
            if (!usuarioDTO.getTipoUsuario().equals("paciente") && !usuarioDTO.getTipoUsuario().equals("odontologo")) {
                return new ResponseEntity<>(new ApiResponseDTO<>(false, "Rol no válido"), HttpStatus.BAD_REQUEST);
            }

            // Registrar el usuario con el rol asignado
            UsuarioDTO nuevoUsuario = entityService.create(usuarioDTO);
            return new ResponseEntity<>(new ApiResponseDTO<>(true, nuevoUsuario), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseDTO<>(false, "Error al registrar el usuario: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. Autenticación y gestión de credenciales
    @PostMapping("/autenticar")
    public ResponseEntity<?> autenticarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Optional<UsuarioDTO> usuarioAutenticado = entityService.autenticarUsuario(usuarioDTO.getNombreUsuario(), usuarioDTO.getClave());

        if (usuarioAutenticado.isPresent()) {  // Verifica si el Optional tiene un valor
            return ResponseEntity.ok(new ApiResponseDTO<>(true, usuarioAutenticado.get()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO<>(false, "Usuario o contraseña incorrectos"));
        }
    }



    // 3. Asignación de roles (paciente u odontólogo)
    @PutMapping("/{id}/asignar-rol")
    public ResponseEntity<?> asignarRol(@PathVariable Long id, @RequestParam String rol) {
        try {
            // Validar que el rol sea válido (paciente u odontólogo)
            if (!rol.equals("paciente") && !rol.equals("odontologo")) {
                return new ResponseEntity<>(new ApiResponseDTO<>(false, "Rol no válido"), HttpStatus.BAD_REQUEST);
            }

            // Asignar el nuevo rol al usuario
            UsuarioDTO usuarioActualizado = entityService.asignarRol(id, rol);
            return new ResponseEntity<>(new ApiResponseDTO<>(true, usuarioActualizado), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseDTO<>(false, "Error al asignar el rol: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // 4. Recuperación y actualización de contraseñas
    @PutMapping("/{id}/actualizar-clave")
    public ResponseEntity<?> actualizarClave(@PathVariable Long id, @RequestParam String nuevaClave) {
        try {
            // Actualizar la clave del usuario
            UsuarioDTO usuarioActualizado = entityService.actualizarClave(id, nuevaClave);
            return new ResponseEntity<>(new ApiResponseDTO<>(true, usuarioActualizado), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseDTO<>(false, "Error al actualizar la clave: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    
    // 5. Visualizar todos los Usuarios
	@GetMapping
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(new ApiResponseDTO<>(true, entityService.findAll(new UsuarioDTO())), HttpStatus.OK);
	}
}