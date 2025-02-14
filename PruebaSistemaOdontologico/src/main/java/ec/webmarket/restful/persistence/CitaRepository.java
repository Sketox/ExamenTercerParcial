package ec.webmarket.restful.persistence;


import org.springframework.data.jpa.repository.JpaRepository;

import ec.webmarket.restful.domain.Cita;

public interface CitaRepository extends JpaRepository<Cita, Long> {

}
