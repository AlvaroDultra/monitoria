#!/bin/bash

 

# ==============================================================================

# TESTES CURL - API MonitoriaWeb

# ==============================================================================

# Base URL da API

BASE_URL="http://localhost:8080/api"

 

# ==============================================================================

# VARIÁVEIS PARA ARMAZENAR TOKENS E IDS

# ==============================================================================

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

 

# ==============================================================================

# 1. AUTENTICAÇÃO

# ==============================================================================

 

echo "========================================="

echo "1. TESTES DE AUTENTICAÇÃO"

echo "========================================="

 

# 1.1. Login (será necessário criar usuário admin primeiro no banco)

echo -e "\n[1.1] Login - Admin"

curl -X POST "${BASE_URL}/auth/login" \

  -H "Content-Type: application/json" \

  -d '{

    "username": "admin",

    "password": "admin123"

  }' | jq .

 

# Nota: Salve o token retornado para usar nos próximos requests

# ADMIN_TOKEN="seu_token_aqui"

 

# ==============================================================================

# 2. ESCOLAS (ADMIN)

# ==============================================================================

 

echo -e "\n========================================="

echo "2. GESTÃO DE ESCOLAS (ADMIN)"

echo "========================================="

 

# 2.1. Criar Escola

echo -e "\n[2.1] Criar Escola"

curl -X POST "${BASE_URL}/admin/escolas" \

  -H "Content-Type: application/json" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" \

  -d '{

    "nome": "Escola de Engenharias e Ciências Tecnológicas",

    "tipo": "ENGENHARIAS_CIENCIAS_TECNOLOGICAS",

    "ativo": true

  }' | jq .

 

# Salve o ID retornado

# ESCOLA_ID=1

 

# 2.2. Listar Todas as Escolas

echo -e "\n[2.2] Listar Todas as Escolas"

curl -X GET "${BASE_URL}/admin/escolas" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" | jq .

 

# 2.3. Listar Escolas Ativas

echo -e "\n[2.3] Listar Escolas Ativas"

curl -X GET "${BASE_URL}/admin/escolas/ativas" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" | jq .

 

# 2.4. Buscar Escola por ID

echo -e "\n[2.4] Buscar Escola por ID"

curl -X GET "${BASE_URL}/admin/escolas/${ESCOLA_ID}" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" | jq .

 

# 2.5. Atualizar Escola

echo -e "\n[2.5] Atualizar Escola"

curl -X PUT "${BASE_URL}/admin/escolas/${ESCOLA_ID}" \

  -H "Content-Type: application/json" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" \

  -d '{

    "nome": "Escola de Engenharias e Ciências Tecnológicas - UCSAL",

    "tipo": "ENGENHARIAS_CIENCIAS_TECNOLOGICAS",

    "ativo": true

  }' | jq .

 

# 2.6. Inativar Escola

echo -e "\n[2.6] Inativar Escola"

curl -X PATCH "${BASE_URL}/admin/escolas/${ESCOLA_ID}/inativar" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" | jq .

 

# 2.7. Ativar Escola

echo -e "\n[2.7] Ativar Escola"

curl -X PATCH "${BASE_URL}/admin/escolas/${ESCOLA_ID}/ativar" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" | jq .

 

# ==============================================================================

# 3. PROFESSORES (ADMIN)

# ==============================================================================

 

echo -e "\n========================================="

echo "3. GESTÃO DE PROFESSORES (ADMIN)"

echo "========================================="

 

# 3.1. Criar Professor

echo -e "\n[3.1] Criar Professor"

curl -X POST "${BASE_URL}/admin/professores" \

  -H "Content-Type: application/json" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" \

  -d '{

    "numeroRegistro": "PROF001",

    "nomeCompleto": "João da Silva",

    "formacao": "MESTRADO",

    "nomeInstituicao": "UCSAL",

    "nomeCurso": "Ciência da Computação",

    "anoConclusao": 2020,

    "escolaId": 1,

    "username": "joao.silva",

    "email": "joao.silva@ucsal.br",

    "password": "senha123"

  }' | jq .

 

# Salve o ID e faça login com o professor

# PROFESSOR_ID=1

 

# 3.2. Login do Professor

echo -e "\n[3.2] Login - Professor"

curl -X POST "${BASE_URL}/auth/login" \

  -H "Content-Type: application/json" \

  -d '{

    "username": "joao.silva",

    "password": "senha123"

  }' | jq .

 

