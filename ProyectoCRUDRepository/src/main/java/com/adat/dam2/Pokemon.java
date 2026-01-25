package com.adat.dam2;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/*
 * @Entity
 * Indica que esta clase es una ENTIDAD JPA.
 * Esto significa que:
 *  - Esta clase se mapea a una tabla de la base de datos
 *  - Cada instancia de Pokemon es una fila de la tabla
 * Por defecto:
 *  - El nombre de la tabla será "pokemon"
 *    (igual al nombre de la clase)
 */
@Entity
public class Pokemon {
	
	/*
     * @Id
     * Marca este atributo como la CLAVE PRIMARIA (Primary Key)
     * Identifica de forma única cada Pokémon en la base de datos
     */
    @Id
    /*
     * @GeneratedValue
     * Indica que el valor del id se genera automáticamente
     * strategy = GenerationType.IDENTITY
     * - La base de datos se encarga de generar el id
     * - Normalmente usando AUTO_INCREMENT
     * - Muy común en MySQL / PostgreSQL
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    // Nombre del Pokémon (columna de la tabla)
    String nombre;
    
    // Tipo del Pokémon (columna de la tabla)
    String tipo;
    
    // Nivel del Pokémon (columna de la tabla)
    int nivel;
    
    /*
     * Constructor vacío
     * OBLIGATORIO para JPA.
     * Hibernate lo usa para crear objetos al leer de la BD.
     */
    public Pokemon() { }

    /*
     * Constructor con parámetros
     * Útil para crear objetos manualmente en el código
     */
    public Pokemon(String nombre, String tipo, int nivel) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.nivel = nivel;
    }
    /*
     * equals
     * Define cuándo dos objetos Pokemon se consideran IGUALES.
     * En este caso:
     * - Dos Pokémon son iguales si tienen el mismo nombre dado que estamos en una
     *   pokedex solo podria existir un pokemon con el mismo nombre
     * - Se ignora mayúsculas/minúsculas
     * Importante:
     * - NO se usa el id
     * - Se asume que el nombre identifica al Pokémon
     */
    @Override
    public boolean equals(Object o) {

        // Si es el mismo objeto en memoria, son iguales
        if (this == o) return true;

        // Si el objeto es null o de otra clase, no son iguales
        if (o == null || getClass() != o.getClass()) return false;

        // Cast seguro
        Pokemon pokemon = (Pokemon) o;

        // Comparación por nombre ignorando mayúsculas
        return nombre != null && nombre.equalsIgnoreCase(pokemon.nombre);
    }

    
    /*
     * hashCode
     * Debe ser COHERENTE con equals.
     * Si dos objetos son iguales según equals,
     * DEBEN devolver el mismo hashCode.
     * Aquí:
     * - Se genera a partir del nombre en minúsculas
     * - Muy importante para colecciones como:
     *   HashSet, HashMap
     */
    @Override
    public int hashCode() {
        return nombre != null ? nombre.toLowerCase().hashCode() : 0;
    }

    // GETTERS Y SETTERS

    // Devuelve el id del Pokémon
    public Long getId() { return id; }

    // Permite asignar el id (normalmente JPA lo hace solo)
    public void setId(Long id) { this.id = id; }

    // Devuelve el nombre
    public String getNombre() { return nombre; }

    // Modifica el nombre
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Devuelve el tipo
    public String getTipo() { return tipo; }

    // Modifica el tipo
    public void setTipo(String tipo) { this.tipo = tipo; }

    // Devuelve el nivel
    public int getNivel() { return nivel; }

    // Modifica el nivel
    public void setNivel(int nivel) { this.nivel = nivel; }
}

