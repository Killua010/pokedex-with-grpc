package br.com.grpcpokemon.server.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.protobuf.Empty;

import br.com.grpcpokemon.server.protos.Input;
import br.com.grpcpokemon.server.protos.PokedexGrpc.PokedexBlockingStub;
import br.com.grpcpokemon.server.protos.Pokemon;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class PokemonServiceClient {
	
	@GrpcClient("pokedex")
	private PokedexBlockingStub pokedexBlockingStub;
	
	
	public String getByCode(Integer code) {
        Input input = Input.newBuilder()
                .setPokedexCode(code)
                .build();
       return pokedexBlockingStub.getPokemonByCode(input).getName();
	}


	public List<String> getAll() {
		List<String> names = new ArrayList<String>();
		
		Iterator<Pokemon> iterator = pokedexBlockingStub.getAllPokemons(Empty.newBuilder().build());
		
		iterator.forEachRemaining(pokemon -> names.add(pokemon.getName()));
		
		return names;
		
	}
	

}
