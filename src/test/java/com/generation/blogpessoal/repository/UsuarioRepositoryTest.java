package com.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.blogpessoal.model.UsuarioModel;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		usuarioRepository.save(new UsuarioModel(0L, "maiar abdar", "ma@gmail.com","12345"));
		
		usuarioRepository.save(new UsuarioModel(0L, "Israel palestina", "is@gmail.com","12345"));
		
		usuarioRepository.save(new UsuarioModel(0L, "Dougles silva", "do@gmail.com","12345"));
		
		usuarioRepository.save(new UsuarioModel(0L, "Michael silva", "mi@gmail.com","12345"));
		
		usuarioRepository.save(new UsuarioModel(0L, "Fernando silva", "fe@gmail.com","12345"));
	}
	
	@Test
	@DisplayName("Retorna 1 usuario")
	public void deveRetornarUmUsuario() {
		Optional<UsuarioModel> usuario = usuarioRepository.findByUsuario("ma@gmail.com");
		assertTrue(usuario.get().getUsuario().equals("ma@gmail.com"));
	}
	
	@Test
	@DisplayName("Retorna 3 usuarios")
	public void deveRetornarTresUsuarios() {
		List<UsuarioModel> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("silva");
		assertEquals(3, listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getNome().equals("Dougles silva"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Michael silva"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Fernando silva"));
	}

	@AfterAll
	public void end() {
		usuarioRepository.deleteAll();
	}

}
