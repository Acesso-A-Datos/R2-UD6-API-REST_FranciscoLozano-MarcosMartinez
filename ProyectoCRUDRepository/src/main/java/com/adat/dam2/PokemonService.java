package com.adat.dam2;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class PokemonService {

    private final RepositorioPokemon repositorio;

    public PokemonService(RepositorioPokemon repositorio) {
        this.repositorio = repositorio;
    }

    // Listar todos
    public List<Pokemon> listarTodos() {
    	
    	// findAll() devuelve Iterable<Pokemon>, se convierte a List
        return (List<Pokemon>) repositorio.findAll();
    }

    // Buscar por ID
    public Optional<Pokemon> buscarPorId(Long id) {
    	
    	/*
         * repositorio.findById(id) devuelve un Optional<Pokemon>
         *
         * Optional<T> es un contenedor que puede:
         *  1) contener un valor (el Pokémon existe)
         *  2) estar vacío (el Pokémon no existe)
         *
         * Ventajas de Optional:
         *  - Evita NullPointerException
         *  - Permite usar métodos como map(), ifPresent(), orElse()
         *
         * Ejemplo de uso:
         *   Optional<Pokemon> opcional = buscarPorId(5L);
         *   opcional.map(p -> p.getNombre())  --> devuelve Optional<String>
         *   opcional.orElse(new Pokemon())   --> devuelve un Pokémon o uno por defecto si está vacío
         */
        return repositorio.findById(id);
    }

    // Guardar (con validación de nombre duplicado)
    public Pokemon guardar(Pokemon pokemon) {
    	
    	// save() persiste el objeto en la base de datos
        return repositorio.save(pokemon);
    }

    // Verificar si existe por ID (para PUT/PATCH/DELETE)
    public boolean existeId(Long id) {
        return repositorio.existsById(id);
    }

    // Para el PATCH
    public Optional<Pokemon> actualizarParcial(Long id, Pokemon datosNuevos) {
    	
    	/*
         * repositorio.findById(id) devuelve Optional<Pokemon>
         * map() solo se ejecuta si el Pokémon existe
         * Dentro de map:
         *   - Se actualizan SOLO los campos que vienen en datosNuevos
         *   - Se valida que nivel sea mayor a 0 antes de actualizar
         *   - Se guarda el Pokémon actualizado en la base de datos
         * Finalmente, map devuelve Optional<Pokemon> actualizado
         *
         * Si el Pokémon no existe, el Optional queda vacío y el map() NO se ejecuta
         */
        return repositorio.findById(id).map(p -> {
            if (datosNuevos.getNombre() != null) {
                p.setNombre(datosNuevos.getNombre());
            }
            if (datosNuevos.getTipo() != null) {
                p.setTipo(datosNuevos.getTipo());
            }
            // Validamos que el nivel sea mayor a 0 para actualizarlo
            if (datosNuevos.getNivel() > 0) {
                p.setNivel(datosNuevos.getNivel());
            }
            return repositorio.save(p);
        });
    }
    
    // Eliminar
    public void eliminar(Long id) {
    	
    	// deleteById borra el registro de la base de datos
        repositorio.deleteById(id);
    }
}