# Salve o token do professor

# PROFESSOR_TOKEN="token_do_professor"

 

# 3.3. Listar Todos os Professores

echo -e "\n[3.3] Listar Todos os Professores"

curl -X GET "${BASE_URL}/admin/professores" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" | jq .

 

# 3.4. Listar Professores Ativos

echo -e "\n[3.4] Listar Professores Ativos"

curl -X GET "${BASE_URL}/admin/professores/ativos" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" | jq .

 

# 3.5. Buscar Professor por ID

echo -e "\n[3.5] Buscar Professor por ID"

curl -X GET "${BASE_URL}/admin/professores/${PROFESSOR_ID}" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" | jq .

 

# 3.6. Inativar Professor

echo -e "\n[3.6] Inativar Professor"

curl -X PATCH "${BASE_URL}/admin/professores/${PROFESSOR_ID}/inativar" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" | jq .

 

# 3.7. Ativar Professor

echo -e "\n[3.7] Ativar Professor"

curl -X PATCH "${BASE_URL}/admin/professores/${PROFESSOR_ID}/ativar" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" | jq .

 

# ==============================================================================

# 4. DISCIPLINAS (ADMIN)

# ==============================================================================

 

echo -e "\n========================================="

echo "4. GESTÃO DE DISCIPLINAS (ADMIN)"

echo "========================================="

 

# 4.1. Criar Disciplina

echo -e "\n[4.1] Criar Disciplina"

curl -X POST "${BASE_URL}/admin/disciplinas" \

  -H "Content-Type: application/json" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" \

  -d '{

    "sigla": "ADS001",

    "nome": "Estruturas de Dados",

    "descricao": "Disciplina sobre estruturas de dados fundamentais",

    "cargaHoraria": "80h",

    "curso": "Análise e Desenvolvimento de Sistemas",

    "matrizVinculada": "2024.1",

    "escolaId": 1,

    "professorId": 1

  }' | jq .

 

# Salve o ID da disciplina

# DISCIPLINA_ID=1

 

# 4.2. Listar Todas as Disciplinas

echo -e "\n[4.2] Listar Todas as Disciplinas"

curl -X GET "${BASE_URL}/admin/disciplinas" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" | jq .

 

# 4.3. Buscar Disciplina por ID

echo -e "\n[4.3] Buscar Disciplina por ID"

curl -X GET "${BASE_URL}/admin/disciplinas/${DISCIPLINA_ID}" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" | jq .

 

# 4.4. Atualizar Disciplina

echo -e "\n[4.4] Atualizar Disciplina"

curl -X PUT "${BASE_URL}/admin/disciplinas/${DISCIPLINA_ID}" \

  -H "Content-Type: application/json" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" \

  -d '{

    "sigla": "ADS001",

    "nome": "Estruturas de Dados Avançadas",

    "descricao": "Disciplina sobre estruturas de dados fundamentais e avançadas",

    "cargaHoraria": "80h",

    "curso": "Análise e Desenvolvimento de Sistemas",

    "matrizVinculada": "2024.1",

    "escolaId": 1,

    "professorId": 1

  }' | jq .

 

# 4.5. Inativar Disciplina

echo -e "\n[4.5] Inativar Disciplina"

curl -X PATCH "${BASE_URL}/admin/disciplinas/${DISCIPLINA_ID}/inativar" \

  -H "Authorization: Bearer ${ADMIN_TOKEN}" | jq .

 

# ==============================================================================

# 5. ALUNOS (PROFESSOR)

# ==============================================================================

 

echo -e "\n========================================="

echo "5. GESTÃO DE ALUNOS (PROFESSOR)"

echo "========================================="

 

# 5.1. Criar Aluno

echo -e "\n[5.1] Criar Aluno"

curl -X POST "${BASE_URL}/professor/alunos" \

  -H "Content-Type: application/json" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" \

  -d '{

    "matricula": "20241234",

    "nomeCompleto": "Maria Santos",

    "disciplinaId": 1,

    "semestre": 3

  }' | jq .

 

# Salve o ID do aluno

# ALUNO_ID=1

 

# 5.2. Listar Meus Alunos

echo -e "\n[5.2] Listar Meus Alunos"

curl -X GET "${BASE_URL}/professor/alunos" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" | jq .

 

# 5.3. Listar Alunos por Disciplina

