#!/bin/bash

# ==============================================================================
# SCRIPT DE TESTE AUTOMATIZADO - API MonitoriaWeb
# ==============================================================================

BASE_URL="http://localhost:8080/api"
COLOR_GREEN='\033[0;32m'
COLOR_RED='\033[0;31m'
COLOR_YELLOW='\033[1;33m'
COLOR_BLUE='\033[0;34m'
COLOR_NC='\033[0m' # No Color

# Variáveis para armazenar tokens e IDs
ADMIN_TOKEN=""
PROFESSOR_TOKEN=""
ESCOLA_ID=""
PROFESSOR_ID=""
DISCIPLINA_ID=""
ALUNO_ID=""
MONITORIA_ID=""
MONITOR_ID=""
FREQUENCIA_ID=""
ASSUNTO_ID=""

# Função para imprimir seção
print_section() {
    echo -e "\n${COLOR_BLUE}=========================================${COLOR_NC}"
    echo -e "${COLOR_BLUE}$1${COLOR_NC}"
    echo -e "${COLOR_BLUE}=========================================${COLOR_NC}\n"
}

# Função para imprimir teste
print_test() {
    echo -e "${COLOR_YELLOW}[$1] $2${COLOR_NC}"
}

# Função para fazer requisição e extrair JSON
make_request() {
    local method=$1
    local url=$2
    local headers=$3
    local data=$4
    local extract_field=$5
    local extracted_value=""
    
    if [ -z "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$url" $headers 2>&1)
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$url" $headers -d "$data" 2>&1)
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_code" -ge 200 ] && [ "$http_code" -lt 300 ]; then
        echo -e "${COLOR_GREEN}✓ Status: $http_code${COLOR_NC}"
        if [ ! -z "$extract_field" ]; then
            # Tenta extrair string primeiro
            extracted_value=$(echo "$body" | grep -o "\"$extract_field\"[[:space:]]*:[[:space:]]*\"[^\"]*\"" | cut -d'"' -f4)
            if [ -z "$extracted_value" ]; then
                # Tenta extrair número
                extracted_value=$(echo "$body" | grep -o "\"$extract_field\"[[:space:]]*:[[:space:]]*[0-9]*" | grep -o "[0-9]*$")
            fi
            if [ ! -z "$extracted_value" ]; then
                echo "$extracted_value"
            fi
        fi
        # Formata JSON se possível
        if command -v jq &> /dev/null; then
            echo "$body" | jq . 2>/dev/null || echo "$body"
        elif command -v python3 &> /dev/null; then
            echo "$body" | python3 -m json.tool 2>/dev/null || echo "$body"
        else
            echo "$body"
        fi
        return 0
    else
        echo -e "${COLOR_RED}✗ Status: $http_code${COLOR_NC}"
        # Formata JSON se possível
        if command -v jq &> /dev/null; then
            echo "$body" | jq . 2>/dev/null || echo "$body"
        elif command -v python3 &> /dev/null; then
            echo "$body" | python3 -m json.tool 2>/dev/null || echo "$body"
        else
            echo "$body"
        fi
        return 1
    fi
}

# Verificar se a aplicação está rodando
print_section "VERIFICANDO SE A APLICAÇÃO ESTÁ RODANDO"
http_code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/auth/login" 2>/dev/null || echo "000")
if [ "$http_code" = "000" ] || [ "$http_code" = "" ]; then
    echo -e "${COLOR_RED}ERRO: A aplicação não está respondendo em $BASE_URL${COLOR_NC}"
    echo -e "${COLOR_YELLOW}Por favor, inicie a aplicação Spring Boot primeiro.${COLOR_NC}"
    exit 1
fi
echo -e "${COLOR_GREEN}✓ Aplicação está rodando (HTTP $http_code)${COLOR_NC}"

# ==============================================================================
# 1. AUTENTICAÇÃO
# ==============================================================================
print_section "1. TESTES DE AUTENTICAÇÃO"

print_test "1.1" "Login - Admin"
result=$(make_request "POST" "$BASE_URL/auth/login" \
    "-H \"Content-Type: application/json\"" \
    '{"username":"admin","password":"admin123"}' \
    "token")
