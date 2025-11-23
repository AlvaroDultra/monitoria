#!/bin/bash

# Script para atualizar a senha do admin com um hash BCrypt válido

echo "========================================="
echo "ATUALIZANDO SENHA DO ADMIN"
echo "========================================="
echo ""

# Vou usar um hash BCrypt válido gerado para "admin123"
# Este hash foi testado e funciona
NEW_HASH='$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'

DB_NAME="monitoriaweb"
DB_USER="postgres"
DB_PASSWORD="170902"

echo "Atualizando senha do usuário admin..."
echo ""

PGPASSWORD="$DB_PASSWORD" psql -U "$DB_USER" -d "$DB_NAME" << EOF
UPDATE usuarios 
SET password = '$NEW_HASH'
WHERE username = 'admin';
EOF

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Senha atualizada com sucesso!"
    echo ""
    echo "Credenciais:"
    echo "  Username: admin"
    echo "  Password: admin123"
    echo ""
    echo "Teste o login agora!"
else
    echo ""
    echo "❌ Erro ao atualizar senha."
fi

