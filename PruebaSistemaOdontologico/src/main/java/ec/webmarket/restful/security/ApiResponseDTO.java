package ec.webmarket.restful.security;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseDTO<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean success;

	@NotNull
	private T result;


}