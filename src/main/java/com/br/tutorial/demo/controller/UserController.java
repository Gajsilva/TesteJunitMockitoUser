package com.br.tutorial.demo.controller;


import com.br.tutorial.demo.handler.entidadeHandler.BadRequestException;
import com.br.tutorial.demo.request.UsuarioRequestDTO;
import com.br.tutorial.demo.response.UsuarioResponseDTO;
import com.br.tutorial.demo.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
	private final UsuarioService userService;

	public UserController(UsuarioService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<UsuarioResponseDTO> salvar(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO)  {
		UsuarioResponseDTO usuarioResponseDTO = this.userService.salvar(usuarioRequestDTO);
		return ResponseEntity.ok(usuarioResponseDTO);
	}
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) throws BadRequestException {
		UsuarioResponseDTO usuarioResponseDTO = this.userService.buscarPorId(id);
		return ResponseEntity.ok(usuarioResponseDTO);
	}

	@GetMapping
	public ResponseEntity<List<UsuarioResponseDTO>> buscarTodos() {
		List<UsuarioResponseDTO> usuarioResponseDTOS = this.userService.buscarTodos();
		return ResponseEntity.ok(usuarioResponseDTOS);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id,
														@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
		UsuarioResponseDTO usuarioResponseDTO = this.userService.atualizar(id, usuarioRequestDTO);
		return ResponseEntity.ok(usuarioResponseDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		this.userService.deletar(id);
		return ResponseEntity.noContent().build();
	}

}
