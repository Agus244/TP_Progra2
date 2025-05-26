package Eventos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Usuario {
    private String email;
    private String nombre; // Nuevo campo
    private String apellido; // Nuevo campo
    private String contrasenia;
    private List<IEntrada> misEntradas; // Usamos IEntrada para mantener la abstracción

    public Usuario(String email, String nombre, String apellido, String contrasenia) {
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede ser nulo o vacío.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("Apellido no puede ser nulo o vacío.");
        }
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new IllegalArgumentException("Contraseña no puede ser nula o vacía.");
        }

        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.contrasenia = contrasenia; // En una aplicación real, se debería hashear la contraseña.
        this.misEntradas = new ArrayList<>();
    }

    public boolean autenticar(String contrasenia) {
        return this.contrasenia.equals(contrasenia); // Simple comparación, no segura para producción.
    }

    public void agregarEntrada(IEntrada entrada) {
        if (entrada != null) {
            this.misEntradas.add(entrada);
        }
    }

    public boolean eliminarEntrada(IEntrada entrada) {
        if (entrada != null) {
            return this.misEntradas.remove(entrada);
        }
        return false;
    }


    // Getters
    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public List<IEntrada> getMisEntradas() {
        return Collections.unmodifiableList(new ArrayList<>(misEntradas)); // Retorna copia inmutable
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usuario: ").append(nombre).append(" ").append(apellido);
        sb.append(" (").append(email).append(")\n");
        if (!misEntradas.isEmpty()) {
            sb.append("  Entradas Compradas (").append(misEntradas.size()).append("):\n");
            for (IEntrada entrada : misEntradas) {
                sb.append("    ").append(entrada.toString()).append("\n");
            }
        } else {
            sb.append("  No tiene entradas compradas.\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}