if [ ! -z "$result" ]; then
    ADMIN_TOKEN=$(echo "$result" | head -n1)
    echo -e "${COLOR_GREEN}Token capturado: ${ADMIN_TOKEN:0:20}...${COLOR_NC}"
fi

if [ -z "$ADMIN_TOKEN" ]; then
    echo -e "${COLOR_RED}ERRO: Não foi possível obter token de admin. Verifique se o usuário admin existe no banco.${COLOR_NC}"
    echo -e "${COLOR_YELLOW}Você pode criar o usuário admin executando o SQL no banco de dados.${COLOR_NC}"
    exit 1
fi

# ==============================================================================
# 2. ESCOLAS (ADMIN)
# ==============================================================================
print_section "2. GESTÃO DE ESCOLAS (ADMIN)"

print_test "2.1" "Criar Escola"
result=$(make_request "POST" "$BASE_URL/admin/escolas" \
    "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $ADMIN_TOKEN\"" \
    '{"nome":"Escola de Engenharias e Ciências Tecnológicas","tipo":"ENGENHARIAS_CIENCIAS_TECNOLOGICAS","ativo":true}' \
    "id")
if [ ! -z "$result" ]; then
    ESCOLA_ID=$(echo "$result" | head -n1)
    echo -e "${COLOR_GREEN}Escola ID: $ESCOLA_ID${COLOR_NC}"
fi

print_test "2.2" "Listar Todas as Escolas"
make_request "GET" "$BASE_URL/admin/escolas" \
    "-H \"Authorization: Bearer $ADMIN_TOKEN\"" \
    "" ""

print_test "2.3" "Listar Escolas Ativas"
make_request "GET" "$BASE_URL/admin/escolas/ativas" \
    "-H \"Authorization: Bearer $ADMIN_TOKEN\"" \
    "" ""

if [ ! -z "$ESCOLA_ID" ]; then
    print_test "2.4" "Buscar Escola por ID"
    make_request "GET" "$BASE_URL/admin/escolas/$ESCOLA_ID" \
        "-H \"Authorization: Bearer $ADMIN_TOKEN\"" \
        "" ""
    
    print_test "2.5" "Atualizar Escola"
    make_request "PUT" "$BASE_URL/admin/escolas/$ESCOLA_ID" \
        "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $ADMIN_TOKEN\"" \
        '{"nome":"Escola de Engenharias e Ciências Tecnológicas - UCSAL","tipo":"ENGENHARIAS_CIENCIAS_TECNOLOGICAS","ativo":true}' \
        ""
fi

# ==============================================================================
# 3. PROFESSORES (ADMIN)
# ==============================================================================
print_section "3. GESTÃO DE PROFESSORES (ADMIN)"

if [ -z "$ESCOLA_ID" ]; then
    ESCOLA_ID=1  # Usar ID padrão se não foi criado
fi

print_test "3.1" "Criar Professor"
result=$(make_request "POST" "$BASE_URL/admin/professores" \
    "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $ADMIN_TOKEN\"" \
    "{\"numeroRegistro\":\"PROF001\",\"nomeCompleto\":\"João da Silva\",\"formacao\":\"MESTRADO\",\"nomeInstituicao\":\"UCSAL\",\"nomeCurso\":\"Ciência da Computação\",\"anoConclusao\":2020,\"escolaId\":$ESCOLA_ID,\"username\":\"joao.silva\",\"email\":\"joao.silva@ucsal.br\",\"password\":\"senha123\"}" \
    "id")
if [ ! -z "$result" ]; then
    PROFESSOR_ID=$(echo "$result" | head -n1)
    echo -e "${COLOR_GREEN}Professor ID: $PROFESSOR_ID${COLOR_NC}"
fi

print_test "3.2" "Login - Professor"
result=$(make_request "POST" "$BASE_URL/auth/login" \
    "-H \"Content-Type: application/json\"" \
    '{"username":"joao.silva","password":"senha123"}' \
    "token")
if [ ! -z "$result" ]; then
    PROFESSOR_TOKEN=$(echo "$result" | head -n1)
    echo -e "${COLOR_GREEN}Token capturado: ${PROFESSOR_TOKEN:0:20}...${COLOR_NC}"
