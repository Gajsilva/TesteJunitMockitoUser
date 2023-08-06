package com.br.tutorial.demo;

import com.br.tutorial.demo.entity.Usuario;
import com.br.tutorial.demo.repository.UsuarioRepository;
import com.br.tutorial.demo.request.UsuarioRequestDTO;
import com.br.tutorial.demo.response.UsuarioResponseDTO;
import com.br.tutorial.demo.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {
    @Mock // Indica que esta classe será uma instância simulada.
    private UsuarioRepository usuarioRepository;
    @Mock
    private Usuario usuario;
    @Mock
    private UsuarioResponseDTO usuarioResponseDTO;
    @Mock
    private UsuarioRequestDTO usuarioRequestDTO;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks // Injeta automaticamente objetos simulados, permitindo o uso de dependências simuladas.
    private UsuarioService userService;

    @BeforeEach // Método executado antes de cada teste dentro da classe de testes.
    public void setup() {
        MockitoAnnotations.openMocks(this); // Inicializar mocks criados com @Mock.
        usuario = new Usuario(); // Criando objetos reais para teste.
        usuarioResponseDTO = new UsuarioResponseDTO(); // Criando objetos reais para teste.
        usuarioRequestDTO = new UsuarioRequestDTO(); // Criando objetos reais para teste.
        // O método setup garante que a classe sob teste comece cada teste em um estado conhecido e limpo.
    }

    // Usado para testes parametrizados, onde podemos realizar o mesmo teste com IDs diferentes.
    private static Stream<Long> idsParaTeste() {
        return Stream.of(1L, 2L, 3L);
    }

    @Test
    @DisplayName("Teste para salvar um usuário")
    public void testSalvar() {
        // Configurando o comportamento do mock modelMapper para mapear o objeto usuarioRequestDTO para a classe Usuario
        // e esperando que o retorno seja o objeto usuario simulado.
        when(modelMapper.map(usuarioRequestDTO, Usuario.class)).thenReturn(usuario);

        // Configurando o comportamento do mock usuarioRepository para retornar o próprio objeto usuario simulado
        // quando o método save é chamado com o objeto usuario como argumento.
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        // Configurando o comportamento do mock modelMapper para mapear o objeto usuario para a classe UsuarioResponseDTO
        // e esperando que o retorno seja o objeto usuarioResponseDTO simulado.
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(usuarioResponseDTO);

        // Chamando o método salvar do userService para testá-lo.
        UsuarioResponseDTO salvarUsuarioResponseDTO = userService.salvar(usuarioRequestDTO);

        // Verificando se o resultado retornado não é nulo.
        assertNotNull(salvarUsuarioResponseDTO);

        // Verificando se o método save do mock usuarioRepository foi chamado exatamente uma vez com o objeto usuario
        // como argumento.
        verify(usuarioRepository, times(1)).save(usuario);

        // Verificando se o método map do mock modelMapper foi chamado exatamente uma vez com o objeto usuarioRequestDTO
        // como primeiro argumento e a classe Usuario.class como segundo argumento.
        verify(modelMapper, times(1)).map(usuarioRequestDTO, Usuario.class);

        // Verificando se o método map do mock modelMapper foi chamado exatamente uma vez com o objeto usuario como
        // primeiro argumento e a classe UsuarioResponseDTO.class como segundo argumento.
        verify(modelMapper, times(1)).map(usuario, UsuarioResponseDTO.class);
    }

    @Test
    @DisplayName("Teste para buscar todos os usuários")
    void testBuscarTodos() {
        // Configurando o comportamento do mock usuarioRepository para retornar uma lista contendo 1 objeto usuario
        // quando o método findAll for chamado.
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        // Configurando o comportamento do mock modelMapper para mapear o objeto usuario para a classe
        // UsuarioResponseDTO e retornar o objeto usuarioResponseDTO quando o método map for chamado.
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(usuarioResponseDTO);

        // Chamando o método buscarTodos do userService para testá-lo.
        List<UsuarioResponseDTO> usuarioResponseDTOS = userService.buscarTodos();

        // Verificando se o resultado retornado não é nulo.
        assertNotNull(usuarioResponseDTOS);

        // Verificando se a lista de resultado contém exatamente um elemento.
        assertEquals(1, usuarioResponseDTOS.size());

        // Verifica se o primeiro elemento da lista é do tipo UsuarioResponseDTO, pois temos apenas 1 usuário.
        assertEquals(UsuarioResponseDTO.class, usuarioResponseDTOS.get(0).getClass());

        // Verificando se o método findAll do mock usuarioRepository foi chamado exatamente uma vez.
        verify(usuarioRepository, times(1)).findAll();

        // Verificando se o método map do mock modelMapper foi chamado exatamente uma vez, mapeando o objeto usuario
        // para a classe UsuarioResponseDTO.
        verify(modelMapper, times(1)).map(usuario, UsuarioResponseDTO.class);
    }

    @ParameterizedTest(name = "Teste com ID: {0}") // Será executado várias vezes com valores de ids diferentes.
    @MethodSource("idsParaTeste") // Anotação indicando uso das streams de ids no @ParameterizedTest
    @DisplayName("Teste para buscar usuário por ID")
    void testBuscarPorId(Long id) {
        // Configurando o comportamento do mock usuarioRepository para retornar um Optional contendo o objeto usuario
        // quando o método findById é chamado com o valor específico de id.
        when(usuarioRepository.findById(id)).thenReturn(Optional.ofNullable(usuario));

        // Configurando o comportamento do mock modelMapper para mapear o objeto usuario para a classe UsuarioResponseDTO
        // e retornar o objeto usuarioResponseDTO quando o método map é chamado.
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(usuarioResponseDTO);

        // Chamando o método buscarPorId do userService para testá-lo.
        UsuarioResponseDTO buscarUsuarioResponseDTO = userService.buscarPorId(id);

        // Verificando se o resultado retornado não é nulo.
        assertNotNull(buscarUsuarioResponseDTO);

        // Verificando se os campos do objeto usuarioResponseDTO retornado são iguais aos campos do objeto usuario
        // que foi configurado no mock do usuarioRepository.
        assertEquals(usuarioResponseDTO.getId(), buscarUsuarioResponseDTO.getId());
        assertEquals(usuarioResponseDTO.getNome(), buscarUsuarioResponseDTO.getNome());
        assertEquals(usuarioResponseDTO.getCodigo(), buscarUsuarioResponseDTO.getCodigo());

        // Verificando se o objeto usuarioResponseDTO retornado é igual ao objeto usuarioResponseDTO configurado
        // no mock do modelMapper.
        assertEquals(usuarioResponseDTO, buscarUsuarioResponseDTO);

        // Verificando se o método findById do mock usuarioRepository foi chamado exatamente uma vez com o valor
        // específico de id.
        verify(usuarioRepository, times(1)).findById(id);

        // Verificando se o método map do mock modelMapper foi chamado exatamente uma vez, mapeando o objeto usuario
        // para a classe UsuarioResponseDTO.
        verify(modelMapper, times(1)).map(usuario, UsuarioResponseDTO.class);
    }

    @ParameterizedTest(name = "Teste com ID: {0}")
    @MethodSource("idsParaTeste")
    @DisplayName("Teste para atualizar um usuário, buscando pelo ID")
    void testAtualizar(Long id) {
        // Configurando o comportamento do mock usuarioRepository para retornar um Optional contendo o objeto usuario simulado
        // quando o método findById é chamado com o valor específico de id.
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // Configurando o comportamento do mock modelMapper para mapear o objeto usuarioRequestDTO para a classe Usuario
        // e retornar o objeto usuario simulado.
        when(modelMapper.map(usuarioRequestDTO, Usuario.class)).thenReturn(usuario);

        // Configurando o comportamento do mock usuarioRepository para retornar o próprio objeto usuario simulado
        // quando o método save é chamado com o objeto usuario como argumento.
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        // Configurando o comportamento do mock modelMapper para mapear o objeto usuario para a classe UsuarioResponseDTO
        // e retornar o objeto usuarioResponseDTO simulado.
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(usuarioResponseDTO);

        // Chamando o método atualizar do userService para testá-lo.
        UsuarioResponseDTO usuarioResponseDTOatualizado = userService.atualizar(id, usuarioRequestDTO);

        // Verificando se o resultado retornado não é nulo.
        assertNotNull(usuarioResponseDTOatualizado);

        // Verificando se os campos do objeto usuarioResponseDTO retornado são iguais aos campos do objeto usuarioResponseDTO simulado.
        assertEquals(usuarioResponseDTO.getId(), usuarioResponseDTOatualizado.getId());
        assertEquals(usuarioResponseDTO.getNome(), usuarioResponseDTOatualizado.getNome());
        assertEquals(usuarioResponseDTO.getCodigo(), usuarioResponseDTOatualizado.getCodigo());

        // Verificando se o método findById do mock usuarioRepository foi chamado exatamente uma vez com o valor específico de id.
        verify(usuarioRepository, times(1)).findById(id);

        // Verificando se o método map do mock modelMapper foi chamado para mapear o objeto usuarioRequestDTO para a classe Usuario.
        // Não estamos especificando o número exato de vezes, apenas verificando se o método foi chamado ao menos uma vez.
        verify(modelMapper).map(usuarioRequestDTO, Usuario.class);

        // Verificando se o método save do mock usuarioRepository foi chamado exatamente uma vez com o objeto usuario simulado como argumento.
        verify(usuarioRepository, times(1)).save(usuario);

        // Verificando se o método map do mock modelMapper foi chamado para mapear o objeto usuario para a classe UsuarioResponseDTO.
        // Não estamos especificando o número exato de vezes, apenas verificando se o método foi chamado ao menos uma vez.
        verify(modelMapper).map(usuario, UsuarioResponseDTO.class);
    }

    @ParameterizedTest(name = "Teste com ID: {0}")
    @MethodSource("idsParaTeste")
    @DisplayName("Teste para deletar um usuário, buscando pelo ID")
    void testDeletar(Long id) {
        // Configurando o comportamento do mock usuarioRepository para retornar um Optional contendo o objeto usuario simulado
        // quando o método findById é chamado com o valor específico de id.
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // Configurando o comportamento do mock usuarioRepository para não fazer nada (simulando o comportamento do método delete)
        // quando o método delete é chamado com o objeto usuario como argumento.
        doNothing().when(usuarioRepository).delete(usuario);

        // Chamando o método deletar do userService para testá-lo.
        userService.deletar(id);

        // Verificando se o método findById do mock usuarioRepository foi chamado exatamente uma vez com o valor específico de id.
        verify(usuarioRepository, times(1)).findById(id);

        // Verificando se o método delete do mock usuarioRepository foi chamado exatamente uma vez com o objeto usuario simulado como argumento.
        verify(usuarioRepository, times(1)).delete(usuario);
    }
}
