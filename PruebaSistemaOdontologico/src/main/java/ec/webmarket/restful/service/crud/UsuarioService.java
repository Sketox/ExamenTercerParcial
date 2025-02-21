package ec.webmarket.restful.service.crud;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.webmarket.restful.domain.Usuario;
import ec.webmarket.restful.dto.v1.UsuarioDTO;
import ec.webmarket.restful.persistence.UsuarioRepository;
import ec.webmarket.restful.service.GenericCrudServiceImpl;

@Service
public class UsuarioService extends GenericCrudServiceImpl<Usuario, UsuarioDTO> {

	@Autowired
	private UsuarioRepository repository;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public Optional<Usuario> find(UsuarioDTO dto) {
		return repository.findById(dto.getId_usuario());
	}

	
   public Optional<Usuario> findById(Long id) {
        return repository.findById(id);
    }
   
   
   public Optional<UsuarioDTO> autenticarUsuario(String nombreUsuario, String clave) {
	    return repository.findByNombreUsuarioAndClave(nombreUsuario, clave)
	        .map(usuario -> {
	            UsuarioDTO usuarioDto = mapToDto(usuario);
	            usuarioDto.setClave(""); // Borra la clave en el DTO
	            return usuarioDto;
	        });
	}

   public UsuarioDTO asignarRol(Long id, String rol) throws Exception {
       Usuario usuario = repository.findById(id)
               .orElseThrow(() -> new Exception("Usuario no encontrado"));

       // Validar que el rol sea válido (paciente u odontólogo)
       if (!rol.equals("paciente") && !rol.equals("odontologo")) {
           throw new Exception("Rol no válido");
       }

       // Asignar el nuevo rol al usuario
       usuario.setTipoUsuario(rol);
       Usuario usuarioActualizado = repository.save(usuario);
       
       

       // Convertir la entidad actualizada a DTO
       	UsuarioDTO usuarioDTO = mapToDto(usuarioActualizado);

       return usuarioDTO;
   }
   
   public UsuarioDTO actualizarClave(Long id, String nuevaClave) throws Exception {
       Usuario usuario = repository.findById(id)
               .orElseThrow(() -> new Exception("Usuario no encontrado"));

       // Actualizar la clave del usuario
       usuario.setClave(nuevaClave); // Asegúrate de que el campo clave exista en la entidad Usuario
       Usuario usuarioActualizado = repository.save(usuario);

       // Convertir la entidad actualizada a DTO
       UsuarioDTO usuarioDTO = mapToDto(usuarioActualizado);
       
       return usuarioDTO;
   }
   
	@Override
	public void delete(UsuarioDTO dto) {
		repository.deleteById(dto.getId_usuario());
	}
	
	@Override
	public Usuario mapToDomain(UsuarioDTO dto) {
		return modelMapper.map(dto, Usuario.class);
	}

	@Override
	public UsuarioDTO mapToDto(Usuario domain) {
		return modelMapper.map(domain, UsuarioDTO.class);
	}
	
}