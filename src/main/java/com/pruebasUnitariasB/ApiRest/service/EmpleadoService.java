package com.pruebasUnitariasB.ApiRest.service;

import com.pruebasUnitariasB.ApiRest.entity.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {
    Empleado guardarEmpleado(Empleado empleado);
    List<Empleado> getAllEmpleados();
    Optional<Empleado> getEmpleadoById(Long id);
    Empleado updateEmpleado(Empleado updateEmpleado);
    void deleteEmpleado(Long id);
}
