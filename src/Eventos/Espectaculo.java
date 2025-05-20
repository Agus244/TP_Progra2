package Eventos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Espectaculo {
    String nombre;
    Map<String, Funcion> funciones = new HashMap<>();

    Espectaculo(String nombre) {
        this.nombre = nombre;
    }

    void agregarFuncion(Funcion funcion) {
        if (funciones.containsKey(funcion.fecha)) throw new RuntimeException("Funcion ya existente");
        funciones.put(funcion.fecha, funcion);
    }

    List<Funcion> getFunciones() {
        return new ArrayList<>(funciones.values());
    }

    Funcion getFuncion(String fecha) {
        return funciones.get(fecha);
    }
}

