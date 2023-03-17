package com.hdfc.mongo.webcl.product;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.hdfc.mongo.webcl.product.entity.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/mongo/webclient")
public class ProductWebClient {

	String baseUrl = "http://localhost:8585/api/product";
	
	@GetMapping("/all")
	public Mono<List<Product>> getAll() {

		WebClient webClient = WebClient.create(baseUrl);

		Flux<Product> flux = webClient.get().uri("/getall").retrieve().bodyToFlux(Product.class);

		flux.doOnNext((emp) -> {
			System.out.println(emp);
		});

		Mono<List<Product>> mono = flux.collectList();

		return mono;

	}
	@PostMapping("/add")
	public Mono<Product> addProduct(@RequestBody Product product){
		WebClient webClient = WebClient.create(baseUrl);
		Mono<Product> mono=webClient.post().uri("/add").body(Mono.just(product),Product.class).retrieve().bodyToMono(Product.class);
		
		return mono;
	}
	
	@PutMapping("/update")
	public Mono<Product> updateProduct(@RequestBody Product product){
		WebClient webClient = WebClient.create(baseUrl);
		Mono<Product> mono=webClient.put().uri("/update").body(Mono.just(product),Product.class).retrieve().bodyToMono(Product.class);
		return mono;
	}
	
	@DeleteMapping("/delete/{id}")
	public Mono<String> delete(@PathVariable Integer id){
		WebClient webClient = WebClient.create(baseUrl);
		Mono<String> mono=webClient.delete().
				uri(uriBuilder ->uriBuilder.path("/delete/{id}").
				build(id)).
				retrieve().
				bodyToMono(String.class);
		return mono;
	}
	
}
