package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//Importa a classe model e repository
import com.generation.blogpessoal.model.PostagemModel;
import com.generation.blogpessoal.repository.PostagemRepository;


@RestController
@RequestMapping("/postagem")
@CrossOrigin("*")

public class PostagemController {

	
		@Autowired
		private PostagemRepository repository;
		
		@GetMapping 
		public ResponseEntity< List<PostagemModel>> getAll(){
			return ResponseEntity.ok(repository.findAll());
		}
		
		@GetMapping("/{id}")
		public ResponseEntity<PostagemModel> GetById(@PathVariable long id){
			//assim que for feito uma requisição vai ser capturado uma varaivel id, vamos retornar a interfface com o metodo findBYiD, E ELE DEVOLVE OK OU NÃO ENCONTRADO
			return repository.findById(id)
					.map(resp -> ResponseEntity.ok(resp)).orElse(ResponseEntity.notFound().build());
		}
		
		@GetMapping("/titulo/{titulo}")
		public ResponseEntity<List<PostagemModel>> GetByTitulo(@PathVariable String titulo){
			return ResponseEntity.ok(repository.findAllByTituloContainingIgnoreCase(titulo));
		}
		
		@PostMapping
		public ResponseEntity<PostagemModel> post (@RequestBody PostagemModel postagem){
			return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(postagem));
		}
		
		@PutMapping
		public ResponseEntity<PostagemModel> put (@RequestBody PostagemModel postagem){
			return repository.findById(postagem.getId())
					.map(resposta -> ResponseEntity.ok().body(repository.save(postagem)))
					.orElse(ResponseEntity.notFound().build());

			//Para utilizar o put no postman, devemos copiar todos os dados, ex: titulo,id e texto, e colar no json, para alterar
		}
		
		@DeleteMapping("/{id}")
		public void delete(@PathVariable long id) {
			repository.deleteById(id);
		}
	}

