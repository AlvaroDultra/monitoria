#!/bin/bash

# ==============================================================================
# SCRIPT DE TESTE COMPLETO - API MonitoriaWeb
# ==============================================================================

BASE_URL="http://localhost:8080/api"
LOG_FILE="test-results-$(date +%Y%m%d-%H%M%S).log"

# Cores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Contadores
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Variáveis
ADMIN_TOKEN=""
PROFESSOR_TOKEN=""
ESCOLA_ID=""
PROFESSOR_ID=""
DISCIPLINA_ID=""
ALUNO_ID=""
MONITORIA_ID=""

log() {
    echo "$1" | tee -a "$LOG_FILE"
}

test_endpoint() {
    local name="$1"
    local method="$2"
    local url="$3"
    local headers="$4"
    local data="$5"
    local expected_status="${6:-200}"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    log ""
    log "${YELLOW}[TESTE $TOTAL_TESTS] $name${NC}"
    log "  Método: $method"
    log "  URL: $url"
    
    if [ -z "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$url" $headers 2>&1)
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$url" $headers -d "$data" 2>&1)
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_code" = "$expected_status" ]; then
        log "  ${GREEN}✓ PASSOU (HTTP $http_code)${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
        echo "$body"
        return 0
    else
        log "  ${RED}✗ FALHOU (HTTP $http_code, esperado $expected_status)${NC}"
        FAILED_TESTS=$((FAILED_TESTS + 1))
        if [ ! -z "$body" ]; then
            log "  Resposta: $body"
        fi
        return 1
    fi
}

extract_json_field() {
    local json="$1"
    local field="$2"
    echo "$json" | grep -o "\"$field\"[[:space:]]*:[[:space:]]*\"[^\"]*\"" | cut -d'"' -f4 || \
    echo "$json" | grep -o "\"$field\"[[:space:]]*:[[:space:]]*[0-9]*" | grep -o "[0-9]*$"
}

log "${BLUE}=========================================${NC}"
log "${BLUE}TESTE COMPLETO DOS ENDPOINTS${NC}"
log "${BLUE}=========================================${NC}"
log "Data: $(date)"
log "Base URL: $BASE_URL"
log ""

# Verificar se aplicação está rodando
log "${BLUE}[VERIFICAÇÃO INICIAL]${NC}"
status=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/auth/login" 2>/dev/null || echo "000")
if [ "$status" = "000" ]; then
    log "${RED}ERRO: Aplicação não está respondendo${NC}"
    log "Por favor, inicie a aplicação Spring Boot primeiro"
    exit 1
fi
log "${GREEN}✓ Aplicação está respondendo (HTTP $status)${NC}"
log ""

# ==============================================================================
# 1. AUTENTICAÇÃO
# ==============================================================================
log "${BLUE}=========================================${NC}"
log "${BLUE}1. TESTES DE AUTENTICAÇÃO${NC}"
log "${BLUE}=========================================${NC}"

result=$(test_endpoint "Login Admin" "POST" "$BASE_URL/auth/login" \
    '-H "Content-Type: application/json" -H "Origin: http://localhost:4200"' \
    '{"username":"admin","password":"admin123"}' \
    "200")

if [ $? -eq 0 ]; then
    ADMIN_TOKEN=$(extract_json_field "$result" "token")
    if [ ! -z "$ADMIN_TOKEN" ]; then
        log "${GREEN}Token admin capturado: ${ADMIN_TOKEN:0:30}...${NC}"
    fi
fi

# ==============================================================================
# 2. ESCOLAS (ADMIN)
# ==============================================================================
log ""
log "${BLUE}=========================================${NC}"
log "${BLUE}2. GESTÃO DE ESCOLAS (ADMIN)${NC}"
log "${BLUE}=========================================${NC}"

