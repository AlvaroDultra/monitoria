#!/bin/bash

# Script simples para testar endpoints da API MonitoriaWeb

BASE_URL="http://localhost:8080/api"

echo "========================================="
echo "TESTE SIMPLES DOS ENDPOINTS"
echo "========================================="
echo ""

# Teste 1: Verificar se a aplicação está rodando
echo "[1] Verificando se a aplicação está rodando..."
status=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/auth/login")
if [ "$status" = "000" ]; then
    echo "❌ ERRO: Aplicação não está respondendo"
    echo "   Por favor, inicie a aplicação Spring Boot primeiro"
    exit 1
fi
echo "✓ Aplicação está respondendo (HTTP $status)"
echo ""

# Teste 2: Testar login (pode falhar se usuário não existir)
echo "[2] Testando login..."
response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -H "Origin: http://localhost:4200" \
    -d '{"username":"admin","password":"admin123"}')

http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | sed '$d')

echo "Status HTTP: $http_code"
if [ "$http_code" = "200" ]; then
    echo "✓ Login bem-sucedido!"
    token=$(echo "$body" | grep -o '"token"[[:space:]]*:[[:space:]]*"[^"]*"' | cut -d'"' -f4)
    if [ ! -z "$token" ]; then
        echo "Token: ${token:0:50}..."
        echo ""
        echo "[3] Testando endpoint protegido com token..."
        response2=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/admin/escolas" \
            -H "Authorization: Bearer $token")
        http_code2=$(echo "$response2" | tail -n1)
        echo "Status HTTP: $http_code2"
        if [ "$http_code2" = "200" ]; then
            echo "✓ Endpoint protegido acessível!"
        else
            echo "✗ Endpoint protegido retornou erro"
            echo "$response2" | sed '$d'
        fi
    fi
elif [ "$http_code" = "403" ]; then
    echo "✗ Acesso negado (403)"
    echo "   Possíveis causas:"
    echo "   - Problema de CORS"
    echo "   - Filtro de segurança bloqueando"
    echo "   - Endpoint não está permitido"
elif [ "$http_code" = "401" ]; then
    echo "✗ Não autorizado (401)"
    echo "   - Credenciais inválidas ou usuário não existe"
    echo "   - Você precisa criar o usuário admin no banco de dados"
else
    echo "✗ Erro inesperado: $http_code"
    echo "Resposta: $body"
fi

echo ""
echo "========================================="
echo "TESTE CONCLUÍDO"
echo "========================================="

