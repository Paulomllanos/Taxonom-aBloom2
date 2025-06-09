// controller/EvaluacionController.java
package controller;

import backend.Item;
import backend.Pregunta;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class EvaluacionController {
    public List<Item> cargarEvaluacionDesdeTxt(File archivo) throws IOException {
        List<Item> items = new ArrayList<>();
        BufferedReader lector = new BufferedReader(new FileReader(archivo));
        String linea;
        Item itemActual = null;

        while ((linea = lector.readLine()) != null) {
            if (linea.startsWith("Item")) {
                String[] partes = linea.split("\\|");
                itemActual = new Item();
                itemActual.itemId = Integer.parseInt(partes[1]);
                itemActual.nivel = partes[2];
                itemActual.preguntas = new ArrayList<>();
                items.add(itemActual);
            } else if (linea.startsWith("Pregunta") && itemActual != null) {
                String[] partes = linea.split("\\|");
                Pregunta pregunta = new Pregunta();
                pregunta.preguntaId = Integer.parseInt(partes[1]);
                pregunta.texto = partes[2];
                pregunta.opciones = Arrays.asList(partes[3].split(","));
                pregunta.respuestasCorrectas = Arrays.asList(partes[4].split(","));
                itemActual.preguntas.add(pregunta);
            }
        }

        lector.close();
        return items;
    }

    public Map<String, Object> evaluar(List<Item> items, Map<Integer, List<String>> respuestasUsuario) {
        int totalPreguntas = 0, totalCorrectas = 0;
        StringBuilder resultados = new StringBuilder();

        for (Item item : items) {
            int correctasItem = 0;

            for (Pregunta pregunta : item.preguntas) {
                totalPreguntas++;
                List<String> respuesta = respuestasUsuario.getOrDefault(pregunta.preguntaId, new ArrayList<>());
                if (new HashSet<>(respuesta).equals(new HashSet<>(pregunta.respuestasCorrectas))) {
                    totalCorrectas++;
                    correctasItem++;
                }
            }

            double porcentajeItem = (double) correctasItem * 100 / item.preguntas.size();
            resultados.append("Item ").append(item.itemId)
                    .append(" - ").append(String.format("%.2f", porcentajeItem)).append("% correctas\n");
        }

        double porcentajeTotal = (double) totalCorrectas * 100 / totalPreguntas;
        resultados.append("\nResultado total: ").append(String.format("%.2f", porcentajeTotal)).append("%");

        Map<String, Object> resultadoFinal = new HashMap<>();
        resultadoFinal.put("porcentaje", porcentajeTotal);
        resultadoFinal.put("detalle", resultados.toString());

        return resultadoFinal;
    }
}
