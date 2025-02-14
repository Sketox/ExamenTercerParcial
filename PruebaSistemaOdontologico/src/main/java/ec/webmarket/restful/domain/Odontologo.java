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
public class Odontologo {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id_odontologo;
    
    @Column(updatable = true, nullable = false, unique = true)
    private Long cedula;
    
    @Column(updatable = true, length = 50, nullable = false)
    private String nombre;
    
    @Column(updatable = true, length = 50, nullable = false)
    private String apellido;
    
    @Column(updatable = true, length = 50, nullable = false)
    private String especialidad;
    
    
    @Column(updatable = true, nullable = true, unique = false)
    private String telefono;
    
    
    @Column(updatable = true, length = 255, nullable = true) 
    private String email;
}
