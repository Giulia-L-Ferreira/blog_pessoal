package com.generation.blogpessoal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.User;


public interface UsuarioRepository extends JpaRepository<User, Long>{

	public Optional<User> findByUsuario(String usuario);
	
}
