package ec.webmarket.restful.domain;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class Horario {

   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id_horario;

    @ManyToOne
    @JoinColumn(name = "id_odontologo", nullable = false)
    private Odontologo odontologo;

    @Column(updatable = true, nullable = false)
    private LocalDate fecha;

    @Column(updatable = true, nullable = false)
    private LocalTime horaInicio;

    @Column(updatable = true, nullable = false)
    private LocalTime horaFin;

    @Column(updatable = true, nullable = false)
    private Boolean disponible;

}
