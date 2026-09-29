package Controlador;

import Modelo.EmpleadoAdministrativo;
import Modelo.EmpleadoBase;
import Modelo.RepositorioEmpleado;

import java.util.ArrayList;

public class EmpleadoControlador {
    public static final String[] TIPOS_EMPLEADO = {
            "Operativo",
            "Administrativo"
    };

    private final RepositorioEmpleado repositorio;
    private final ArrayList<String> historial;

    public EmpleadoControlador() {

        repositorio = new RepositorioEmpleado();
        historial = new ArrayList<>();

        cargarDatosPrueba();
    }

    private void cargarDatosPrueba() {

        agregarEmpleado(
                "1001",
                "Ana Torres",
                "Operativo",
                "1800000",
                ""
        );

        agregarEmpleado(
                "1002",
                "Luis Gómez",
                "Administrativo",
                "2500000",
                "300000"
        );

        agregarEmpleado(
                "1003",
                "Marta Ríos",
                "Operativo",
                "1750000",
                ""
        );

        agregarEmpleado(
                "1004",
                "Pedro Cano",
                "Administrativo",
                "3200000",
                "300000"
        );
    }

    public boolean esNumeroValido(String texto) {

        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }

        int puntos = 0;

        for (int i = 0; i < texto.length(); i++) {

            char caracter = texto.charAt(i);

            if (caracter == '.') {
                puntos++;

                if (puntos > 1) {
                    return false;
                }

            } else if (!Character.isDigit(caracter)) {
                return false;
            }
        }

        return true;
    }

    public String validar(
            String cedula,
            String nombre,
            String tipo,
            String salario,
            String bonificacion) {

        if (cedula == null || cedula.trim().isEmpty()) {
            return "La cédula es obligatoria.";
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre es obligatorio.";
        }

        if (!esNumeroValido(salario)) {
            return "El salario no es válido.";
        }

        if (Double.parseDouble(salario) < 0) {
            return "El salario no puede ser negativo.";
        }

        if (tipo.equals("Administrativo")) {

            if (!esNumeroValido(bonificacion)) {
                return "La bonificación no es válida.";
            }

            if (Double.parseDouble(bonificacion) < 0) {
                return "La bonificación no puede ser negativa.";
            }
        }

        return "";
    }

    private EmpleadoBase construirEmpleado(
            String cedula,
            String nombre,
            String tipo,
            String salario,
            String bonificacion) {

        double salarioNumero =
                Double.parseDouble(salario);

        if (tipo.equals("Administrativo")) {

            double bonificacionNumero =
                    Double.parseDouble(bonificacion);

            return new EmpleadoAdministrativo(
                    cedula,
                    nombre,
                    salarioNumero,
                    bonificacionNumero
            );
        }

        return new EmpleadoBase(
                cedula,
                nombre,
                salarioNumero
        );
    }

    public String agregarEmpleado(
            String cedula,
            String nombre,
            String tipo,
            String salario,
            String bonificacion) {

        String error = validar(
                cedula,
                nombre,
                tipo,
                salario,
                bonificacion
        );

        if (!error.isEmpty()) {
            return error;
        }

        if (repositorio.buscar(cedula) != null) {
            return "Ya existe un empleado con esa cédula.";
        }

        EmpleadoBase empleado =
                construirEmpleado(
                        cedula,
                        nombre,
                        tipo,
                        salario,
                        bonificacion
                );

        repositorio.agregar(empleado);

        historial.add(
                "Empleado agregado: " + cedula
        );

        return "Empleado agregado correctamente.";
    }

    public EmpleadoBase buscarEmpleado(String cedula) {

        EmpleadoBase empleado =
                repositorio.buscar(cedula);

        if (empleado != null) {
            historial.add(
                    "Empleado consultado: " + cedula
            );
        }

        return empleado;
    }

    public String actualizarEmpleado(
            String cedula,
            String nombre,
            String tipo,
            String salario,
            String bonificacion) {

        String error = validar(
                cedula,
                nombre,
                tipo,
                salario,
                bonificacion
        );

        if (!error.isEmpty()) {
            return error;
        }

        if (repositorio.buscar(cedula) == null) {
            return "No existe un empleado con esa cédula.";
        }

        EmpleadoBase empleado =
                construirEmpleado(
                        cedula,
                        nombre,
                        tipo,
                        salario,
                        bonificacion
                );

        repositorio.actualizar(empleado);

        historial.add(
                "Empleado actualizado: " + cedula
        );

        return "Empleado actualizado correctamente.";
    }

    public String eliminarEmpleado(String cedula) {

        if (cedula == null ||
                cedula.trim().isEmpty()) {

            return "Digite una cédula.";
        }

        if (!repositorio.eliminar(cedula)) {
            return "No existe un empleado con esa cédula.";
        }

        historial.add(
                "Empleado eliminado: " + cedula
        );

        return "Empleado eliminado correctamente.";
    }

    public ArrayList<EmpleadoBase> obtenerEmpleados() {
        return repositorio.listarTodos();
    }

    public double calcularTotalNomina() {

        double total = 0;

        for (EmpleadoBase empleado :
                obtenerEmpleados()) {

            total += empleado.calcularSalarioTotal();
        }

        return total;
    }

    public ArrayList<String> obtenerHistorial() {
        return new ArrayList<>(historial);
    }
}