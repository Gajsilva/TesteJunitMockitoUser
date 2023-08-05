package com.br.tutorial.demo;


import com.br.tutorial.demo.entity.Usuario;
import com.br.tutorial.demo.handler.entidadeHandler.BadRequestException;
import com.br.tutorial.demo.repository.UsuarioRepository;
import com.br.tutorial.demo.request.UsuarioRequestDTO;
import com.br.tutorial.demo.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

public class UsuarioExceptionTest {
   @Mock
   private UsuarioRepository usuarioRepository;
   @Mock
   private Usuario usuario;
   @Mock
   private UsuarioRequestDTO usuarioRequestDTO;
   @InjectMocks
   private UsuarioService usuarioService;

   @BeforeEach
    public void setup(){
       MockitoAnnotations.openMocks(this);
   }
   private static Stream<Long> idsParaTeste(){
       return Stream.of(1L,2L,3L);
   }

   @Test
   @DisplayName("Teste para verificar o lançamento de exceção ao salvar um Usuario com nome existente")
    void testSalvarUsuarioLancarBadRequest(){
       when(usuarioRepository.findByNome(usuarioRequestDTO.getNome())).thenReturn(Optional.of(usuario));

       BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> usuarioService.salvar(usuarioRequestDTO));

       assertEquals(badRequestException.getMessage(),"Este nome para Usuario ja existe");

       verify(usuarioRepository,times(1)).findByNome(usuarioRequestDTO.getNome());
       verify(usuarioRepository, never()).save(any(Usuario.class));
   }

   @ParameterizedTest(name = "Teste com ID:{0}")
   @MethodSource("idsParaTeste")
   @DisplayName("Teste para verificar o lançamento de exceção ao buscar um Usuario inexistente por id")
    void testeBuscarPorIdLancarExcecao(Long id){
       when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

       BadRequestException badRequestException =
               assertThrows(BadRequestException.class, () -> usuarioService.buscarPorId(id));

       assertEquals(badRequestException.getMessage(),"Usuario não encontrado");

       verify(usuarioRepository,times(1)).findById(id);
   }

    @ParameterizedTest(name = "Teste com ID:{0}")
    @MethodSource("idsParaTeste")
    @DisplayName("teste para verificar lancamento de excecao ao atualizar Usuario inexistente")
    void testeAtualizarLancarExcecao(Long id){
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        BadRequestException badRequestException =
                assertThrows(BadRequestException.class, () -> usuarioService.atualizar(id, usuarioRequestDTO));

        assertEquals(badRequestException.getMessage(),"Usuario não encontrado");

        verify(usuarioRepository,times(1)).findById(id);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @ParameterizedTest(name = "Teste com ID:{0}")
    @MethodSource("idsParaTeste")
    @DisplayName("teste para verificar lancamento de excecao ao deletar Usuario inexistente")
    void testeDeletarLancarExcecao(Long id){
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        BadRequestException badRequestException =
                assertThrows(BadRequestException.class, () -> usuarioService.deletar(id));

        assertEquals(badRequestException.getMessage(),"Usuario não encontrado");

        verify(usuarioRepository,times(1)).findById(id);
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }
}