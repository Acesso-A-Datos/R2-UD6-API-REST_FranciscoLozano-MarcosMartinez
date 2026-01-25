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
        return (List<Pokemon>) repositorio.findAll();
    }

    // Buscar por ID
    public Optional<Pokemon> buscarPorId(Long id) {
        return repositorio.findById(id);
    }

    // Guardar (con validación de nombre duplicado)
    public Pokemon guardar(Pokemon pokemon) {
        return repositorio.save(pokemon);
    }

    // Verificar si existe por ID (para PUT/PATCH/DELETE)
    public boolean existeId(Long id) {
        return repositorio.existsById(id);
    }

    // Para el PATCH
    public Optional<Pokemon> actualizarParcial(Long id, Pokemon datosNuevos) {
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
        repositorio.deleteById(id);
    }
}