echo -e "\n[5.3] Listar Alunos por Disciplina"

curl -X GET "${BASE_URL}/professor/alunos/disciplina/${DISCIPLINA_ID}" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" | jq .

 

# 5.4. Buscar Aluno por ID

echo -e "\n[5.4] Buscar Aluno por ID"

curl -X GET "${BASE_URL}/professor/alunos/${ALUNO_ID}" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" | jq .

 

# 5.5. Atualizar Aluno

echo -e "\n[5.5] Atualizar Aluno"

curl -X PUT "${BASE_URL}/professor/alunos/${ALUNO_ID}" \

  -H "Content-Type: application/json" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" \

  -d '{

    "matricula": "20241234",

    "nomeCompleto": "Maria Santos Silva",

    "disciplinaId": 1,

    "semestre": 4

  }' | jq .

 

# 5.6. Inativar Aluno

echo -e "\n[5.6] Inativar Aluno"

curl -X PATCH "${BASE_URL}/professor/alunos/${ALUNO_ID}/inativar" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" | jq .

 

# ==============================================================================

# 6. MONITORIAS (PROFESSOR)

# ==============================================================================

 

echo -e "\n========================================="

echo "6. GESTÃO DE MONITORIAS (PROFESSOR)"

echo "========================================="

 

# 6.1. Criar Monitoria

echo -e "\n[6.1] Criar Monitoria"

curl -X POST "${BASE_URL}/professor/monitorias" \

  -H "Content-Type: application/json" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" \

  -d '{

    "disciplinaId": 1,

    "tipo": "PRESENCIAL",

    "local": "Sala 301",

    "dataInicio": "2024-03-01",

    "dataEncerramento": "2024-06-30",

    "horaInicio": "14:00:00",

    "horaEncerramento": "16:00:00",

    "curso": "Análise e Desenvolvimento de Sistemas",

    "semestre": 1

  }' | jq .

 

# Salve o ID da monitoria

# MONITORIA_ID=1

 

# 6.2. Listar Minhas Monitorias

echo -e "\n[6.2] Listar Minhas Monitorias"

curl -X GET "${BASE_URL}/professor/monitorias" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" | jq .

 

# 6.3. Listar Monitorias em Andamento

echo -e "\n[6.3] Listar Monitorias em Andamento"

curl -X GET "${BASE_URL}/professor/monitorias/em-andamento" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" | jq .

 

# 6.4. Buscar Monitoria por ID

echo -e "\n[6.4] Buscar Monitoria por ID"

curl -X GET "${BASE_URL}/professor/monitorias/${MONITORIA_ID}" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" | jq .

 

# 6.5. Atualizar Monitoria

echo -e "\n[6.5] Atualizar Monitoria"

curl -X PUT "${BASE_URL}/professor/monitorias/${MONITORIA_ID}" \

  -H "Content-Type: application/json" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" \

  -d '{

    "disciplinaId": 1,

    "tipo": "REMOTO",

    "local": "Google Meet",

    "dataInicio": "2024-03-01",

    "dataEncerramento": "2024-06-30",

    "horaInicio": "14:00:00",

    "horaEncerramento": "16:00:00",

    "curso": "Análise e Desenvolvimento de Sistemas",

    "semestre": 1

  }' | jq .

 

# 6.6. Associar Aluno à Monitoria

echo -e "\n[6.6] Associar Aluno à Monitoria"

curl -X POST "${BASE_URL}/professor/monitorias/associar-aluno" \

  -H "Content-Type: application/json" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" \

  -d '{

    "alunoId": 1,

    "monitoriaId": 1

  }' | jq .

 

# 6.7. Contar Alunos na Monitoria

echo -e "\n[6.7] Contar Alunos na Monitoria"

curl -X GET "${BASE_URL}/professor/monitorias/${MONITORIA_ID}/quantidade-alunos" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" | jq .

 

# 6.8. Remover Aluno da Monitoria

echo -e "\n[6.8] Remover Aluno da Monitoria"

curl -X DELETE "${BASE_URL}/professor/monitorias/monitor/${MONITOR_ID}" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" | jq .

 

# 6.9. Finalizar Monitoria

echo -e "\n[6.9] Finalizar Monitoria"

curl -X PATCH "${BASE_URL}/professor/monitorias/${MONITORIA_ID}/finalizar" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" | jq .

 

# ==============================================================================

# 7. FREQUÊNCIAS (PROFESSOR)

