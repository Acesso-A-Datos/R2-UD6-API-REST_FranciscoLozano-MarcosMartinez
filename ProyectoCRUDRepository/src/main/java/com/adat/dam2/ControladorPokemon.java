package com.adat.dam2;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokemon")
public class ControladorPokemon {

    private final PokemonService service;

    public ControladorPokemon(PokemonService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Pokemon>> getAll() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> getOne(@PathVariable(name="id") Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> postPokemon(@RequestBody Pokemon pokemon) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(pokemon));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pokemon> putPokemon(@PathVariable(name="id") Long id, @RequestBody Pokemon datosNuevos) {
        if (!service.existeId(id)) {
            return ResponseEntity.notFound().build();
        }
        return service.buscarPorId(id).map(p -> {
            p.setNombre(datosNuevos.getNombre());
            p.setTipo(datosNuevos.getTipo());
            p.setNivel(datosNuevos.getNivel());
            return ResponseEntity.ok(service.guardar(p));
        }).orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<Pokemon> patchPokemon(
            @PathVariable("id") Long id, 
            @RequestBody Pokemon pokemonDatosNuevos) {
        
        return service.actualizarParcial(id, pokemonDatosNuevos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePokemon(@PathVariable(name="id") Long id) {
        if (service.existeId(id)) {
            service.eliminar(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();
    }
}