fi

print_test "3.3" "Listar Todos os Professores"
make_request "GET" "$BASE_URL/admin/professores" \
    "-H \"Authorization: Bearer $ADMIN_TOKEN\"" \
    "" ""

print_test "3.4" "Listar Professores Ativos"
make_request "GET" "$BASE_URL/admin/professores/ativos" \
    "-H \"Authorization: Bearer $ADMIN_TOKEN\"" \
    "" ""

if [ ! -z "$PROFESSOR_ID" ]; then
    print_test "3.5" "Buscar Professor por ID"
    make_request "GET" "$BASE_URL/admin/professores/$PROFESSOR_ID" \
        "-H \"Authorization: Bearer $ADMIN_TOKEN\"" \
        "" ""
fi

# ==============================================================================
# 4. DISCIPLINAS (ADMIN)
# ==============================================================================
print_section "4. GESTÃO DE DISCIPLINAS (ADMIN)"

if [ -z "$PROFESSOR_ID" ]; then
    PROFESSOR_ID=1  # Usar ID padrão se não foi criado
fi

print_test "4.1" "Criar Disciplina"
result=$(make_request "POST" "$BASE_URL/admin/disciplinas" \
    "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $ADMIN_TOKEN\"" \
    "{\"sigla\":\"ADS001\",\"nome\":\"Estruturas de Dados\",\"descricao\":\"Disciplina sobre estruturas de dados fundamentais\",\"cargaHoraria\":\"80h\",\"curso\":\"Análise e Desenvolvimento de Sistemas\",\"matrizVinculada\":\"2024.1\",\"escolaId\":$ESCOLA_ID,\"professorId\":$PROFESSOR_ID}" \
    "id")
if [ ! -z "$result" ]; then
    DISCIPLINA_ID=$(echo "$result" | head -n1)
    echo -e "${COLOR_GREEN}Disciplina ID: $DISCIPLINA_ID${COLOR_NC}"
fi

print_test "4.2" "Listar Todas as Disciplinas"
make_request "GET" "$BASE_URL/admin/disciplinas" \
    "-H \"Authorization: Bearer $ADMIN_TOKEN\"" \
    "" ""

if [ ! -z "$DISCIPLINA_ID" ]; then
    print_test "4.3" "Buscar Disciplina por ID"
    make_request "GET" "$BASE_URL/admin/disciplinas/$DISCIPLINA_ID" \
        "-H \"Authorization: Bearer $ADMIN_TOKEN\"" \
        "" ""
    
    print_test "4.4" "Atualizar Disciplina"
    make_request "PUT" "$BASE_URL/admin/disciplinas/$DISCIPLINA_ID" \
        "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $ADMIN_TOKEN\"" \
        "{\"sigla\":\"ADS001\",\"nome\":\"Estruturas de Dados Avançadas\",\"descricao\":\"Disciplina sobre estruturas de dados fundamentais e avançadas\",\"cargaHoraria\":\"80h\",\"curso\":\"Análise e Desenvolvimento de Sistemas\",\"matrizVinculada\":\"2024.1\",\"escolaId\":$ESCOLA_ID,\"professorId\":$PROFESSOR_ID}" \
        ""
fi

# ==============================================================================
# 5. ALUNOS (PROFESSOR)
# ==============================================================================
print_section "5. GESTÃO DE ALUNOS (PROFESSOR)"

if [ -z "$PROFESSOR_TOKEN" ]; then
    echo -e "${COLOR_RED}ERRO: Token do professor não disponível. Pulando testes de alunos.${COLOR_NC}"
