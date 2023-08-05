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
    @Mock //esta anotação indica que esta classe sera uma instancia simulada
    private UsuarioRepository usuarioRepository;
    @Mock
    private Usuario usuario;
    @Mock
    private UsuarioResponseDTO usuarioResponseDTO;
    @Mock
    private UsuarioRequestDTO usuarioRequestDTO;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks //Injeta automaticamente objetos simulados. Permitindo uso de dependencias simuladas
    private UsuarioService userService;
    @BeforeEach //método que será executado antes de cada teste dentro de uma classe de testes.
    public void setup(){ // sera executado antes de cada teste
        MockitoAnnotations.openMocks(this); // inicializar mock criados com @Mock
        usuario = new Usuario(); //criando objetos reais para teste
        usuarioResponseDTO = new UsuarioResponseDTO();//criando objetos reais para teste
        usuarioRequestDTO = new UsuarioRequestDTO();//criando objetos reais para teste
        // metodo setup garante que que a classe sob teste comece cada teste em um estado conhecido e limpo.
    }

    //usado para testes parametrizados, onde conseguimos realizar o mesmo teste com ids diferentes.
    private static Stream<Long> idsParaTeste(){
        return Stream.of(1L,2L,3L);
    }


    @Test
    @DisplayName("Teste para salvar um usuario")
    public void testSalvar(){
        //configurando o comportamento do mock de um maper entre request e entidade e esperando que o retorno
        // esperado, seja a entidade
        when(modelMapper.map(usuarioRequestDTO, Usuario.class)).thenReturn(usuario);

        // configurando o comportamento do mock, para quando e chamado com o save, ele retornar o proprio objeto usuario
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        //configurando o comportamento do mock, estamos mapeando para que a etidade retorne um response
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(usuarioResponseDTO);

        //invocanto metodo do service. Para teste isolado.
        UsuarioResponseDTO salvarUsuarioResponseDTO = userService.salvar(usuarioRequestDTO);

        //verificando se o valor retornado e um objeto não nulo;
        assertNotNull(salvarUsuarioResponseDTO);

        verify(usuarioRepository, times(1)).save(usuario);

        verify(modelMapper, times(1)).map(usuarioRequestDTO, Usuario.class);

        verify(modelMapper, times(1)).map(usuario, UsuarioResponseDTO.class);
    }

    @Test
    @DisplayName("Teste para buscar todos os usuarios")
    void testBuscarTodos() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(usuarioResponseDTO);

        List <UsuarioResponseDTO> usuarioResponseDTOS = userService.buscarTodos();

        assertNotNull(usuarioResponseDTOS);
        assertEquals(1, usuarioResponseDTOS.size());
        assertEquals(UsuarioResponseDTO.class, usuarioResponseDTOS.get(0).getClass());

        verify(usuarioRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(usuario, UsuarioResponseDTO.class);
    }

    @ParameterizedTest(name = "Teste com ID: {0}")
    @MethodSource("idsParaTeste")
    @DisplayName("Teste para buscar usuario por id")
    void testBuscarPorId(Long id) {
        when(usuarioRepository.findById(id)).thenReturn(Optional.ofNullable(usuario));

        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO bucarUsuarioResponseDTO = userService.buscarPorId(id);

        assertNotNull(bucarUsuarioResponseDTO);
        assertEquals(usuarioResponseDTO.getId(), bucarUsuarioResponseDTO.getId());
        assertEquals(usuarioResponseDTO.getNome(), bucarUsuarioResponseDTO.getNome());
        assertEquals(usuarioResponseDTO.getCodigo(), bucarUsuarioResponseDTO.getCodigo());
        assertEquals(usuarioResponseDTO, bucarUsuarioResponseDTO);


        verify(usuarioRepository, times(1)).findById(id);
        verify(modelMapper, times(1)).map(usuario, UsuarioResponseDTO.class);
    }


    @ParameterizedTest(name = "Teste com ID: {0}")
    @MethodSource("idsParaTeste")
    @DisplayName("teste para atualizar um usuario, buscando pelo id")
    void testAtualizar(Long id) {
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(usuarioRequestDTO, Usuario.class)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO usuarioResponseDTOatualizado = userService.atualizar(id, usuarioRequestDTO);

        assertNotNull(usuarioResponseDTOatualizado);
        assertEquals(usuarioResponseDTO.getId(), usuarioResponseDTOatualizado.getId());
        assertEquals(usuarioResponseDTO.getNome(), usuarioResponseDTOatualizado.getNome());
        assertEquals(usuarioResponseDTO.getCodigo(), usuarioResponseDTOatualizado.getCodigo());

        verify(usuarioRepository, times(1)).findById(id);
        verify(modelMapper).map(usuarioRequestDTO, Usuario.class);
        verify(usuarioRepository, times(1)).save(usuario);
        verify(modelMapper).map(usuario, UsuarioResponseDTO.class);
    }


    @ParameterizedTest(name = "Teste com ID: {0}")
    @MethodSource("idsParaTeste")
    @DisplayName("teste para deletar um usuario, buscando pelo id")
    void deletar(Long id) {
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        userService.deletar(id);

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).delete(usuario);

    }
}