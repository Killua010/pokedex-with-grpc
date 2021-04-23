package br.com.grpcpokemon.server.services;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.protobuf.Empty;

import br.com.grpcpokemon.server.models.PokemonResponse;
import br.com.grpcpokemon.server.models.QtdPokemonResponse;
import br.com.grpcpokemon.server.protos.Input;
import br.com.grpcpokemon.server.protos.PokedexGrpc.PokedexImplBase;
import br.com.grpcpokemon.server.protos.Pokemon;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class PokemonServiceServer extends PokedexImplBase {
	
	@Autowired
	private RestTemplate restTemplate;
	
	private final String url = "https://pokeapi.co/api/v2/pokemon/";
	
	@Override
	public void getPokemonByCode(Input request, StreamObserver<Pokemon> responseObserver) {
		
		ResponseEntity<PokemonResponse> pokemonResponse = restTemplate.getForEntity(url + request.getPokedexCode(), PokemonResponse.class);
		String name = pokemonResponse.getBody().getName();
		Pokemon pokemon = Pokemon.newBuilder().setName(name).build();
		
        responseObserver.onNext(pokemon);
        responseObserver.onCompleted();
		
	}
	
	@Override
	public void getAllPokemons(Empty request, StreamObserver<Pokemon> responseObserver) {
		
		AtomicInteger i = new AtomicInteger(0);
		ExecutorService executorService = Executors.newFixedThreadPool(4);		
		ResponseEntity<QtdPokemonResponse> qtdPokemonResponse = restTemplate.getForEntity(url, QtdPokemonResponse.class);
		
		Callable<?> callable = () -> {
//			System.err.println(Thread.currentThread().getName() + "  --  " + i.get());
			ResponseEntity<PokemonResponse> pokemonResponse = restTemplate.getForEntity(url + i.incrementAndGet(), PokemonResponse.class);
			String name = pokemonResponse.getBody().getName();
			Pokemon pokemon = Pokemon.newBuilder().setName(name).build();
			
			responseObserver.onNext(pokemon);
			return pokemon;
			
		};
		
		List<Callable<?>> pokemons = Stream.generate(() -> callable).limit(qtdPokemonResponse.getBody().getCount()).collect(Collectors.toList());
		
		try {
			executorService.invokeAll(pokemons);
			responseObserver.onCompleted();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
