package com.adat.dam2;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioPokemon extends CrudRepository<Pokemon, Long> {
    
}
