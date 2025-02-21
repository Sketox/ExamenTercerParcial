package ec.webmarket.restful.domain;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id_paciente;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario id_usuario;

    @Column(updatable = true, nullable = false, unique = true)
    private Long cedula;

    @Column(updatable = true, length = 50, nullable = false)
    private String nombre;

    @Column(updatable = true, length = 50, nullable = false)
    private String apellido;

    @Column(updatable = true, nullable = false)
    private LocalDate fechaNacimiento;

    @Column(updatable = true, nullable = true, unique = false)
    private String telefono;

    @Column(updatable = true, length = 100, nullable = true)
    private String direccion;

    @Column(updatable = true, length = 255, nullable = true)
    private String correo;
}