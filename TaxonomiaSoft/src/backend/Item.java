package backend;

import java.util.List;

public class Item {
    public int itemId;
    public String tipo; // "opcion_multiple" o "verdadero_falso"
    public List<Pregunta> preguntas;
}
