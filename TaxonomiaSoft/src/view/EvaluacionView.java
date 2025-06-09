// view/EvaluacionView.java
package view;

import backend.Item;
import backend.Pregunta;
import controller.EvaluacionController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class EvaluacionView extends JFrame {
    private List<Item> items;
    private Map<Integer, List<String>> respuestasUsuario = new HashMap<>();
    private int itemActual = 0;

    private JPanel panelPreguntas;
    private JButton btnAnterior;
    private JButton btnSiguiente;
    private JLabel lblItem;

    private final EvaluacionController controller;

    public EvaluacionView() {
        controller = new EvaluacionController();
        setupUI();
        seleccionarArchivoYComenzar();
    }

    private void setupUI() {
        setTitle("Evaluación MVC");
        setSize(600, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        lblItem = new JLabel("Item", SwingConstants.CENTER);
        add(lblItem, BorderLayout.NORTH);

        panelPreguntas = new JPanel();
        panelPreguntas.setLayout(new BoxLayout(panelPreguntas, BoxLayout.Y_AXIS));
        add(new JScrollPane(panelPreguntas), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        btnAnterior = new JButton("Anterior");
        btnSiguiente = new JButton("Siguiente");

        btnAnterior.addActionListener(e -> mostrarItem(itemActual - 1));
        btnSiguiente.addActionListener(e -> {
            if (btnSiguiente.getText().equals("Entregar")) {
                mostrarResultados();
            } else {
                mostrarItem(itemActual + 1);
            }
        });

        panelBotones.add(btnAnterior);
        panelBotones.add(btnSiguiente);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void seleccionarArchivoYComenzar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione el archivo de evaluación");

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                items = controller.cargarEvaluacionDesdeTxt(archivo);
                mostrarItem(0);
                setVisible(true);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo.");
                System.exit(1);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se seleccionó archivo.");
            System.exit(0);
        }
    }

    private void mostrarItem(int indice) {
        if (indice >= 0 && indice < items.size()) {
            itemActual = indice;
            panelPreguntas.removeAll();
            Item item = items.get(indice);
            lblItem.setText("Item " + item.itemId + " - Nivel: " + item.nivel);

            for (Pregunta pregunta : item.preguntas) {
                JPanel panelPregunta = new JPanel(new BorderLayout());
                panelPregunta.setBorder(BorderFactory.createTitledBorder(pregunta.texto));

                JPanel opcionesPanel = new JPanel();
                opcionesPanel.setLayout(new BoxLayout(opcionesPanel, BoxLayout.Y_AXIS));

                List<JCheckBox> checkBoxes = new ArrayList<>();
                for (String opcion : pregunta.opciones) {
                    JCheckBox cb = new JCheckBox(opcion);
                    checkBoxes.add(cb);
                    opcionesPanel.add(cb);
                }

                for (JCheckBox cb : checkBoxes) {
                    cb.addItemListener(e -> {
                        List<String> seleccionadas = new ArrayList<>();
                        for (JCheckBox box : checkBoxes) {
                            if (box.isSelected()) {
                                seleccionadas.add(box.getText());
                            }
                        }
                        respuestasUsuario.put(pregunta.preguntaId, seleccionadas);
                    });
                }

                panelPregunta.add(opcionesPanel, BorderLayout.CENTER);
                panelPreguntas.add(panelPregunta);
            }

            btnAnterior.setEnabled(itemActual > 0);
            btnSiguiente.setText(itemActual == items.size() - 1 ? "Entregar" : "Siguiente");

            panelPreguntas.revalidate();
            panelPreguntas.repaint();
        }
    }

    private void mostrarResultados() {
        Map<String, Object> resultado = controller.evaluar(items, respuestasUsuario);
        JOptionPane.showMessageDialog(this, resultado.get("detalle").toString());
        System.exit(0);
    }
}