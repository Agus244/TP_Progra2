package Eventos;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
	 String email, nombre, apellido, contrasena;
	    List<IEntrada> entradas = new ArrayList<>();

	    Usuario(String email, String nombre, String apellido, String contrasena) {
	        this.email = email;
	        this.nombre = nombre;
	        this.apellido = apellido;
	        this.contrasena = contrasena;
	    }

	    boolean validarContrasena(String input) {
	        return contrasena.equals(input);
	    }
}
	