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
	    
	    
	    
	    public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getApellido() {
			return apellido;
		}

		public void setApellido(String apellido) {
			this.apellido = apellido;
		}

		public String getContrasena() {
			return contrasena;
		}

		public void setContrasena(String contrasena) {
			this.contrasena = contrasena;
		}

		public List<IEntrada> getEntradas() {
			return entradas;
		}

		public void setEntradas(List<IEntrada> entradas) {
			this.entradas = entradas;
		}

		@Override
	    public String toString() {
	        return "Usuario: " + nombre + " " + apellido +
	               " | Email: " + email +
	               " | Entradas: " + entradas.size();
	    }

		public void agregarEntrada(Entrada entrada) {
			entradas.add(entrada);
		}
}
	