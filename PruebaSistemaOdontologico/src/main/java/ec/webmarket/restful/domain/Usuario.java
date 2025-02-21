package ec.webmarket.restful.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id_usuario;

    @Column(updatable = true, length = 50, nullable = false, unique = true)
    private String nombreUsuario;

    @Column(updatable = true, length = 100, nullable = false)
    private String clave;

    @Column(updatable = true, length = 20, nullable = false)
    private String tipoUsuario; // "odontologo" o "paciente"
}