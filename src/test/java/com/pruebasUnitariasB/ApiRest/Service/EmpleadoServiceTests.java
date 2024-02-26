package com.pruebasUnitariasB.ApiRest.Service;

import com.pruebasUnitariasB.ApiRest.entity.Empleado;
import com.pruebasUnitariasB.ApiRest.exception.ResourceNotFoundException;
import com.pruebasUnitariasB.ApiRest.repository.EmpleadoRepository;
import com.pruebasUnitariasB.ApiRest.service.Impl.EmpleadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

//Esta anotacion sirve para indicarle que vamos a trabajar con mockito, asi como para indicarle que vamos a trabajar
//con unas extensiones de JUnit5
@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTests {

    //Con esta anotacion le indicamos que estamos obteniendo nuestro objeto simulado
    //@Mock se utiliza para crear un objeto simulado (mock) de la clase EmpleadoRepository
    //Un mock es un objeto simulado que imita el comportamiento de un objeto real.
    // En este caso, empleadoRepository es un mock de EmpleadoRepository.
    @Mock
    private EmpleadoRepository empleadoRepository;

    //empleadoServiceImpl es una instancia de EmpleadoServiceImpl, y con @InjectMocks, Mockito intentará inyectar
    // automáticamente los mocks necesarios (en este caso, empleadoRepository) en los campos de esta clase.
    //@InjectMocks se utiliza para inyectar (o insertar) automáticamente mocks(los mocks son -> empleadoRepository)
    // anotados en la clase que se está probando, en este caso, EmpleadoServiceImpl
    @InjectMocks
    private EmpleadoServiceImpl empleadoServiceImpl;

    private Empleado empleado;
    @BeforeEach
    void setup(){
        empleado = Empleado.builder()
                .id(1L)
                .nombre("Andrea")
                .apellido("Ramirez")
                .email("andrea@gmail.com")
                .build();
    }

    @Test
    @DisplayName("Test para guardar un empleado")
    void TestGuardarEmpleado(){
        //given - preconfiguracion
        //.willReturn(Optional.empty()) indica que cuando se llame al método findByEmail con el correo electrónico
        // del empleado, el mock debe devolver un Optional vacío (Optional.empty()).

        //al proporcionar un Optional.empty() en la configuración, se está simulando la ausencia de un empleado con el
        // mismo correo electrónico en la base de datos, permitiendo probar cómo reacciona el código en esta condición.

        //En este caso, al devolver un Optional.empty(), estás indicando que no se encontró ningún empleado con ese
        // correo electrónico, y tu servicio debería proceder a guardar el nuevo empleado.
        given(empleadoRepository.findByEmail(empleado.getEmail())).willReturn(Optional.empty());
        //caso contrario si no existe me retornara el empleado
        // Cuando se llama al método save con el objeto empleado, Mockito devuelve el mismo objeto empleado en lugar
        // de realizar la operación de guardado real en la base de datos.
        given(empleadoRepository.save(empleado)).willReturn(empleado);



        //when
        //n este caso específico de prueba, el método guardarEmpleado del servicio se comportará como si estuviera
        // guardando un nuevo empleado en el repositorio,
        Empleado empleado1 = empleadoServiceImpl.guardarEmpleado(empleado);

        //then
        assertThat(empleado1).isNotNull();
    }


    //En este segundo test, estás probando cómo se comporta tu servicio (empleadoServiceImpl) cuando intentas guardar
    // un empleado, pero el repositorio ya tiene un empleado con el mismo correo electrónico.
    @Test
    @DisplayName("Test para guardar un empleado con Throw Exception")
    void TestGuardarEmpleadoConThrowException(){
        //given - preconfiguracion
        //Aquí, estás configurando el comportamiento del mock del repositorio. Estás diciendo que, cuando se llame al
        // método findByEmail del repositorio con el correo electrónico del empleado ("andrea@gmail.com" en este caso),
        // el repositorio debe devolver un Optional que contiene el mismo empleado que ya está en el repositorio.
        given(empleadoRepository.findByEmail(empleado.getEmail())).willReturn(Optional.of(empleado));


        //when
        //Estás ejecutando el método guardarEmpleado del servicio (empleadoServiceImpl) pasando el mismo empleado
        // (empleado) que ya está configurado en el repositorio.
        //estás verificando que al intentar guardar el mismo empleado que ya existe en el repositorio, se lanza una
        // excepción de tipo ResourceNotFoundException. Esto significa que tu servicio está diseñado para lanzar esta
        // excepción cuando intenta guardar un empleado que ya existe en la base de datos.
        assertThrows(ResourceNotFoundException.class, ()->{
            empleadoServiceImpl.guardarEmpleado(empleado);
        });

        //then
        //Estás verificando que el método save del repositorio nunca fue llamado. Esto tiene sentido, ya que tu
        // servicio debería lanzar una excepción y no intentar guardar el empleado nuevamente si ya existe en la base de datos.
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }

    @Test
    @DisplayName("Test para listar a los empleados")
    void testListarEmpleados(){
        //given
        Empleado empleado1 = Empleado.builder()
                .id(1L)
                .nombre("Andres")
                .apellido("Perez")
                .email("andres@gmail.com")
                .build();
        given(empleadoRepository.findAll()).willReturn(List.of(empleado, empleado1));

        //when
        List<Empleado> empleados = empleadoServiceImpl.getAllEmpleados();

        //then
        assertThat(empleados).isNotNull();
        assertThat(empleados.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test para retornar una lista vacia")
    void testListarColleccionEmpleadosVacia(){
        //given
        Empleado empleado1 = Empleado.builder()
                .id(1L)
                .nombre("Andres")
                .apellido("Perez")
                .email("andres@gmail.com")
                .build();
        given(empleadoRepository.findAll()).willReturn(Collections.emptyList());

        //when
        List<Empleado> empleadoList = empleadoServiceImpl.getAllEmpleados();

        //then
        assertThat(empleadoList).isEmpty();
        assertThat(empleadoList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Test para obtener un empleado por Id")
    void testObtenerEmpleadoPorId(){
        //given
        given(empleadoRepository.findById(1L)).willReturn(Optional.of(empleado));

        //when
        Empleado empleado1 = empleadoServiceImpl.getEmpleadoById(empleado.getId()).get();

        //then
        assertThat(empleado1).isNotNull();
    }

    @Test
    @DisplayName("Test para actualizar un empleado")
    void testActualizarEmpleado(){
        //given
        given(empleadoRepository.save(empleado)).willReturn(empleado);
        empleado.setEmail("gabriel9528@gmail.com");
        empleado.setNombre("Gabo");

        //when
        Empleado empleado1 = empleadoServiceImpl.updateEmpleado(empleado);

        //then
        assertThat(empleado1.getEmail()).isEqualTo("gabriel9528@gmail.com");
        assertThat(empleado1.getNombre()).isEqualTo("Gabo");
    }

    @Test
    @DisplayName("Test para eliminar un empleado")
    void testEliminarEmpleado(){
        //given
        //WillDoNothing significa que no retornara nada simplemente se elminara
        long empleadoId = 1L;
        willDoNothing().given(empleadoRepository).deleteById(empleadoId);

        //when
        empleadoServiceImpl.deleteEmpleado(empleadoId);

        //then
        verify(empleadoRepository, times(1)).deleteById(empleadoId);
    }

}
