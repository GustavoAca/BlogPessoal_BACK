package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.UsuarioModel;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Test
	@Order(1)
	@DisplayName("Cadastrar um Usuario")
	public void deveCriarUmUsuario() {
		HttpEntity<UsuarioModel> requisicao = new HttpEntity<UsuarioModel>(
				new UsuarioModel(0L, "Paulo Antunes", "paulo_antunes@email.com.br", "12345"));

		ResponseEntity<UsuarioModel> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST,
				requisicao, UsuarioModel.class);

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
	}

	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do usuario")
	public void naoDeveDuplicarUsuario() {

		usuarioService.cadastraUsuario(new UsuarioModel(0L, "Maria da Silva", "maria_silva@email.com.br", "12345678"));

		HttpEntity<UsuarioModel> requisicao = new HttpEntity<UsuarioModel>(
				new UsuarioModel(0L, "Maria da Silva", "maria_silva@email.com.br", "12345678"));

		ResponseEntity<UsuarioModel> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST,
				requisicao, UsuarioModel.class);

		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}

	@Test
	@Order(3)
	@DisplayName("Alterar um usuario")
	public void deveAtualizarUmUsuario() {
		Optional<UsuarioModel> usuarioCreate = usuarioService
				.cadastraUsuario(new UsuarioModel(
						0L,"Juliana Andrews", "juliana_andrews@email.com.br", "juliana123"));
		
		UsuarioModel usuarioUpdate = new UsuarioModel(usuarioCreate.get().getId(),
				"Juliana Andrews Ramos", "juliana_ramos@email.com.br","julina123");
		
		HttpEntity<UsuarioModel> requisicao = new HttpEntity<UsuarioModel>(usuarioUpdate);
		
		ResponseEntity<UsuarioModel> resposta = testRestTemplate
				.withBasicAuth("root","root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, UsuarioModel.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os usuario")
	public void deveMostrarTodosUsuarios() {
		usuarioService.cadastraUsuario(new UsuarioModel(
				0L,"Sabrina Sanches","sabrina_sanches@email.com","sabrina123"));
		
		usuarioService.cadastraUsuario(new UsuarioModel(
				0L,"Ricardo Marques","ricardo_marques@email.com", "ricardo123"
				));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}
	
	
	
}
