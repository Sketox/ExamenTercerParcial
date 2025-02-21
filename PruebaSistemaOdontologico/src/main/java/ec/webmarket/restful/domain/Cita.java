package ec.webmarket.restful.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id_cita;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente pacienteAsignado;

    @ManyToOne
    @JoinColumn(name = "horario_id", nullable = false)
    private Horario horarioAsignado;

    @Column(nullable = false)
    private String estado; // Puede ser "pendiente", "confirmada", "cancelada", etc.

    @Column(nullable = false, length = 500)
    private String motivo; // Descripción del motivo de la cita
}