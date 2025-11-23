#!/bin/bash

# Script para criar usuário admin no banco de dados
# Este script gera o hash BCrypt e cria o SQL necessário

echo "========================================="
echo "CRIAR USUÁRIO ADMIN - MonitoriaWeb"
echo "========================================="
echo ""

# Verificar se o Maven está disponível
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven não encontrado. Instalando dependências manualmente..."
    echo ""
    echo "Opção 1: Use um gerador BCrypt online:"
    echo "  https://www.bcrypt-generator.com/"
    echo "  Senha: admin123"
    echo ""
    echo "Opção 2: Use o script SQL abaixo com hash pré-gerado:"
    echo ""
    echo "INSERT INTO usuarios (username, email, password, role, ativo)"
    echo "VALUES ('admin', 'admin@ucsal.br', '\$2a\$10\$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_ADMIN', true);"
    echo ""
    exit 1
fi

echo "Gerando hash BCrypt para a senha 'admin123'..."
echo ""

# Criar um arquivo Java temporário para gerar o hash
cat > /tmp/GenerateHash.java << 'EOF'
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senha = "admin123";
        String hash = encoder.encode(senha);
        System.out.println(hash);
    }
}
EOF

# Tentar gerar o hash usando Maven
cd "$(dirname "$0")"

echo "Compilando e executando gerador de hash..."
HASH=$(mvn -q exec:java -Dexec.mainClass="GenerateHash" -Dexec.classpathScope=compile 2>/dev/null)

if [ -z "$HASH" ]; then
    echo "❌ Não foi possível gerar o hash automaticamente."
    echo ""
    echo "Use um dos métodos abaixo:"
    echo ""
    echo "MÉTODO 1: Gerador BCrypt Online"
    echo "  1. Acesse: https://www.bcrypt-generator.com/"
    echo "  2. Digite a senha: admin123"
    echo "  3. Clique em 'Generate Hash'"
    echo "  4. Copie o hash gerado"
    echo ""
    echo "MÉTODO 2: Usar hash pré-gerado (senha: admin123)"
    HASH='$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
    echo "  Hash: $HASH"
    echo ""
fi

echo "========================================="
echo "SQL PARA CRIAR USUÁRIO ADMIN"
echo "========================================="
echo ""
echo "Conecte-se ao PostgreSQL e execute:"
echo ""
echo "psql -U postgres -d monitoriaweb"
echo ""
echo "Depois execute o SQL abaixo:"
echo ""
echo "INSERT INTO usuarios (username, email, password, role, ativo)"
echo "VALUES ('admin', 'admin@ucsal.br', '$HASH', 'ROLE_ADMIN', true);"
echo ""
echo "========================================="
echo ""
echo "Ou execute diretamente:"
echo ""
echo "psql -U postgres -d monitoriaweb -c \"INSERT INTO usuarios (username, email, password, role, ativo) VALUES ('admin', 'admin@ucsal.br', '$HASH', 'ROLE_ADMIN', true);\""
echo ""
echo "========================================="
echo "CREDENCIAIS DO USUÁRIO ADMIN"
echo "========================================="
echo "Username: admin"
echo "Password: admin123"
echo "Email: admin@ucsal.br"
echo "Role: ROLE_ADMIN"
echo ""

