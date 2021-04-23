package br.com.grpcpokemon.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.grpcpokemon.server.services.PokemonServiceClient;

@RestController
@RequestMapping("/pokemons")
public class PokemonController {
	
	@Autowired
	private PokemonServiceClient pokemonServiceClient;

	@GetMapping("/{code}")
	public ResponseEntity<String> getByCode(@PathVariable("code") Integer code) {
		return ResponseEntity.ok(pokemonServiceClient.getByCode(code));
	}
	
	@GetMapping
	public ResponseEntity<List<String>> getAll() {
		return ResponseEntity.ok(pokemonServiceClient.getAll());
	}
	
}
