package com.adat.dam2;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

/*
 * @RestController
 * Indica que esta clase es un controlador REST.
 * Combina internamente:
 *  - @Controller  -> marca la clase como controlador
 *  - @ResponseBody -> lo que devuelvan los métodos se convierte a JSON
 * No devuelve vistas HTML, solo datos (JSON).
 */

@RestController

/*
 * @RequestMapping("/pokemon")
 * Define la ruta base del controlador.
 * Todas las URLs de esta clase empezarán por /pokemon
 * Ejemplo:
 *  - GET /pokemon
 *  - GET /pokemon/1
 */

@RequestMapping("/pokemon")
public class ControladorPokemon {

    // Servicio con la lógica de negocio
    private final PokemonService service;

    /*
     * Constructor
     * Spring lo usa para inyectar dependencias automáticamente.
     */

    public ControladorPokemon(PokemonService service) {
        this.service = service;
    }

    /*
     * @GetMapping
     * Maneja peticiones HTTP GET.
     * Al no tener ruta, responde a:
     * GET /pokemon
     */

    @GetMapping
    public ResponseEntity<List<Pokemon>> getAll() {

        /*
         * ResponseEntity
         * Permite devolver:
         *  - código HTTP
         *  - cuerpo de la respuesta
         *  - cabeceras (si hiciera falta)
         * ResponseEntity.ok(...) -> HTTP 200
         */
    	ResponseEntity<List<Pokemon>> resultado = ResponseEntity.ok(service.listarTodos());
    	System.out.println("GET ALL: " + resultado.getStatusCode());
    	
        return resultado;
    }

    /*
     * @GetMapping("/{id}")
     * {id} es una variable en la URL.
     * Ejemplo: GET /pokemon/5
     */

    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> getOne(

            /*
             * @PathVariable
             * Extrae el valor de {id} desde la URL
             * y lo asigna a la variable id.
             * Ejemplo:
             * URL: /pokemon/8
             * id = 8
             */

            @PathVariable(name = "id") Long id) {

        /*
         * buscarPorId devuelve Optional<Pokemon>
         * Optional evita usar null.
         */

    	ResponseEntity<Pokemon> resultado = service.buscarPorId(id)
                // Si existe -> 200 OK + Pokémon
                .map(ResponseEntity::ok)
                // Si no existe -> 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    	
    	System.out.println("GET BY ID: " + resultado.getStatusCode());
    	
        return resultado;
    }

    /*
     * @PostMapping
     * Maneja peticiones HTTP POST.
     * Se usa para CREAR recursos.
     * URL: POST /pokemon
     */

    @PostMapping
    public ResponseEntity<Optional<Pokemon>> postPokemon(

            /*
             * @RequestBody
             * Indica que el objeto Pokemon se obtiene
             * del cuerpo (body) de la petición HTTP.
             * Spring convierte automáticamente el JSON
             * recibido en un objeto Pokemon.
             */

            @RequestBody Pokemon pokemon) {

        /*
         * HttpStatus.CREATED -> HTTP 201
         * Se usa cuando un recurso se crea correctamente.
         */

    	ResponseEntity<Optional<Pokemon>> resultado = ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.guardar(pokemon));
    	
    	System.out.println("POST: " + resultado.getStatusCode());
    	
        return resultado;
    }

    /*
     * @PutMapping("/{id}")
     * Maneja peticiones HTTP PUT.
     * PUT se usa para REEMPLAZAR completamente un recurso.
     */

    @PutMapping("/{id}")
    public ResponseEntity<Optional<Pokemon>> putPokemon(

            // @PathVariable vuelve a capturar el id de la URL
            @PathVariable(name = "id") Long id,

            /*
             * @RequestBody
             * Contiene TODOS los datos nuevos del Pokémon.
             * En PUT se espera el objeto completo.
             */

            @RequestBody Pokemon datosNuevos) {

    	ResponseEntity<Optional<Pokemon>> resultado;
    	
        // Si el Pokémon no existe, devuelve 404
        if (!service.existeId(id)) {
        	resultado = ResponseEntity.notFound().build();
        	System.out.println("PUT :" + resultado.getStatusCode());
            return resultado;
        }

        resultado = service.buscarPorId(id).map(p -> {

            // Se reemplazan todos los campos
            p.setNombre(datosNuevos.getNombre());
            p.setTipo(datosNuevos.getTipo());
            p.setNivel(datosNuevos.getNivel());

            return ResponseEntity.ok(service.guardar(p));

        }).orElse(ResponseEntity.notFound().build());
        
        System.out.println("PUT: " + resultado.getStatusCode());
        
        return resultado;
    }

    /*
     * @PatchMapping("/{id}")
     * Maneja peticiones HTTP PATCH.
     * PATCH se usa para ACTUALIZAR PARCIALMENTE un recurso.
     */

    @PatchMapping("/{id}")
    public ResponseEntity<Pokemon> patchPokemon(

            // @PathVariable obtiene el id desde la URL
            @PathVariable("id") Long id,

            /*
             * @RequestBody
             * Contiene solo los campos que se quieren modificar.
             */

            @RequestBody Pokemon pokemonDatosNuevos) {

    	ResponseEntity<Pokemon> resultado = service.actualizarParcial(id, pokemonDatosNuevos)
                /*
                 * service.actualizarParcial(id, pokemonDatosNuevos)
                 * devuelve Optional<Pokemon>:
                 *  - si existe el Pokémon → Optional con el Pokémon actualizado
                 *  - si no existe → Optional vacío
                 *
                 * .map(ResponseEntity::ok)
                 *  - si el Optional tiene valor, lo envuelve en ResponseEntity HTTP 200
                 *  - equivalente a: p -> ResponseEntity.ok(p)
                 *
                 * .orElse(ResponseEntity.notFound().build())
                 *  - si el Optional está vacío, devuelve HTTP 404
                 */
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    	
    	System.out.println("PATCH: " + resultado.getStatusCode());
    	
        return resultado;
    }

    /*
     * @DeleteMapping("/{id}")
     * Maneja peticiones HTTP DELETE.
     * Se usa para eliminar recursos.
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePokemon(

            // @PathVariable captura el id de la URL
            @PathVariable(name = "id") Long id) {

    	ResponseEntity<Void> resultado;
    	
        if (service.existeId(id)) {
            service.eliminar(id);

            /*
             * ResponseEntity.noContent()
             * Devuelve HTTP 204
             * Significa: borrado correcto sin cuerpo.
             */

            resultado = ResponseEntity.noContent().build();
            System.out.println("DELETE: " + resultado.getStatusCode());
            
            return resultado;
        }

        resultado = ResponseEntity.notFound().build();
        System.out.println("DELETE: " + resultado.getStatusCode());
        
        return resultado;
    }
}