package com.generation.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.model.UsuarioModel;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	// Função para cadastrar um usuario
	public Optional<UsuarioModel> cadastraUsuario(UsuarioModel usuario) {

		// primeiro valida se o usuario é existente
		if (repository.findByUsuario(usuario.getUsuario()).isPresent()) {
			return Optional.empty();
		}

		// Criptografia de senha em usuario não existente
		usuario.setSenha(criptografarSenha(usuario.getSenha()));

		// Salvo usuario com senha criptografada
		return Optional.of(repository.save(usuario));
	}

	public Optional<UsuarioModel> atualizarUsuario(UsuarioModel usuario) {

		// procurar usuario por id
		if (repository.findById(usuario.getId()).isPresent()) {

			// criptografar a senha nova
			usuario.setSenha(criptografarSenha(usuario.getSenha()));

			// retornar a senha cript
			return Optional.of(repository.save(usuario));
		}
		return Optional.empty();
	}

	// função para encriptar, ela pega a senha do usuario
	public String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(senha);
	}

	// Função para entrar com o login
	public Optional<UsuarioLogin> autenticaUsuario(Optional<UsuarioLogin> usuarioLogin) {
		Optional<UsuarioModel> usuario = repository.findByUsuario(usuarioLogin.get().getUsuario());
		// se usuario existir ele vai cair no if e vai comparar se a senha cadastrada no
		// banco de dados do usuario é igual a senha inserida

		if (usuario.isPresent()) {
			// comparar senha retorna um verdadeiro ou falso
			if (compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {
				// Se as senhas forem iguais, ele vai preencher os dados de id,nome e foto, e
				// ira gerar um token
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get()
						.setToken(gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());
				usuarioLogin.get().setTipo(usuario.get().getTipo());

				return usuarioLogin;
			}
		}

		return Optional.empty();
	}

	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.matches(senhaDigitada, senhaBanco);
	}

	private String gerarBasicToken(String usuario, String senha) {

		String token = usuario + ":" + senha;

		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);
	}

}
