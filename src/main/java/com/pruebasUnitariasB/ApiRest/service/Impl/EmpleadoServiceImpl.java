package com.pruebasUnitariasB.ApiRest.service.Impl;

import com.pruebasUnitariasB.ApiRest.entity.Empleado;
import com.pruebasUnitariasB.ApiRest.exception.ResourceNotFoundException;
import com.pruebasUnitariasB.ApiRest.repository.EmpleadoRepository;
import com.pruebasUnitariasB.ApiRest.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Override
    public Empleado guardarEmpleado(Empleado empleado) {
        Optional<Empleado> empleadoGuardado = empleadoRepository.findByEmail(empleado.getEmail());
        if(empleadoGuardado.isPresent()){
            throw new ResourceNotFoundException("El empleado con ese email ya existe: "+ empleado.getEmail());
        }
        return empleadoRepository.save(empleado);
    }

    @Override
    public List<Empleado> getAllEmpleados() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> getEmpleadoById(Long id) {
        return empleadoRepository.findById(id);
    }

    @Override
    public Empleado updateEmpleado(Empleado updateEmpleado) {
        return empleadoRepository.save(updateEmpleado);
    }

    @Override
    public void deleteEmpleado(Long id) {
        empleadoRepository.deleteById(id);

    }
}
