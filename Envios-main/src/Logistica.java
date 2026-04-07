package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Logistica extends JFrame {
    private JTextField txtCliente, txtCodigo, txtPeso, txtDistancia;
    private JComboBox<String> comboTipo;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private ArrayList<Envio> listaEnvios = new ArrayList<>();

    public Logistica() {
        // Configuración básica de la ventana
        setTitle("Operador Logístico - Gestión de Envíos");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10)); // Espaciado entre paneles

        // --- 1. BARRA DE HERRAMIENTAS (Iconos superiores) ---
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false); // Evita que se mueva la barra

        // Carga de iconos con validación para evitar que el programa falle
        JButton btnAdd = crearBotonConIcono("/imagenes/agregar.png","Agregar");
        JButton btnDel = crearBotonConIcono("/imagenes/eliminar.png", "Eliminar");
        JButton btnList = crearBotonConIcono("/imagenes/Listar.png","Listar");

        toolBar.add(btnAdd);
        toolBar.add(btnDel);
        toolBar.add(btnList);
        add(toolBar, BorderLayout.NORTH);

        // --- 2. PANEL DE FORMULARIO (Se adapta al tamaño) ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Envío"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Margen entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Número y Tipo
        gbc.gridx = 0; gbc.gridy = 0; panelFormulario.add(new JLabel("Número:"), gbc);
        gbc.gridx = 1; txtCodigo = new JTextField(15); panelFormulario.add(txtCodigo, gbc);
        gbc.gridx = 2; panelFormulario.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 3; comboTipo = new JComboBox<>(new String[]{"Terrestre", "Aéreo", "Marítimo"}); 
        panelFormulario.add(comboTipo, gbc);

        // Fila 1: Cliente
        gbc.gridx = 0; gbc.gridy = 1; panelFormulario.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; // Ocupa 3 columnas
        txtCliente = new JTextField(); panelFormulario.add(txtCliente, gbc);

        // Fila 2: Peso y Distancia
        gbc.gridwidth = 1; // Reset a 1 columna
        gbc.gridx = 0; gbc.gridy = 2; panelFormulario.add(new JLabel("Peso:"), gbc);
        gbc.gridx = 1; txtPeso = new JTextField(); panelFormulario.add(txtPeso, gbc);
        gbc.gridx = 2; panelFormulario.add(new JLabel("Distancia Km:"), gbc);
        gbc.gridx = 3; txtDistancia = new JTextField(); panelFormulario.add(txtDistancia, gbc);

        // Fila 3: Botones Guardar y Cancelar
        JPanel panelBotonesAccion = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotonesAccion.add(btnGuardar);
        panelBotonesAccion.add(btnCancelar);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        panelFormulario.add(panelBotonesAccion, gbc);

        // --- 3. TABLA (Ocupa el resto del espacio) ---
        String[] columnas = {"Tipo", "Código", "Cliente", "Peso", "Distancia", "Costo"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tabla);

        // Panel contenedor central para formulario + tabla
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.add(panelFormulario, BorderLayout.NORTH);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        
        add(panelCentral, BorderLayout.CENTER);

        // --- EVENTOS ---
        btnGuardar.addActionListener(e -> accionAgregar());
        btnAdd.addActionListener(e -> accionAgregar());
        btnDel.addActionListener(e -> accionEliminar());
        btnList.addActionListener(e -> actualizarTabla());
        btnCancelar.addActionListener(e -> limpiarCampos());
    }

    // Método para cargar iconos de forma segura
    private JButton crearBotonConIcono(String ruta, String textoAlt) {
        try {
            java.net.URL url = getClass().getResource(ruta);
            if (url != null) {
                return new JButton(new ImageIcon(url));
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar: " + ruta);
        }
        return new JButton(textoAlt); // Si falla la imagen, pone el texto
    }

    private void accionAgregar() {
        try {
            String cod = txtCodigo.getText();
            String cli = txtCliente.getText();
            double p = Double.parseDouble(txtPeso.getText());
            double d = Double.parseDouble(txtDistancia.getText());
            
            Envio env;
            String tipo = (String) comboTipo.getSelectedItem();
            if (tipo.equals("Terrestre")) env = new EnvioTerrestre(cli, cod, p, d);
            else if (tipo.equals("Aéreo")) env = new EnvioAereo(cli, cod, p, d);
            else env = new EnvioMaritimo(cli, cod, p, d);

            listaEnvios.add(env);
            actualizarTabla();
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Peso y Distancia deben ser números.");
        }
    }

    private void accionEliminar() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            listaEnvios.remove(fila);
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un envío de la tabla para eliminar.");
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Envio e : listaEnvios) {
            modeloTabla.addRow(new Object[]{
                e.getClass().getSimpleName().replace("Envio", ""),
                e.getCodigo(), e.getCliente(), e.getPeso(), e.getDistancia(), e.calcularTarifa()
            });
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText(""); txtCliente.setText("");
        txtPeso.setText(""); txtDistancia.setText("");
        comboTipo.setSelectedIndex(0);
    }
}