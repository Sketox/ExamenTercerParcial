package ec.webmarket.restful.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.webmarket.restful.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Optional<Usuario> findByNombreUsuarioAndClave(String nombreUsuario, String clave);

}