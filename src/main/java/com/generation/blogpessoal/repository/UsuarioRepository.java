package com.generation.blogpessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.generation.blogpessoal.model.UsuarioModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
	/**
	 * * Método que busca um usuário pelo seu usuario (email). * * select * from
	 * tb_usuarios where usuario = "usuario procurado" * * Quando não se sabe qual
	 * tipo de resposta pode ter ao buscar na requisição e * tem mais de uma
	 * possível
	 */
	public Optional<UsuarioModel> findByUsuario(String usuario);

	public List<UsuarioModel> findAllByNomeContainingIgnoreCase(String nome);
}
