package Vista;
import Controlador.EmpleadoControlador;
import Modelo.EmpleadoAdministrativo;
import Modelo.EmpleadoBase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class VentanaEmpleado extends JFrame {

    private final EmpleadoControlador controlador;

    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtSalario;
    private JTextField txtBonificacion;

    private JComboBox<String> comboTipo;

    private JButton btnAgregar;
    private JButton btnBuscar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnHistorial;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JLabel lblResumen;

    public VentanaEmpleado(
            EmpleadoControlador controlador) {

        this.controlador = controlador;

        setTitle("Talento Humano");
        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        construirInterfaz();

        setSize(780, 540);
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {

        setLayout(
                new BorderLayout(10, 10)
        );

        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(5, 2, 5, 5)
                );

        panelFormulario.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del empleado"
                )
        );

        txtCedula = new JTextField();
        txtNombre = new JTextField();
        txtSalario = new JTextField();
        txtBonificacion = new JTextField();

        comboTipo = new JComboBox<>(
                EmpleadoControlador.TIPOS_EMPLEADO
        );

        panelFormulario.add(
                new JLabel("Cédula:")
        );
        panelFormulario.add(txtCedula);

        panelFormulario.add(
                new JLabel("Nombre:")
        );
        panelFormulario.add(txtNombre);

        panelFormulario.add(
                new JLabel("Tipo:")
        );
        panelFormulario.add(comboTipo);

        panelFormulario.add(
                new JLabel("Salario base:")
        );
        panelFormulario.add(txtSalario);

        panelFormulario.add(
                new JLabel("Bonificación:")
        );
        panelFormulario.add(txtBonificacion);

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout()
                );

        btnAgregar = new JButton("Agregar");
        btnBuscar = new JButton("Buscar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnHistorial = new JButton("Historial");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnHistorial);

        JPanel panelSuperior =
                new JPanel(
                        new BorderLayout()
                );

        panelSuperior.add(
                panelFormulario,
                BorderLayout.CENTER
        );

        panelSuperior.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        add(
                panelSuperior,
                BorderLayout.NORTH
        );

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "Cédula",
                                "Nombre",
                                "Tipo",
                                "Salario base",
                                "Salario total"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int fila,
                            int columna) {

                        return false;
                    }
                };

        tabla = new JTable(modeloTabla);

        add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        lblResumen =
                new JLabel();

        add(
                lblResumen,
                BorderLayout.SOUTH
        );

        txtBonificacion.setEnabled(false);

        agregarEventos();

        actualizarTabla();
    }

    private void agregarEventos() {

        comboTipo.addActionListener(e -> {

            String tipo =
                    comboTipo
                            .getSelectedItem()
                            .toString();

            boolean administrativo =
                    tipo.equals("Administrativo");

            txtBonificacion.setEnabled(
                    administrativo
            );

            if (!administrativo) {
                txtBonificacion.setText("");
            }
        });

        btnAgregar.addActionListener(
                e -> agregar()
        );

        btnBuscar.addActionListener(
                e -> buscar()
        );

        btnActualizar.addActionListener(
                e -> actualizar()
        );

        btnEliminar.addActionListener(
                e -> eliminar()
        );

        btnLimpiar.addActionListener(
                e -> limpiar()
        );

        btnHistorial.addActionListener(
                e -> mostrarHistorial()
        );
    }

    private void agregar() {

        String resultado =
                controlador.agregarEmpleado(
                        txtCedula.getText(),
                        txtNombre.getText(),
                        comboTipo.getSelectedItem().toString(),
                        txtSalario.getText(),
                        txtBonificacion.getText()
                );

        JOptionPane.showMessageDialog(
                this,
                resultado
        );

        if (resultado.equals(
                "Empleado agregado correctamente."
        )) {

            actualizarTabla();
            limpiar();
        }
    }

    private void buscar() {

        EmpleadoBase empleado =
                controlador.buscarEmpleado(
                        txtCedula.getText()
                );

        if (empleado == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Empleado no encontrado."
            );

            return;
        }

        txtNombre.setText(
                empleado.getNombre()
        );

        txtSalario.setText(
                String.valueOf(
                        empleado.getSalarioBase()
                )
        );

        comboTipo.setSelectedItem(
                empleado.getTipo()
        );

        if (empleado instanceof
                EmpleadoAdministrativo) {

            EmpleadoAdministrativo administrativo =
                    (EmpleadoAdministrativo) empleado;

            txtBonificacion.setEnabled(true);

            txtBonificacion.setText(
                    String.valueOf(
                            administrativo
                                    .getBonificacion()
                    )
            );

        } else {

            txtBonificacion.setText("");
            txtBonificacion.setEnabled(false);
        }
    }

    private void actualizar() {

        String resultado =
                controlador.actualizarEmpleado(
                        txtCedula.getText(),
                        txtNombre.getText(),
                        comboTipo.getSelectedItem().toString(),
                        txtSalario.getText(),
                        txtBonificacion.getText()
                );

        JOptionPane.showMessageDialog(
                this,
                resultado
        );

        if (resultado.equals(
                "Empleado actualizado correctamente."
        )) {

            actualizarTabla();
            limpiar();
        }
    }

    private void eliminar() {

        String cedula =
                txtCedula.getText();

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta ==
                JOptionPane.YES_OPTION) {

            String resultado =
                    controlador.eliminarEmpleado(
                            cedula
                    );

            JOptionPane.showMessageDialog(
                    this,
                    resultado
            );

            actualizarTabla();
            limpiar();
        }
    }

    private void limpiar() {

        txtCedula.setText("");
        txtNombre.setText("");
        txtSalario.setText("");
        txtBonificacion.setText("");

        comboTipo.setSelectedIndex(0);

        txtBonificacion.setEnabled(false);
    }

    private void mostrarHistorial() {

        ArrayList<String> historial =
                controlador.obtenerHistorial();

        StringBuilder texto =
                new StringBuilder();

        for (String registro : historial) {

            texto.append(registro)
                    .append("\n");
        }

        JTextArea area =
                new JTextArea(
                        texto.toString()
                );

        area.setEditable(false);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(area),
                "Historial",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        ArrayList<EmpleadoBase> empleados =
                controlador.obtenerEmpleados();

        for (EmpleadoBase empleado :
                empleados) {

            modeloTabla.addRow(
                    new Object[]{
                            empleado.getCedula(),
                            empleado.getNombre(),
                            empleado.getTipo(),
                            formatoPesos(
                                    empleado.getSalarioBase()
                            ),
                            formatoPesos(
                                    empleado.calcularSalarioTotal()
                            )
                    }
            );
        }

        lblResumen.setText(
                "Empleados: "
                        + empleados.size()
                        + " | Nómina total: "
                        + formatoPesos(
                        controlador.calcularTotalNomina()
                )
        );
    }

    private String formatoPesos(double valor) {

        NumberFormat formato =
                NumberFormat.getCurrencyInstance(
                        new Locale("es", "CO")
                );

        formato.setMaximumFractionDigits(0);

        return formato.format(valor);
    }
}