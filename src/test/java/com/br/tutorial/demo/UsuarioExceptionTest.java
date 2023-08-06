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
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioExceptionTest {
    @Mock // Indica que esta classe será uma instância simulada.
    private UsuarioRepository usuarioRepository;
    @Mock
    private Usuario usuario;
    @Mock
    private UsuarioRequestDTO usuarioRequestDTO;
    @InjectMocks // Injeta automaticamente objetos simulados, permitindo o uso de dependências simuladas.
    private UsuarioService usuarioService;

    @BeforeEach // Método executado antes de cada teste dentro da classe de testes.
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Inicializar mocks criados com @Mock.
    }

    // Usado para testes parametrizados, onde podemos realizar o mesmo teste com IDs diferentes.
    private static Stream<Long> idsParaTeste() {
        return Stream.of(1L, 2L, 3L);
    }

    @Test
    @DisplayName("Teste para verificar o lançamento de exceção ao salvar um Usuario com nome existente")
    void testSalvarUsuarioLancarBadRequest() {
        // Configurando o comportamento do mock usuarioRepository para retornar um Optional contendo o objeto usuario simulado
        // quando o método findByNome é chamado com o nome de usuário fornecido no objeto usuarioRequestDTO.
        when(usuarioRepository.findByNome(usuarioRequestDTO.getNome())).thenReturn(Optional.of(usuario));

        // Verificando se a exceção BadRequestException é lançada quando o método salvar é chamado no usuarioService.
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> usuarioService.salvar(usuarioRequestDTO));

        // Verificando se a mensagem da exceção lançada é a esperada.
        assertEquals(badRequestException.getMessage(), "Este nome para Usuario ja existe");

        // Verificando se o método findByNome do mock usuarioRepository foi chamado exatamente uma vez com o nome de usuário fornecido.
        verify(usuarioRepository, times(1)).findByNome(usuarioRequestDTO.getNome());

        // Verificando se o método save do mock usuarioRepository nunca foi chamado com qualquer objeto Usuario.
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @ParameterizedTest(name = "Teste com ID:{0}") // Será executado várias vezes com valores de ids diferentes.
    @MethodSource("idsParaTeste")  // Anotação indicando uso das streams de ids no @ParameterizedTest
    @DisplayName("Teste para verificar o lançamento de exceção ao buscar um Usuario inexistente por id")
    void testeBuscarPorIdLancarExcecao(Long id) {
        // Configurando o comportamento do mock usuarioRepository para retornar um Optional vazio quando o método findById
        // é chamado com o ID fornecido.
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Verificando se a exceção BadRequestException é lançada quando o método buscarPorId é chamado no usuarioService.
        BadRequestException badRequestException =
                assertThrows(BadRequestException.class, () -> usuarioService.buscarPorId(id));

        // Verificando se a mensagem da exceção lançada é a esperada.
        assertEquals(badRequestException.getMessage(), "Usuario não encontrado");

        // Verificando se o método findById do mock usuarioRepository foi chamado exatamente uma vez com o ID fornecido.
        verify(usuarioRepository, times(1)).findById(id);
    }

    @ParameterizedTest(name = "Teste com ID:{0}")
    @MethodSource("idsParaTeste")
    @DisplayName("teste para verificar lançamento de exceção ao atualizar Usuario inexistente")
    void testeAtualizarLancarExcecao(Long id) {
        // Configurando o comportamento do mock usuarioRepository para retornar um Optional vazio quando o método findById
        // é chamado com o ID fornecido.
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Verificando se a exceção BadRequestException é lançada quando o método atualizar é chamado no usuarioService.
        BadRequestException badRequestException =
                assertThrows(BadRequestException.class, () -> usuarioService.atualizar(id, usuarioRequestDTO));

        // Verificando se a mensagem da exceção lançada é a esperada.
        assertEquals(badRequestException.getMessage(), "Usuario não encontrado");

        // Verificando se o método findById do mock usuarioRepository foi chamado exatamente uma vez com o ID fornecido.
        verify(usuarioRepository, times(1)).findById(id);

        // Verificando se o método save do mock usuarioRepository nunca foi chamado com qualquer objeto Usuario.
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @ParameterizedTest(name = "Teste com ID:{0}")
    @MethodSource("idsParaTeste")
    @DisplayName("teste para verificar lançamento de exceção ao deletar Usuario inexistente")
    void testeDeletarLancarExcecao(Long id) {
        // Configurando o comportamento do mock usuarioRepository para retornar um Optional vazio quando o método findById
        // é chamado com o ID fornecido.
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Verificando se a exceção BadRequestException é lançada quando o método deletar é chamado no usuarioService.
        BadRequestException badRequestException =
                assertThrows(BadRequestException.class, () -> usuarioService.deletar(id));

        // Verificando se a mensagem da exceção lançada é a esperada.
        assertEquals(badRequestException.getMessage(), "Usuario não encontrado");

        // Verificando se o método findById do mock usuarioRepository foi chamado exatamente uma vez com o ID fornecido.
        verify(usuarioRepository, times(1)).findById(id);

        // Verificando se o método delete do mock usuarioRepository nunca foi chamado com qualquer objeto Usuario.
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }
}
