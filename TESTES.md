# Testes dos Endpoints - API MonitoriaWeb

## Scripts de Teste Disponíveis

### 1. `test-simple.sh`
Script simples para verificar se a aplicação está rodando e testar o login básico.

**Uso:**
```bash
chmod +x test-simple.sh
./test-simple.sh
```

### 2. `test-completo.sh`
Script completo que testa todos os endpoints de forma sequencial, capturando tokens e IDs automaticamente.

**Uso:**
```bash
chmod +x test-completo.sh
./test-completo.sh
```

### 3. `test-endpoints.sh`
Script original melhorado com captura automática de tokens e IDs.

**Uso:**
```bash
chmod +x test-endpoints.sh
./test-endpoints.sh
```

### 4. `test-curl.sh`
Script original com exemplos de todos os endpoints (requer configuração manual de tokens).

**Uso:**
```bash
chmod +x test-curl.sh
./test-curl.sh
```

## Problema Identificado

Atualmente, o endpoint `/api/auth/login` está retornando **HTTP 403 (Forbidden)** mesmo estando configurado como `permitAll()` no Spring Security.

### Possíveis Causas:

1. **Problema de CORS**: O CORS está configurado apenas para `http://localhost:4200`, mas as requisições podem estar sendo bloqueadas mesmo com o header Origin correto.

2. **Ordem dos Filtros**: O `JwtAuthenticationFilter` pode estar interferindo antes do Spring Security processar a configuração de `permitAll()`.

3. **Configuração de Segurança**: Pode haver algum problema na configuração do `SecurityFilterChain`.

### Soluções Sugeridas:

1. **Verificar se o filtro JWT está pulando requisições para `/api/auth/**`**:
   - O filtro JWT deveria ignorar requisições de autenticação

2. **Ajustar a configuração de CORS**:
   - Permitir requisições de qualquer origem em desenvolvimento
   - Ou garantir que as requisições incluam o header `Origin: http://localhost:4200`

3. **Verificar logs da aplicação**:
   - Verificar se há erros no log relacionados à autenticação
   - Verificar se o endpoint está sendo acessado corretamente

## Endpoints Disponíveis

### Autenticação
- `POST /api/auth/login` - Login de usuário

### Admin - Escolas
- `POST /api/admin/escolas` - Criar escola
- `GET /api/admin/escolas` - Listar todas as escolas
- `GET /api/admin/escolas/ativas` - Listar escolas ativas
- `GET /api/admin/escolas/{id}` - Buscar escola por ID
- `PUT /api/admin/escolas/{id}` - Atualizar escola
- `PATCH /api/admin/escolas/{id}/inativar` - Inativar escola
- `PATCH /api/admin/escolas/{id}/ativar` - Ativar escola

### Admin - Professores
- `POST /api/admin/professores` - Criar professor
- `GET /api/admin/professores` - Listar todos os professores
- `GET /api/admin/professores/ativos` - Listar professores ativos
- `GET /api/admin/professores/{id}` - Buscar professor por ID
- `PATCH /api/admin/professores/{id}/inativar` - Inativar professor
- `PATCH /api/admin/professores/{id}/ativar` - Ativar professor

### Admin - Disciplinas
- `POST /api/admin/disciplinas` - Criar disciplina
- `GET /api/admin/disciplinas` - Listar todas as disciplinas
- `GET /api/admin/disciplinas/{id}` - Buscar disciplina por ID
- `PUT /api/admin/disciplinas/{id}` - Atualizar disciplina
- `PATCH /api/admin/disciplinas/{id}/inativar` - Inativar disciplina

### Professor - Alunos
- `POST /api/professor/alunos` - Criar aluno
- `GET /api/professor/alunos` - Listar meus alunos
- `GET /api/professor/alunos/disciplina/{id}` - Listar alunos por disciplina
- `GET /api/professor/alunos/{id}` - Buscar aluno por ID
- `PUT /api/professor/alunos/{id}` - Atualizar aluno
- `PATCH /api/professor/alunos/{id}/inativar` - Inativar aluno

### Professor - Monitorias
- `POST /api/professor/monitorias` - Criar monitoria
- `GET /api/professor/monitorias` - Listar minhas monitorias
- `GET /api/professor/monitorias/em-andamento` - Listar monitorias em andamento
- `GET /api/professor/monitorias/{id}` - Buscar monitoria por ID
- `PUT /api/professor/monitorias/{id}` - Atualizar monitoria
- `PATCH /api/professor/monitorias/{id}/finalizar` - Finalizar monitoria
- `POST /api/professor/monitorias/associar-aluno` - Associar aluno à monitoria
- `DELETE /api/professor/monitorias/monitor/{id}` - Remover aluno da monitoria
- `GET /api/professor/monitorias/{id}/quantidade-alunos` - Contar alunos na monitoria

### Professor - Frequências
- `POST /api/professor/frequencias` - Registrar frequência
- `PATCH /api/professor/frequencias/{id}?presente={boolean}` - Atualizar frequência

### Professor - Assuntos
- `POST /api/professor/assuntos` - Registrar assunto
- `GET /api/professor/assuntos/monitoria/{id}` - Listar assuntos por monitoria
- `DELETE /api/professor/assuntos/{id}` - Excluir assunto

## Valores Válidos para Enums

### TipoEscola
- `EDUCACAO_CULTURA_HUMANIDADES`
- `CIENCIAS_SOCIAIS_APLICADAS`
- `ENGENHARIAS_CIENCIAS_TECNOLOGICAS`
- `CIENCIAS_NATURAIS_SAUDE`

### FormacaoProfessor
- `GRADUACAO`
- `ESPECIALIZACAO`
- `MBA`
- `MESTRADO`
- `DOUTORADO`
- `POS_DOUTORADO`

### TipoMonitoria
- `PRESENCIAL`
- `REMOTO`

## Pré-requisitos

1. **Aplicação Spring Boot rodando** em `http://localhost:8080`
2. **Banco de dados PostgreSQL** configurado e rodando
3. **Usuário admin criado** no banco de dados (necessário para os testes)
4. **Ferramentas**:
   - `curl` (geralmente já instalado)
   - `jq` (opcional, para formatação JSON): `sudo apt-get install jq`
   - `python3` (opcional, para formatação JSON)

## Exemplo de Criação de Usuário Admin

Para criar um usuário admin no banco de dados, você pode usar o seguinte SQL (ajuste a senha hash conforme necessário):

```sql
INSERT INTO usuario (username, email, password, role, ativo) 
VALUES ('admin', 'admin@ucsal.br', '$2a$10$...', 'ADMIN', true);
```

Para gerar o hash da senha, você pode usar um utilitário BCrypt ou criar um endpoint temporário na aplicação.

## Notas

- Todos os scripts incluem o header `Origin: http://localhost:4200` para compatibilidade com CORS
- Os tokens JWT são capturados automaticamente e reutilizados nos testes subsequentes
- Os IDs criados (escola, professor, disciplina, etc.) são capturados e reutilizados nos testes
- Os scripts geram logs detalhados dos testes executados

