package com.pruebasUnitariasB.ApiRest.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebasUnitariasB.ApiRest.entity.Empleado;
import com.pruebasUnitariasB.ApiRest.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static  org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.StatusResultMatchers.*;

//Sirve para realizar pruebas especificamente para los controladores
//se utiliza para realizar pruebas específicas de controladores en el contexto de Spring MVC.
//@WebMvcTest proporciona un entorno de prueba ligero y enfocado en el controlador.
@WebMvcTest
public class EmpleadoControllerTests {

    //MockMvc es un componente de Spring Test que proporciona una interfaz para realizar solicitudes HTTP y evaluar
    // las respuestas sin necesidad de un servidor real.
    @Autowired
    private MockMvc mockMvc;

    //@MockBean se utiliza para crear un mock (simulacro) de la interfaz EmpleadoService y lo inserta en el contexto de Spring.
    //Esto es beneficioso porque permite que el controlador utilice este mock en lugar de la implementación real de
    // EmpleadoService durante las pruebas, facilitando el aislamiento y la simulación de comportamientos.
    //estás utilizando @MockBean para crear un mock del EmpleadoService y reemplazar el bean real del servicio en el
    // contexto de la aplicación. Esto se hace para aislar el controlador durante las pruebas y evitar que interactúe
    // con implementaciones reales del servicio y del repositorio.
    //En resumen, @Mock y @InjectMocks son de Mockito y se utilizan principalmente en pruebas de unidades para simular
    // dependencias y crear instancias de clases bajo prueba. @MockBean es de Spring Boot y se utiliza en pruebas de
    // integración para reemplazar beans reales en el contexto de la aplicación,

    @MockBean
    private EmpleadoService empleadoService;

    //ObjectMapper es una clase de Jackson que convierte objetos entre Java y formatos de datos como JSON.
    @Autowired
    private ObjectMapper objectMapper;

    //La razón principal para utilizar throws Exception en este contexto es permitir que el método de prueba propague
    // cualquier excepción no controlada que pueda ocurrir durante la ejecución del test.
    @Test
    void testGuardarEmpleado() throws Exception {
        //given
        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("Andrea")
                .apellido("Ramirez")
                .email("andrea@gmail.com")
                .build();
        //Indica que cuando se llame al método guardarEmpleado con cualquier instancia de Empleado como argumento,
        // se aplicará la lógica definida a continuación.- (invocation) -> invocation.getArgument(0): Esto define la
        // lógica personalizada. La interfaz funcional Answer de Mockito toma un objeto invocation que representa la
        // llamada al método. Donde invocation.getArgument(0) devuelve el primer argumento pasado al método en la llamada,
        // en este caso, el objeto Empleado. Entonces, la lógica establece que el método guardarEmpleado devolverá el
        // mismo objeto Empleado que se le pasó como argumento.
        given(empleadoService.guardarEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when
        //Se realiza una solicitud HTTP simulada al endpoint /api/empleados mediante mockMvc.perform(...).
        // Se está utilizando el método HTTP POST y se proporciona el contenido del empleado en formato JSON.
        ResultActions resultActions = mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado)));

        //then
        //jsonPath("$.nombre", is(empleado.getNombre())): Verifica que el campo "nombre" en la respuesta JSON
        // coincida con el nombre del empleado proporcionado.
        //jsonPath("$.apellido", is(empleado.getApellido())): Verifica que el campo "apellido" en la respuesta JSON
        // coincida con el apellido del empleado proporcionado.
        //jsonPath("$.email", is(empleado.getEmail())): Verifica que el campo "email" en la respuesta JSON
        // coincida con el email del empleado proporcionado.
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleado.getEmail())));
    }

    @Test
    void testListarEmpleados() throws Exception{
        //given
        List<Empleado> empleadoList = new ArrayList<>();
        empleadoList.add(Empleado.builder().nombre("Persona1").apellido("Apellido1").email("persona1@gmail.com").build());
        empleadoList.add(Empleado.builder().nombre("Persona2").apellido("Apellido2").email("persona2@gmail.com").build());
        empleadoList.add(Empleado.builder().nombre("Persona3").apellido("Apellido3").email("persona3@gmail.com").build());
        empleadoList.add(Empleado.builder().nombre("Persona4").apellido("Apellido4").email("persona4@gmail.com").build());
        empleadoList.add(Empleado.builder().nombre("Persona5").apellido("Apellido5").email("persona5@gmail.com").build());
        given(empleadoService.getAllEmpleados()).willReturn(empleadoList);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/empleados"));

        //then
        //JsonPath se utiliza para extraer y verificar valores específicos de una respuesta JSON generada por una aplicación web.
        //verifica que el tamaño de la lista en la respuesta JSON coincide con el tamaño de la lista que has preparado
        // en la sección given.
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(empleadoList.size())));
    }

    @Test
    void testObtenerEmpleadoPorId() throws Exception{
        //given
        long empleadoId = 1L;
        Empleado empleado = Empleado.builder()
                .nombre("Andrea")
                .apellido("Ramirez")
                .email("andrea@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(empleado));

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/empleados/{id}", empleadoId));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleado.getEmail())));
    }

    @Test
    void testObtenerEmpleadoNoEncontrado() throws Exception{
        //given
        long empleadoId = 1L;
        Empleado empleado = Empleado.builder()
                .nombre("Andrea")
                .apellido("Ramirez")
                .email("andrea@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/empleados/{id}", empleadoId));

        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    void testActualizarEmpleado() throws Exception{
        //given
        long empleadoId = 1L;
        Empleado empleadoGuardado = Empleado.builder()
                .nombre("Andrea")
                .apellido("Ramirez")
                .email("andrea@gmail.com")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Flor")
                .apellido("Perez")
                .email("flor@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(empleadoGuardado));
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        ResultActions resultActions = mockMvc.perform(put("/api/empleados/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre", is(empleadoActualizado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleadoActualizado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleadoActualizado.getEmail())));
    }

    @Test
    void testActualizarEmpleadoNoEncontrado() throws Exception{
        //given
        long empleadoId = 1L;
        Empleado empleadoGuardado = Empleado.builder()
                .nombre("Andrea")
                .apellido("Ramirez")
                .email("andrea@gmail.com")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Flor")
                .apellido("Perez")
                .email("flor@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        ResultActions resultActions = mockMvc.perform(put("/api/empleados/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
    }


    //este test está configurado para simular el escenario en el que la eliminación de un empleado con el ID
    // proporcionado se realiza con éxito y no arroja ninguna excepción o error. Por lo tanto, es correcto que
    // pase correctamente incluso si el empleado no existe con ese ID, ya que en este caso simplemente no hace nada
    // y no se espera ninguna respuesta específica.
    //Esto es típicamente lo que quieres hacer en un test para simular el escenario en el que la eliminación
    // se realiza con éxito y no hay errores.
    @Test
    void testEliminarEmpleado() throws Exception{
        //given
        long empleadoId = 1L;
        willDoNothing().given(empleadoService).deleteEmpleado(empleadoId);

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/empleados/{id}", empleadoId));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

}
