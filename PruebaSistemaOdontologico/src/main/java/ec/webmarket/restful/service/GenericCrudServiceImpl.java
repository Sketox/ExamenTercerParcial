package ec.webmarket.restful.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import ec.webmarket.restful.common.ApiException;

@Service
public abstract class GenericCrudServiceImpl<DOMAIN, DTO> implements GenericCrudService<DOMAIN, DTO> {

	@Autowired
	private JpaRepository<DOMAIN, Long> repository;

	@Override
	public DTO create(DTO dto) {
	    // Establecer el ID en null para permitir la generación automática
	    setIdToNull(dto);

	    DOMAIN domainObject = mapToDomain(dto);
	    DOMAIN domainObjectResult = repository.save(domainObject);
	    return mapToDto(domainObjectResult);
	}

	private void setIdToNull(DTO dto) {
	    try {
	        // Obtener todos los campos de la clase DTO
	        java.lang.reflect.Field[] fields = dto.getClass().getDeclaredFields();
	        
	        for (java.lang.reflect.Field field : fields) {
	            // Verificar si el campo está anotado con @Id
	            if (field.isAnnotationPresent(jakarta.persistence.Id.class)) {
	                field.setAccessible(true);
	                field.set(dto, null);
	                break;
	            }
	        }
	    } catch (IllegalAccessException e) {
	        // Manejar la excepción si no se puede acceder al campo
	        throw new ApiException("No se pudo establecer el ID en null");
	    }
	}

	@Override
	public DTO update(DTO dto) {
		Optional<DOMAIN> optional = find(dto);
		DOMAIN domainObjectResult = null;
		if (optional.isEmpty()) {
			throw new ApiException(String.format("Registro no encontrado"));
		} else {
			DOMAIN domainObject = mapToDomain(dto);
			domainObjectResult = repository.save(domainObject);
		}
		return mapToDto(domainObjectResult);
	}

	@Override
	public void delete(DTO dto) {
		Optional<DOMAIN> optional = find(dto);
		if (!optional.isPresent()) {
			throw new ApiException(String.format("Registro no encontrado"));
		} else {
			DOMAIN domainObject = mapToDomain(dto);
			repository.delete(domainObject);
		}
	}

	@Override
	public List<DTO> findAll(DTO dto) {
		DOMAIN domain = mapToDomain(dto);
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("id");
		List<DOMAIN> objList = repository.findAll(Example.of(domain, matcher));
		return objList.stream().map(obj -> mapToDto(obj)).collect(Collectors.toList());
	}

	@Override
	public abstract DOMAIN mapToDomain(DTO dto);

	@Override
	public abstract DTO mapToDto(DOMAIN domain);
}