package com.pruebasUnitariasB.ApiRest.repository;

import com.pruebasUnitariasB.ApiRest.entity.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


//Esta notacion nos sirve para probar componentes solo de la capa de persistencia, buscara solo las clases que tengan
//la notacion ENTITY y los repositorios de springData Jpa -> en este caso solo trabajaria con nuestra entidad "EMPLEADO"
//y a los repositorios, no realizara pruebas test a los servicios y a los controllers, solamente a la capa de repositorio(Jpa)
@DataJpaTest
//PRUEBA DE INTEGRACION
public class EmpleadoRepositoryTests {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Empleado empleado;

    //antes de cada metodo
    @BeforeEach
    void setup(){
        empleado = Empleado.builder()
                .nombre("Andrea")
                .apellido("Ramirez")
                .email("andrea@gmail.com")
                .build();
    }

    @Test
    @DisplayName("Test para guardar un empleado")
    void testGuardarEmpleado(){
        //trabajremos con la modalidad BDD: Behavior driver development, estrategia de desarrollo dirigido por comportamiento
        //TDD-> Se centra en la funcionalidad, en las pruebas unitarias, y BDD -> se centra en el comportamiento en este caso:
        //given - dado o condicion previa a la configuracion
        //when - accion o comportamiento que se va a probar
        //then - verificar la salida

        //given
        Empleado empleado1 = Empleado.builder()
                .nombre("Gabriel")
                .apellido("Retamozo")
                .email("gabriel@gmail.com")
                .build();

        //when
        Empleado empleado2 = empleadoRepository.save(empleado1);

        //then
        assertThat(empleado2).isNotNull();
        assertThat(empleado2.getId()).isGreaterThan(0);

    }
    @Test
    @DisplayName("Test para listar a los empleados")
    void testListarEmpleados(){
        //given
        Empleado empleado1 = Empleado.builder()
                .nombre("Fernando")
                .apellido("Vargas")
                .email("fernando@gmail.com")
                .build();

        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado);

        //when
        List<Empleado> listaEmpleados = empleadoRepository.findAll();

        //then
        assertThat(listaEmpleados).isNotNull();
        assertThat(listaEmpleados.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test para obtener un empleado por Id")
    void testObtenerEmpleadoPorId(){
        //given
        empleadoRepository.save(empleado);

        //when - comportamiento o accion que se va a probar
        Empleado empleado1 = empleadoRepository.findById(empleado.getId()).get();

        //then
        assertThat(empleado1).isNotNull();
    }

    @Test
    @DisplayName("Test para actualizar un empleado")
    void testActualizarEmpleado(){
        //given
        empleadoRepository.save(empleado);

        //when
        Empleado empleado1 = empleadoRepository.findById(empleado.getId()).get();
        empleado1.setEmail("pepe@gmail.com");
        empleado1.setNombre("Pepe");
        empleado1.setApellido("Gomez");
        Empleado empleadoUpdate = empleadoRepository.save(empleado1);

        //then
        assertThat(empleadoUpdate.getEmail()).isEqualTo("pepe@gmail.com");
        assertThat(empleadoUpdate.getNombre()).isEqualTo("Pepe");
    }

    @Test
    @DisplayName("Test para eliminar un empleado")
    void testEliminarEmpleado(){
        //given
        empleadoRepository.save(empleado);

        //when
        empleadoRepository.deleteById(empleado.getId());
        Optional<Empleado> empleadoOptional =empleadoRepository.findById(empleado.getId());

        //then
        assertThat(empleadoOptional).isEmpty();
    }
}