else
    if [ -z "$DISCIPLINA_ID" ]; then
        DISCIPLINA_ID=1  # Usar ID padrão se não foi criado
    fi
    
    print_test "5.1" "Criar Aluno"
    result=$(make_request "POST" "$BASE_URL/professor/alunos" \
        "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
        "{\"matricula\":\"20241234\",\"nomeCompleto\":\"Maria Santos\",\"disciplinaId\":$DISCIPLINA_ID,\"semestre\":3}" \
        "id")
    if [ ! -z "$result" ]; then
        ALUNO_ID=$(echo "$result" | head -n1)
        echo -e "${COLOR_GREEN}Aluno ID: $ALUNO_ID${COLOR_NC}"
    fi
    
    print_test "5.2" "Listar Meus Alunos"
    make_request "GET" "$BASE_URL/professor/alunos" \
        "-H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
        "" ""
    
    if [ ! -z "$DISCIPLINA_ID" ]; then
        print_test "5.3" "Listar Alunos por Disciplina"
        make_request "GET" "$BASE_URL/professor/alunos/disciplina/$DISCIPLINA_ID" \
            "-H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
            "" ""
    fi
    
    if [ ! -z "$ALUNO_ID" ]; then
        print_test "5.4" "Buscar Aluno por ID"
        make_request "GET" "$BASE_URL/professor/alunos/$ALUNO_ID" \
            "-H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
            "" ""
        
        print_test "5.5" "Atualizar Aluno"
        make_request "PUT" "$BASE_URL/professor/alunos/$ALUNO_ID" \
            "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
            "{\"matricula\":\"20241234\",\"nomeCompleto\":\"Maria Santos Silva\",\"disciplinaId\":$DISCIPLINA_ID,\"semestre\":4}" \
            ""
    fi
fi

# ==============================================================================
# 6. MONITORIAS (PROFESSOR)
# ==============================================================================
print_section "6. GESTÃO DE MONITORIAS (PROFESSOR)"

if [ -z "$PROFESSOR_TOKEN" ]; then
    echo -e "${COLOR_RED}ERRO: Token do professor não disponível. Pulando testes de monitorias.${COLOR_NC}"
else
    if [ -z "$DISCIPLINA_ID" ]; then
        DISCIPLINA_ID=1  # Usar ID padrão se não foi criado
    fi
    
    print_test "6.1" "Criar Monitoria"
    result=$(make_request "POST" "$BASE_URL/professor/monitorias" \
        "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
        "{\"disciplinaId\":$DISCIPLINA_ID,\"tipo\":\"PRESENCIAL\",\"local\":\"Sala 301\",\"dataInicio\":\"2024-03-01\",\"dataEncerramento\":\"2024-06-30\",\"horaInicio\":\"14:00:00\",\"horaEncerramento\":\"16:00:00\",\"curso\":\"Análise e Desenvolvimento de Sistemas\",\"semestre\":1}" \
        "id")
    if [ ! -z "$result" ]; then
        MONITORIA_ID=$(echo "$result" | head -n1)
        echo -e "${COLOR_GREEN}Monitoria ID: $MONITORIA_ID${COLOR_NC}"
    fi
    
    print_test "6.2" "Listar Minhas Monitorias"
    make_request "GET" "$BASE_URL/professor/monitorias" \
        "-H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
        "" ""
    
    print_test "6.3" "Listar Monitorias em Andamento"
    make_request "GET" "$BASE_URL/professor/monitorias/em-andamento" \
        "-H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
        "" ""
    
    if [ ! -z "$MONITORIA_ID" ]; then
        print_test "6.4" "Buscar Monitoria por ID"
        make_request "GET" "$BASE_URL/professor/monitorias/$MONITORIA_ID" \
            "-H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
            "" ""
        
        print_test "6.5" "Atualizar Monitoria"
        make_request "PUT" "$BASE_URL/professor/monitorias/$MONITORIA_ID" \
            "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
            "{\"disciplinaId\":$DISCIPLINA_ID,\"tipo\":\"REMOTO\",\"local\":\"Google Meet\",\"dataInicio\":\"2024-03-01\",\"dataEncerramento\":\"2024-06-30\",\"horaInicio\":\"14:00:00\",\"horaEncerramento\":\"16:00:00\",\"curso\":\"Análise e Desenvolvimento de Sistemas\",\"semestre\":1}" \
            ""
        
        if [ ! -z "$ALUNO_ID" ]; then
            print_test "6.6" "Associar Aluno à Monitoria"
            make_request "POST" "$BASE_URL/professor/monitorias/associar-aluno" \
                "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
                "{\"alunoId\":$ALUNO_ID,\"monitoriaId\":$MONITORIA_ID}" \
                "id"
            # O resultado pode conter o monitorId
        fi
        
        print_test "6.7" "Contar Alunos na Monitoria"
        make_request "GET" "$BASE_URL/professor/monitorias/$MONITORIA_ID/quantidade-alunos" \
            "-H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
            "" ""
    fi
