package com.pruebasUnitariasB.ApiRest.controller;

import com.pruebasUnitariasB.ApiRest.entity.Empleado;
import com.pruebasUnitariasB.ApiRest.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/empleados")
public class EmpleadoController {
    @Autowired
    private EmpleadoService empleadoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Empleado guardarEmpleado(@RequestBody Empleado empleado){
        return empleadoService.guardarEmpleado(empleado);
    }

    @GetMapping
    public List<Empleado> listEmpleados(){
        return empleadoService.getAllEmpleados();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable ("id") Long id){
        return empleadoService.getEmpleadoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable("id") Long id, @RequestBody Empleado updateEmpleado){
        return empleadoService.getEmpleadoById(id)
                .map(setEmpleado ->{
                    setEmpleado.setNombre(updateEmpleado.getNombre());
                    setEmpleado.setApellido(updateEmpleado.getApellido());
                    setEmpleado.setEmail(updateEmpleado.getEmail());

                    Empleado empleadoActualizado = empleadoService.updateEmpleado(setEmpleado);
                    return new ResponseEntity<Empleado>(empleadoActualizado, HttpStatus.OK);
                }).orElseGet(()->ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEmpleado(@PathVariable("id") Long id){
        empleadoService.deleteEmpleado(id);
        return new ResponseEntity<String>("Empleado eliminado correctamente: ", HttpStatus.OK);
    }
}
