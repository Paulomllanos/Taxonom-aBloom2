package controller;

import backend.Item;
import backend.Pregunta;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class EvaluacionController {
    public List<Item> cargarEvaluacionDesdeTxt(File archivo) throws IOException {
        List<Item> items = new ArrayList<>();
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(archivo), "UTF-8")
        );
        String linea;
        Item itemActual = null;
        List<Pregunta> preguntas = new ArrayList<>();
        int preguntaId = 1;

        while ((linea = reader.readLine()) != null) {
            linea = linea.trim();

            if (linea.startsWith("[Item")) {
                if (itemActual != null) {
                    itemActual.preguntas = preguntas;
                    items.add(itemActual);
                }
                itemActual = new Item();
                itemActual.itemId = items.size() + 1;
                itemActual.tipo = linea.toLowerCase().contains("verdadero/falso") ? "verdadero_falso" : "opcion_multiple";
                preguntas = new ArrayList<>();
            } else if (linea.toLowerCase().startsWith("pregunta")) {
                Pregunta p = new Pregunta();
                p.preguntaId = preguntaId++;
                p.texto = linea.substring(linea.indexOf(":") + 1).trim();
                p.opciones = new ArrayList<>();
                p.respuestasCorrectas = new ArrayList<>();
                p.tipo = itemActual.tipo;
                System.out.println("Pregunta cargada: " + p.texto);
                System.out.println("→ Tipo asignado: " + p.tipo);

                while ((linea = reader.readLine()) != null && !linea.trim().isEmpty()) {
                    linea = linea.trim();
                    //! Errores con capturar respuesta para seleccion multiple
                    if (linea.startsWith("Opciones:")) {
                        while ((linea = reader.readLine()) != null && linea.trim().startsWith("-")) {
                            p.opciones.add(linea.trim().substring(1).trim());
                        }
                    } else if (linea.startsWith("Respuesta:")) {
                        String resp = linea.substring(9).trim();
                        if (resp.contains(",")) {
                            for (String r : resp.split(",")) {
                                //respuestas correctas almacenadas
                                String respuestaNormalizada = r.trim().toLowerCase();
                                System.out.println("Respuesta correcta cargada: " + respuestaNormalizada);
                                p.respuestasCorrectas.add(respuestaNormalizada);
                                // p.respuestasCorrectas.add(r.trim().toLowerCase());
                            }
                        } else {
                            String respuestaNormalizada = resp.trim().toLowerCase();
                            System.out.println("Respuesta correcta cargada: " + respuestaNormalizada);
                            p.respuestasCorrectas.add(respuestaNormalizada);
                            // p.respuestasCorrectas.add(resp.trim().toLowerCase());
                        }
                    } else if (linea.startsWith("Tipo:")) {
                        p.tipo = linea.substring(5).trim().toLowerCase();
                    }
                    //! Aqui termina el error
                }

                if (p.tipo.equals("verdadero_falso")) {
                    p.opciones = Arrays.asList("verdadero", "falso");
                }

                preguntas.add(p);
            }
        }

        if (itemActual != null) {
            itemActual.preguntas = preguntas;
            items.add(itemActual);
        }

        reader.close();
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
                Set<String> r1 = new HashSet<>();
                for (String s : respuesta) r1.add(s.trim().toLowerCase());
                Set<String> r2 = new HashSet<>();
                for (String s : pregunta.respuestasCorrectas) r2.add(s.trim().toLowerCase());
                if (r1.equals(r2)) {
                    totalCorrectas++;
                    correctasItem++;
                }
            }

            double porcentajeItem = (double) correctasItem * 100 / item.preguntas.size();
            resultados.append("Item ").append(item.itemId).append(" (").append(item.tipo).append("): ")
                    .append(String.format("%.2f", porcentajeItem)).append("% correctas\n");
        }

        double porcentajeTotal = (double) totalCorrectas * 100 / totalPreguntas;
        resultados.append("\nResultado total: ").append(String.format("%.2f", porcentajeTotal)).append("%");

        Map<String, Object> resultadoFinal = new HashMap<>();
        resultadoFinal.put("porcentaje", porcentajeTotal);
        resultadoFinal.put("detalle", resultados.toString());
        return resultadoFinal;
    }

    public void guardarRespuesta(Map<Integer, List<String>> mapa, int id, List<String> valores) {
        mapa.put(id, valores);
    }
}