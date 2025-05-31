package frontend;

import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;
import java.io.*;
// import java.util.*;
import backend.Item;
import backend.Pregunta;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
// import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class MainFrame2 extends JFrame {
    private List<Item> items;
    private Map<Integer, List<String>> respuestasUsuario = new HashMap<>();
    private int itemActual = 0;
    private JPanel panelPreguntas;
    private JButton btnAnterior, btnSiguiente;
    private JLabel lblItem;

    public MainFrame2() throws IOException {
        items = leerEvaluacionMock();
        setTitle("Evaluación");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        lblItem = new JLabel("", SwingConstants.CENTER);
        add(lblItem, BorderLayout.NORTH);

        panelPreguntas = new JPanel();
        panelPreguntas.setLayout(new BoxLayout(panelPreguntas, BoxLayout.Y_AXIS));
        add(new JScrollPane(panelPreguntas), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        btnAnterior = new JButton("Anterior");
        btnSiguiente = new JButton("Siguiente");

        btnAnterior.addActionListener(e -> mostrarItem(itemActual - 1));
        btnSiguiente.addActionListener(e -> {
            if (btnSiguiente.getText().equals("Entregar")) mostrarResultados();
            else mostrarItem(itemActual + 1);
        });

        panelBotones.add(btnAnterior);
        panelBotones.add(btnSiguiente);
        add(panelBotones, BorderLayout.SOUTH);

        mostrarItem(0);
        setVisible(true);
    }

    private void mostrarItem(int index) {
        if (index < 0 || index >= items.size()) return;
        itemActual = index;
        panelPreguntas.removeAll();

        Item item = items.get(itemActual);
        lblItem.setText("Item " + item.itemId + " - Nivel: " + item.nivel);

        for (Pregunta pregunta : item.preguntas) {
            JPanel panelPregunta = new JPanel(new BorderLayout());
            panelPregunta.setBorder(BorderFactory.createTitledBorder("" + pregunta.texto));

            JPanel opcionesPanel = new JPanel();
            opcionesPanel.setLayout(new BoxLayout(opcionesPanel, BoxLayout.Y_AXIS));

            List<JCheckBox> checkboxes = new ArrayList<>();
            for (String opcion : pregunta.opciones) {
                JCheckBox cb = new JCheckBox(opcion);
                checkboxes.add(cb);
                opcionesPanel.add(cb);
            }
            panelPregunta.add(opcionesPanel, BorderLayout.CENTER);
            panelPreguntas.add(panelPregunta);

            for (JCheckBox cb : checkboxes) {
                cb.addItemListener(e -> {
                    List<String> seleccionadas = new ArrayList<>();
                    for (JCheckBox c : checkboxes)
                        if (c.isSelected()) seleccionadas.add(c.getText());
                    respuestasUsuario.put(pregunta.preguntaId, seleccionadas);
                });
            }
        }

        btnAnterior.setEnabled(itemActual > 0);
        btnSiguiente.setText(itemActual == items.size() - 1 ? "Entregar" : "Siguiente");

        panelPreguntas.revalidate();
        panelPreguntas.repaint();
    }

    private void mostrarResultados() {
        int total = 0, correctas = 0;
        StringBuilder resultadoPorItem = new StringBuilder();

        for (Item item : items) {
            int totalItem = 0, correctasItem = 0;
            for (Pregunta pregunta : item.preguntas) {
                total++;
                totalItem++;
                List<String> seleccionadas = respuestasUsuario.getOrDefault(pregunta.preguntaId, new ArrayList<>());
                if (new HashSet<>(seleccionadas).equals(new HashSet<>(pregunta.respuestasCorrectas))) {
                    correctas++;
                    correctasItem++;
                }
            }
            double porcItem = (correctasItem * 100.0 / totalItem);
            resultadoPorItem.append("Item ").append(item.itemId).append(": ").append(String.format("%.2f", porcItem)).append("% correctas\n");
        }

        double porcentajeTotal = (correctas * 100.0 / total);
        JOptionPane.showMessageDialog(this, "Resultado total: " + String.format("%.2f", porcentajeTotal) + "% correctas\n" + resultadoPorItem);
    }

    private List<Item> leerEvaluacionMock() {
        List<Item> items = new ArrayList<>();

        Pregunta p1 = new Pregunta();
        p1.preguntaId = 1;
        p1.texto = "¿Cuáles son lenguajes de programación?";
        p1.opciones = Arrays.asList("Java", "HTML", "Python", "CSS");
        p1.respuestasCorrectas = Arrays.asList("Java", "Python");

        Pregunta p2 = new Pregunta();
        p2.preguntaId = 2;
        p2.texto = "¿Cuál es una base de datos relacional?";
        p2.opciones = Arrays.asList("MongoDB", "MySQL", "Redis", "Cassandra");
        p2.respuestasCorrectas = Arrays.asList("MySQL");

        Item item1 = new Item();
        item1.itemId = 1;
        item1.nivel = "Conocimiento";
        item1.preguntas = Arrays.asList(p1, p2);

        Pregunta p3 = new Pregunta();
        p3.preguntaId = 3;
        p3.texto = "¿Qué protocolos pertenecen al modelo TCP/IP?";
        p3.opciones = Arrays.asList("HTTP", "TCP", "FTP", "SMTP");
        p3.respuestasCorrectas = Arrays.asList("TCP", "SMTP");

        Item item2 = new Item();
        item2.itemId = 2;
        item2.nivel = "Comprensión";
        item2.preguntas = Arrays.asList(p3);

        items.add(item1);
        items.add(item2);

        return items;
    }

    public static void main(String[] args) throws IOException {
        new MainFrame2();
    }
}
