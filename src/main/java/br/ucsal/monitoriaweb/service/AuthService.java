package br.ucsal.monitoriaweb.service;

import br.ucsal.monitoriaweb.config.JwtUtil;
import br.ucsal.monitoriaweb.dto.request.LoginRequest;
import br.ucsal.monitoriaweb.dto.response.JwtResponse;
import br.ucsal.monitoriaweb.entity.Usuario;
import br.ucsal.monitoriaweb.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new JwtResponse(
                token,
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRole().name()
        );
    }
}