# ==============================================================================

 

echo -e "\n========================================="

echo "7. GESTÃO DE FREQUÊNCIAS (PROFESSOR)"

echo "========================================="

 

# 7.1. Registrar Frequência

echo -e "\n[7.1] Registrar Frequência"

curl -X POST "${BASE_URL}/professor/frequencias" \

  -H "Content-Type: application/json" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" \

  -d '{

    "alunoId": 1,

    "monitoriaId": 1,

    "data": "2024-03-15",

    "presente": true,

    "observacao": "Aluno participou ativamente"

  }' | jq .

 

# 7.2. Atualizar Frequência

echo -e "\n[7.2] Atualizar Frequência"

curl -X PATCH "${BASE_URL}/professor/frequencias/${FREQUENCIA_ID}?presente=false" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" | jq .

 

# ==============================================================================

# 8. ASSUNTOS (PROFESSOR)

# ==============================================================================

 

echo -e "\n========================================="

echo "8. GESTÃO DE ASSUNTOS (PROFESSOR)"

echo "========================================="

 

# 8.1. Registrar Assunto

echo -e "\n[8.1] Registrar Assunto"

curl -X POST "${BASE_URL}/professor/assuntos" \

  -H "Content-Type: application/json" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" \

  -d '{

    "monitoriaId": 1,

    "data": "2024-03-15",

    "assunto": "Listas Ligadas",

    "atividadePraticaAplicada": "Implementação de lista encadeada em Java"

  }' | jq .

 

# Salve o ID do assunto

# ASSUNTO_ID=1

 

# 8.2. Listar Assuntos por Monitoria

echo -e "\n[8.2] Listar Assuntos por Monitoria"

curl -X GET "${BASE_URL}/professor/assuntos/monitoria/${MONITORIA_ID}" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" | jq .

 

# 8.3. Excluir Assunto

echo -e "\n[8.3] Excluir Assunto"

curl -X DELETE "${BASE_URL}/professor/assuntos/${ASSUNTO_ID}" \

  -H "Authorization: Bearer ${PROFESSOR_TOKEN}" | jq .

 

# ==============================================================================

# EXEMPLOS DE REQUESTS COM VALORES VÁLIDOS PARA ENUMS

# ==============================================================================

 

echo -e "\n========================================="

echo "VALORES VÁLIDOS PARA ENUMS"

echo "========================================="

 

echo -e "\nTipoEscola:"

echo "  - EDUCACAO_CULTURA_HUMANIDADES"

echo "  - CIENCIAS_SOCIAIS_APLICADAS"

echo "  - ENGENHARIAS_CIENCIAS_TECNOLOGICAS"

echo "  - CIENCIAS_NATURAIS_SAUDE"

 

echo -e "\nFormacaoProfessor:"

echo "  - GRADUACAO"

echo "  - ESPECIALIZACAO"

echo "  - MBA"

echo "  - MESTRADO"

echo "  - DOUTORADO"

echo "  - POS_DOUTORADO"

 

echo -e "\nTipoMonitoria:"

echo "  - PRESENCIAL"

echo "  - REMOTO"

 

# ==============================================================================

# OBSERVAÇÕES IMPORTANTES

# ==============================================================================

 

echo -e "\n========================================="

echo "OBSERVAÇÕES IMPORTANTES"

echo "========================================="

echo ""

echo "1. Antes de executar os testes, certifique-se de que:"

echo "   - O PostgreSQL está rodando"

echo "   - O banco 'monitoriaweb' foi criado"

echo "   - A aplicação Spring Boot está rodando em http://localhost:8080"

echo ""

echo "2. Você precisará criar um usuário ADMIN inicial no banco de dados:"

echo "   SQL:"

echo "   INSERT INTO usuario (username, email, password, role, ativo) "

echo "   VALUES ('admin', 'admin@ucsal.br', '\$2a\$10\$...' , 'ADMIN', true);"

echo ""

echo "3. Use o jq para formatar as respostas JSON (opcional):"

echo "   sudo apt-get install jq"

echo ""

echo "4. Substitua os valores das variáveis (ADMIN_TOKEN, IDS, etc.) "

echo "   pelos valores reais retornados pelas requisições."

echo ""

echo "5. Para ver apenas o body da resposta, adicione ' | jq .' no final"

echo "   Para ver headers, adicione a flag '-v' ou '-i'"

echo ""