if [ ! -z "$ADMIN_TOKEN" ]; then
    result=$(test_endpoint "Criar Escola" "POST" "$BASE_URL/admin/escolas" \
        "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $ADMIN_TOKEN\" -H \"Origin: http://localhost:4200\"" \
        '{"nome":"Escola de Engenharias e Ciências Tecnológicas","tipo":"ENGENHARIAS_CIENCIAS_TECNOLOGICAS","ativo":true}' \
        "201")
    
    if [ $? -eq 0 ]; then
        ESCOLA_ID=$(extract_json_field "$result" "id")
        log "${GREEN}Escola ID: $ESCOLA_ID${NC}"
    fi
    
    test_endpoint "Listar Todas as Escolas" "GET" "$BASE_URL/admin/escolas" \
        "-H \"Authorization: Bearer $ADMIN_TOKEN\" -H \"Origin: http://localhost:4200\"" \
        "" "200"
    
    test_endpoint "Listar Escolas Ativas" "GET" "$BASE_URL/admin/escolas/ativas" \
        "-H \"Authorization: Bearer $ADMIN_TOKEN\" -H \"Origin: http://localhost:4200\"" \
        "" "200"
    
    if [ ! -z "$ESCOLA_ID" ]; then
        test_endpoint "Buscar Escola por ID" "GET" "$BASE_URL/admin/escolas/$ESCOLA_ID" \
            "-H \"Authorization: Bearer $ADMIN_TOKEN\" -H \"Origin: http://localhost:4200\"" \
            "" "200"
    fi
else
    log "${RED}Pulando testes de escolas - token admin não disponível${NC}"
fi

# ==============================================================================
# 3. PROFESSORES (ADMIN)
# ==============================================================================
log ""
log "${BLUE}=========================================${NC}"
log "${BLUE}3. GESTÃO DE PROFESSORES (ADMIN)${NC}"
log "${BLUE}=========================================${NC}"

if [ ! -z "$ADMIN_TOKEN" ]; then
    if [ -z "$ESCOLA_ID" ]; then
        ESCOLA_ID=1
    fi
    
    result=$(test_endpoint "Criar Professor" "POST" "$BASE_URL/admin/professores" \
        "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $ADMIN_TOKEN\" -H \"Origin: http://localhost:4200\"" \
        "{\"numeroRegistro\":\"PROF001\",\"nomeCompleto\":\"João da Silva\",\"formacao\":\"MESTRADO\",\"nomeInstituicao\":\"UCSAL\",\"nomeCurso\":\"Ciência da Computação\",\"anoConclusao\":2020,\"escolaId\":$ESCOLA_ID,\"username\":\"joao.silva\",\"email\":\"joao.silva@ucsal.br\",\"password\":\"senha123\"}" \
        "201")
    
    if [ $? -eq 0 ]; then
        PROFESSOR_ID=$(extract_json_field "$result" "id")
        log "${GREEN}Professor ID: $PROFESSOR_ID${NC}"
    fi
    
    # Login do professor
    result=$(test_endpoint "Login Professor" "POST" "$BASE_URL/auth/login" \
        '-H "Content-Type: application/json" -H "Origin: http://localhost:4200"' \
        '{"username":"joao.silva","password":"senha123"}' \
        "200")
    
    if [ $? -eq 0 ]; then
        PROFESSOR_TOKEN=$(extract_json_field "$result" "token")
        if [ ! -z "$PROFESSOR_TOKEN" ]; then
            log "${GREEN}Token professor capturado: ${PROFESSOR_TOKEN:0:30}...${NC}"
        fi
    fi
    
    test_endpoint "Listar Todos os Professores" "GET" "$BASE_URL/admin/professores" \
        "-H \"Authorization: Bearer $ADMIN_TOKEN\" -H \"Origin: http://localhost:4200\"" \
        "" "200"
else
    log "${RED}Pulando testes de professores - token admin não disponível${NC}"
fi

# ==============================================================================
# RESUMO
# ==============================================================================
log ""
log "${BLUE}=========================================${NC}"
log "${BLUE}RESUMO DOS TESTES${NC}"
log "${BLUE}=========================================${NC}"
log "Total de testes: $TOTAL_TESTS"
log "${GREEN}Testes passados: $PASSED_TESTS${NC}"
log "${RED}Testes falhados: $FAILED_TESTS${NC}"
log ""
log "IDs capturados:"
log "  - Escola ID: ${ESCOLA_ID:-N/A}"
log "  - Professor ID: ${PROFESSOR_ID:-N/A}"
log "  - Disciplina ID: ${DISCIPLINA_ID:-N/A}"
log "  - Aluno ID: ${ALUNO_ID:-N/A}"
log "  - Monitoria ID: ${MONITORIA_ID:-N/A}"
log ""
log "Log salvo em: $LOG_FILE"

