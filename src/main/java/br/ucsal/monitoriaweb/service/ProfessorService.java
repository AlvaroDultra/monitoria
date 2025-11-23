package br.ucsal.monitoriaweb.service;

import br.ucsal.monitoriaweb.dto.request.ProfessorRequest;
import br.ucsal.monitoriaweb.dto.response.ProfessorResponse;
import br.ucsal.monitoriaweb.entity.Escola;
import br.ucsal.monitoriaweb.entity.Professor;
import br.ucsal.monitoriaweb.entity.Role;
import br.ucsal.monitoriaweb.entity.Usuario;
import br.ucsal.monitoriaweb.exception.BusinessException;
import br.ucsal.monitoriaweb.exception.ResourceNotFoundException;
import br.ucsal.monitoriaweb.repository.EscolaRepository;
import br.ucsal.monitoriaweb.repository.ProfessorRepository;
import br.ucsal.monitoriaweb.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;
    private final EscolaRepository escolaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ProfessorResponse criar(ProfessorRequest request) {
        if (professorRepository.existsByNumeroRegistro(request.getNumeroRegistro())) {
            throw new BusinessException("Já existe um professor com este número de registro");
        }

        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Já existe um usuário com este username");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe um usuário com este email");
        }

        Escola escola = escolaRepository.findById(request.getEscolaId())
                .orElseThrow(() -> new ResourceNotFoundException("Escola", "id", request.getEscolaId()));

        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEmail(request.getEmail());
        usuario.setRole(Role.ROLE_PROFESSOR);
        usuario.setAtivo(true);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Criar professor
        Professor professor = new Professor();
        professor.setNumeroRegistro(request.getNumeroRegistro());
        professor.setNomeCompleto(request.getNomeCompleto());
        professor.setFormacao(request.getFormacao());
        professor.setNomeInstituicao(request.getNomeInstituicao());
        professor.setNomeCurso(request.getNomeCurso());
        professor.setAnoConclusao(request.getAnoConclusao());
        professor.setUsuario(usuarioSalvo);
        professor.setEscola(escola);
        professor.setAtivo(true);

        Professor professorSalvo = professorRepository.save(professor);
        return mapToResponse(professorSalvo);
    }

    @Transactional(readOnly = true)
    public ProfessorResponse buscarPorId(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor", "id", id));
        return mapToResponse(professor);
    }

    @Transactional(readOnly = true)
    public List<ProfessorResponse> listarTodos() {
        return professorRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProfessorResponse> listarAtivos() {
        return professorRepository.findByAtivo(true).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void inativar(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor", "id", id));
        professor.setAtivo(false);
        professor.getUsuario().setAtivo(false);
        professorRepository.save(professor);
    }

    @Transactional
    public void ativar(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor", "id", id));
        professor.setAtivo(true);
        professor.getUsuario().setAtivo(true);
        professorRepository.save(professor);
    }

    @Transactional
    public void deletar(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor", "id", id));
        Usuario usuario = professor.getUsuario();
        professorRepository.delete(professor);
        usuarioRepository.delete(usuario);
    }

    private ProfessorResponse mapToResponse(Professor professor) {
        ProfessorResponse response = new ProfessorResponse();
        response.setId(professor.getId());
        response.setNumeroRegistro(professor.getNumeroRegistro());
        response.setNomeCompleto(professor.getNomeCompleto());
        response.setFormacao(professor.getFormacao());
        response.setFormacaoDescricao(professor.getFormacao().getDescricao());
        response.setNomeInstituicao(professor.getNomeInstituicao());
        response.setNomeCurso(professor.getNomeCurso());
        response.setAnoConclusao(professor.getAnoConclusao());
        response.setEscolaId(professor.getEscola().getId());
        response.setEscolaNome(professor.getEscola().getNome());
        response.setUsername(professor.getUsuario().getUsername());
        response.setEmail(professor.getUsuario().getEmail());
        response.setAtivo(professor.getAtivo());
        return response;
    }
}
