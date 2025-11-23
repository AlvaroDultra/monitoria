#!/bin/bash

# Script rápido para criar usuário admin diretamente no PostgreSQL

echo "========================================="
echo "CRIANDO USUÁRIO ADMIN NO BANCO DE DADOS"
echo "========================================="
echo ""

# Credenciais do banco (do application.properties)
DB_NAME="monitoriaweb"
DB_USER="postgres"
DB_PASSWORD="170902"

# Hash BCrypt pré-gerado para a senha "admin123"
HASH='$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'

echo "Conectando ao banco de dados..."
echo ""

# Verificar se o usuário já existe
EXISTS=$(PGPASSWORD="$DB_PASSWORD" psql -U "$DB_USER" -d "$DB_NAME" -t -c "SELECT COUNT(*) FROM usuarios WHERE username = 'admin';" 2>/dev/null | xargs)

if [ "$EXISTS" = "1" ]; then
    echo "⚠️  Usuário admin já existe no banco de dados."
    echo ""
    echo "Deseja deletar e recriar? (s/N)"
    read -r resposta
    if [ "$resposta" = "s" ] || [ "$resposta" = "S" ]; then
        echo "Deletando usuário admin existente..."
        PGPASSWORD="$DB_PASSWORD" psql -U "$DB_USER" -d "$DB_NAME" -c "DELETE FROM usuarios WHERE username = 'admin';" 2>/dev/null
        echo "✓ Usuário deletado."
    else
        echo "Operação cancelada."
        exit 0
    fi
fi

echo "Criando usuário admin..."
echo ""

# Criar o usuário
PGPASSWORD="$DB_PASSWORD" psql -U "$DB_USER" -d "$DB_NAME" << EOF
INSERT INTO usuarios (username, email, password, role, ativo)
VALUES (
    'admin',
    'admin@ucsal.br',
    '$HASH',
    'ROLE_ADMIN',
    true
)
ON CONFLICT (username) DO NOTHING;
EOF

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================="
    echo "✅ USUÁRIO ADMIN CRIADO COM SUCESSO!"
    echo "========================================="
    echo ""
    echo "Credenciais:"
    echo "  Username: admin"
    echo "  Password: admin123"
    echo "  Email: admin@ucsal.br"
    echo ""
    echo "Verificando usuário criado..."
    echo ""
    PGPASSWORD="$DB_PASSWORD" psql -U "$DB_USER" -d "$DB_NAME" -c "SELECT id, username, email, role, ativo FROM usuarios WHERE username = 'admin';"
    echo ""
    echo "========================================="
    echo "Agora você pode fazer login e testar!"
    echo "========================================="
else
    echo ""
    echo "❌ Erro ao criar usuário admin."
    echo ""
    echo "Verifique:"
    echo "  1. PostgreSQL está rodando?"
    echo "  2. Banco 'monitoriaweb' existe?"
    echo "  3. Credenciais estão corretas no script?"
    echo ""
    echo "Você pode criar manualmente executando:"
    echo "  psql -U postgres -d monitoriaweb -f criar-admin.sql"
fi