fi

# ==============================================================================
# 7. FREQUÊNCIAS (PROFESSOR)
# ==============================================================================
print_section "7. GESTÃO DE FREQUÊNCIAS (PROFESSOR)"

if [ -z "$PROFESSOR_TOKEN" ]; then
    echo -e "${COLOR_RED}ERRO: Token do professor não disponível. Pulando testes de frequências.${COLOR_NC}"
else
    if [ ! -z "$ALUNO_ID" ] && [ ! -z "$MONITORIA_ID" ]; then
        print_test "7.1" "Registrar Frequência"
        result=$(make_request "POST" "$BASE_URL/professor/frequencias" \
            "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
            "{\"alunoId\":$ALUNO_ID,\"monitoriaId\":$MONITORIA_ID,\"data\":\"2024-03-15\",\"presente\":true,\"observacao\":\"Aluno participou ativamente\"}" \
            "id")
        if [ ! -z "$result" ]; then
            FREQUENCIA_ID=$(echo "$result" | head -n1)
            echo -e "${COLOR_GREEN}Frequência ID: $FREQUENCIA_ID${COLOR_NC}"
        fi
        
        if [ ! -z "$FREQUENCIA_ID" ]; then
            print_test "7.2" "Atualizar Frequência"
            make_request "PATCH" "$BASE_URL/professor/frequencias/$FREQUENCIA_ID?presente=false" \
                "-H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
                "" ""
        fi
    fi
fi

# ==============================================================================
# 8. ASSUNTOS (PROFESSOR)
# ==============================================================================
print_section "8. GESTÃO DE ASSUNTOS (PROFESSOR)"

if [ -z "$PROFESSOR_TOKEN" ]; then
    echo -e "${COLOR_RED}ERRO: Token do professor não disponível. Pulando testes de assuntos.${COLOR_NC}"
else
    if [ ! -z "$MONITORIA_ID" ]; then
        print_test "8.1" "Registrar Assunto"
        result=$(make_request "POST" "$BASE_URL/professor/assuntos" \
            "-H \"Content-Type: application/json\" -H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
            "{\"monitoriaId\":$MONITORIA_ID,\"data\":\"2024-03-15\",\"assunto\":\"Listas Ligadas\",\"atividadePraticaAplicada\":\"Implementação de lista encadeada em Java\"}" \
            "id")
        if [ ! -z "$result" ]; then
            ASSUNTO_ID=$(echo "$result" | head -n1)
            echo -e "${COLOR_GREEN}Assunto ID: $ASSUNTO_ID${COLOR_NC}"
        fi
        
        print_test "8.2" "Listar Assuntos por Monitoria"
        make_request "GET" "$BASE_URL/professor/assuntos/monitoria/$MONITORIA_ID" \
            "-H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
            "" ""
        
        if [ ! -z "$ASSUNTO_ID" ]; then
            print_test "8.3" "Excluir Assunto"
            make_request "DELETE" "$BASE_URL/professor/assuntos/$ASSUNTO_ID" \
                "-H \"Authorization: Bearer $PROFESSOR_TOKEN\"" \
                "" ""
        fi
    fi
fi

# ==============================================================================
# RESUMO
# ==============================================================================
print_section "RESUMO DOS TESTES"

echo -e "${COLOR_GREEN}Testes concluídos!${COLOR_NC}"
echo ""
echo "IDs capturados:"
echo "  - Escola ID: ${ESCOLA_ID:-N/A}"
echo "  - Professor ID: ${PROFESSOR_ID:-N/A}"
echo "  - Disciplina ID: ${DISCIPLINA_ID:-N/A}"
echo "  - Aluno ID: ${ALUNO_ID:-N/A}"
echo "  - Monitoria ID: ${MONITORIA_ID:-N/A}"
echo "  - Frequência ID: ${FREQUENCIA_ID:-N/A}"
echo "  - Assunto ID: ${ASSUNTO_ID:-N/A}"

