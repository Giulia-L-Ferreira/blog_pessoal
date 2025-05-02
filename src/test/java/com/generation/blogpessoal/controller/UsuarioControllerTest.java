package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.User;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		
		usuarioRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(new User(
				0L, "root", "root@root.com", "rootroot", "-"));
		
	}
	
	@Test
	@DisplayName("Cadastrar Usuario")
	public void deveCriarUmUsuario() {
		
		HttpEntity<User> corpoRequisicao = new HttpEntity<User>(new User(
				0L, "pudim", "cachorropudim@root.com", "123456", "-"));
		
		ResponseEntity<User> corpoResposta = testRestTemplate.exchange("/usuarios/cadastrar",
				HttpMethod.POST, corpoRequisicao, User.class);
		
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());

	}
	
	@Test
	@DisplayName("Não deve permitir duplicação de usuario")
	public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new User(
				0L, "Yago", "yagosilva@root.com", "456789", "-"));
		
		HttpEntity<User> corpoRequisicao = new HttpEntity<User>(new User(
				0L, "Yago", "yagosilva@root.com", "456789", "-"));
		
		ResponseEntity<UsuarioRepository> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, UsuarioRepository.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
		
	}
	
	@Test
	@DisplayName("Atualizar um Usuário")
	public void deveAtualizarUmUsuario() {
		
		Optional<User> usuarioCadastradOptional = usuarioService.cadastrarUsuario(new User(
				0L, "Willy", "Willypescador@root.com", "pescarpeixe123", "-"));
		
		User usuarioUpdate = new User(usuarioCadastrado.get().getId(),
				"Willy pescador", "Willypescador@root.com", "pescarpeixes33", "-");
		
			HttpEntity<User> corpoRequisicao = new HttpEntity<User>(usuarioUpdate);
			
			ResponseEntity<User> corpoResposta = testRestTemplate
					.withBasicAuth("root@root.com", "rootroot")
					.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, User.class);
			
			assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
			
	}
	
	@Test
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {
		
		usuarioService.cadastrarUsuario(new User(
				0L, "Emilly", "EmillyCapesto25@root.com", "Boby_25", "-"));
		
		usuarioService.cadastrarUsuario(new User(
				0L, "Michael", "MichaelMiers@root.com", "1984faz", "-"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/all", HttpMethod.GET , null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}

}
