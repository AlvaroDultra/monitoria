import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Script para gerar hash BCrypt da senha do usuário admin
 * 
 * Compilar e executar:
 * javac -cp ".:target/classes:$(find ~/.m2/repository -name '*.jar' | grep -E '(bcrypt|spring-security)' | tr '\n' ':')" criar-usuario-admin.java
 * 
 * Ou usar o Maven para executar:
 * mvn exec:java -Dexec.mainClass="CriarUsuarioAdmin" -Dexec.classpathScope=compile
 */
public class CriarUsuarioAdmin {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senha = "admin123"; // Altere aqui se quiser outra senha
        String hash = encoder.encode(senha);
        
        System.out.println("=========================================");
        System.out.println("HASH BCrypt gerado para a senha: " + senha);
        System.out.println("=========================================");
        System.out.println(hash);
        System.out.println("=========================================");
        System.out.println("\nSQL para inserir o usuário admin:");
        System.out.println("=========================================");
        System.out.println("INSERT INTO usuarios (username, email, password, role, ativo)");
        System.out.println("VALUES ('admin', 'admin@ucsal.br', '" + hash + "', 'ROLE_ADMIN', true);");
        System.out.println("=========================================");
    }
}

