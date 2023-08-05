package com.br.tutorial.demo.service;



import com.br.tutorial.demo.entity.Usuario;
import com.br.tutorial.demo.handler.entidadeHandler.BadRequestException;
import com.br.tutorial.demo.repository.UsuarioRepository;
import com.br.tutorial.demo.request.UsuarioRequestDTO;
import com.br.tutorial.demo.response.UsuarioResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
	private final UsuarioRepository usuarioRepository;
	private final ModelMapper modelMapper;

	public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper) {
		this.usuarioRepository = usuarioRepository;
		this.modelMapper = modelMapper;
	}

	public UsuarioResponseDTO salvar(UsuarioRequestDTO usuarioRequestDTO)  {
			if (usuarioRepository.findByNome(usuarioRequestDTO.getNome()).isPresent()) {
				throw new BadRequestException("Este nome para usuario ja existe");
			}
			Usuario usuario = this.modelMapper.map(usuarioRequestDTO, Usuario.class);
			usuario = this.usuarioRepository.save(usuario);
			return this.modelMapper.map(usuario, UsuarioResponseDTO.class);

	}
	public List<UsuarioResponseDTO> buscarTodos() {
		return this.usuarioRepository.findAll().stream().map(usuario ->
				this.modelMapper.map(usuario, UsuarioResponseDTO.class)).collect(Collectors.toList());
	}

	public UsuarioResponseDTO buscarPorId(Long id) {
			Optional<Usuario> responsavel = this.usuarioRepository.findById(id);
			if (responsavel.isEmpty()){
				throw new BadRequestException("Usuario não encontrado");
			}
			return this.modelMapper.map(responsavel.get(), UsuarioResponseDTO.class);
	}

	public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO usuarioRequestDTO) {
		Optional<Usuario> responsavelOptional = this.usuarioRepository.findById(id);

		if(responsavelOptional.isEmpty()){
			throw new BadRequestException("Usuario não encontrado");
		}
		Usuario usuario = responsavelOptional.get();
		usuarioRequestDTO.setId(usuario.getId());

		usuario = this.modelMapper.map(usuarioRequestDTO, Usuario.class);
		usuario = this.usuarioRepository.save(usuario);

		return this.modelMapper.map(usuario, UsuarioResponseDTO.class);

	}

	public void deletar(Long id) {
		Optional <Usuario> userOptional = this.usuarioRepository.findById(id);

		if(userOptional.isEmpty()){
			throw new BadRequestException("Usuario não encontrado");
		}

		usuarioRepository.delete(userOptional.get());
	}


}
