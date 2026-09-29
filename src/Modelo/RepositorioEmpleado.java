package Modelo;

import java.util.ArrayList;
import java.util.HashMap;

public class RepositorioEmpleado {
    private final HashMap<String, EmpleadoBase> empleados = new HashMap<>();

    public boolean agregar(EmpleadoBase empleado) {

        if (empleados.containsKey(empleado.getCedula())) {
            return false;
        }

        empleados.put(empleado.getCedula(), empleado);
        return true;
    }

    public EmpleadoBase buscar(String cedula) {
        return empleados.get(cedula);
    }

    public boolean actualizar(EmpleadoBase empleado) {

        if (!empleados.containsKey(empleado.getCedula())) {
            return false;
        }

        empleados.put(empleado.getCedula(), empleado);
        return true;
    }

    public boolean eliminar(String cedula) {

        if (!empleados.containsKey(cedula)) {
            return false;
        }

        empleados.remove(cedula);
        return true;
    }

    public ArrayList<EmpleadoBase> listarTodos() {
        return new ArrayList<>(empleados.values());
    }
}