# MonitoriaWeb - Sistema de Controle de Monitoria de Alunos

Sistema desenvolvido para gerenciar monitorias de alunos em disciplinas de cursos da UCSAL.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security** com JWT
- **PostgreSQL**
- **Lombok**
- **Maven**

## Arquitetura

O projeto segue uma arquitetura em camadas:

- **Controller**: Endpoints REST da API
- **Service**: Lógica de negócio
- **Repository**: Acesso a dados (JPA)
- **Entity**: Modelos de domínio
- **DTO**: Objetos de transferência de dados
- **Config**: Configurações de segurança e JWT
- **Exception**: Tratamento de exceções

## Funcionalidades

### Administrador

- Cadastrar, listar, atualizar e inativar escolas
- Cadastrar, listar, atualizar e inativar professores
- Cadastrar, listar, atualizar e inativar disciplinas
- Visualizar relatórios do sistema

### Professor

- Cadastrar alunos em suas disciplinas
- Criar e gerenciar monitorias
- Associar alunos às monitorias
- Registrar frequência diária dos alunos
- Registrar assuntos vistos em cada monitoria
- Iniciar e finalizar monitorias
- Visualizar listas e estatísticas

## Entidades Principais

1. **Escola**: Representa as escolas da UCSAL
2. **Professor**: Professores com suas formações
3. **Disciplina**: Disciplinas com matriz curricular
4. **Aluno**: Alunos cadastrados nas disciplinas
5. **Monitoria**: Sessões de monitoria (presencial/remoto)
6. **Monitor**: Associação entre aluno e monitoria
7. **FrequenciaAluno**: Registro de presença
8. **AssuntoMonitoria**: Assuntos/atividades de cada monitoria

## Configuração

### Banco de Dados

Configure o PostgreSQL no arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/monitoriaweb
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### JWT

A chave secreta JWT e o tempo de expiração podem ser configurados:

```properties
jwt.secret=sua-chave-secreta
jwt.expiration=86400000
```

## Como Executar

1. Certifique-se de ter o PostgreSQL instalado e rodando
2. Crie o banco de dados:
   ```sql
   CREATE DATABASE monitoriaweb;
   ```
3. Execute o projeto:
   ```bash
   mvn spring-boot:run
   ```

A aplicação estará disponível em: `http://localhost:8080/api`

## Endpoints da API

### Autenticação

- `POST /api/auth/login` - Login no sistema

### Admin - Escolas

- `POST /api/admin/escolas` - Criar escola
- `GET /api/admin/escolas` - Listar todas escolas
- `GET /api/admin/escolas/ativas` - Listar escolas ativas
- `GET /api/admin/escolas/{id}` - Buscar escola por ID
- `PUT /api/admin/escolas/{id}` - Atualizar escola
- `PATCH /api/admin/escolas/{id}/inativar` - Inativar escola
- `PATCH /api/admin/escolas/{id}/ativar` - Ativar escola

### Admin - Professores

- `POST /api/admin/professores` - Cadastrar professor
- `GET /api/admin/professores` - Listar todos professores
- `GET /api/admin/professores/ativos` - Listar professores ativos
- `GET /api/admin/professores/{id}` - Buscar professor por ID
- `PATCH /api/admin/professores/{id}/inativar` - Inativar professor
- `PATCH /api/admin/professores/{id}/ativar` - Ativar professor

### Admin - Disciplinas

- `POST /api/admin/disciplinas` - Criar disciplina
- `GET /api/admin/disciplinas` - Listar todas disciplinas
- `GET /api/admin/disciplinas/{id}` - Buscar disciplina por ID
- `PUT /api/admin/disciplinas/{id}` - Atualizar disciplina
- `PATCH /api/admin/disciplinas/{id}/inativar` - Inativar disciplina

### Professor - Alunos

- `POST /api/professor/alunos` - Cadastrar aluno
- `GET /api/professor/alunos` - Listar alunos do professor
- `GET /api/professor/alunos/disciplina/{disciplinaId}` - Listar alunos por disciplina
- `GET /api/professor/alunos/{id}` - Buscar aluno por ID
- `PUT /api/professor/alunos/{id}` - Atualizar aluno
- `PATCH /api/professor/alunos/{id}/inativar` - Inativar aluno

### Professor - Monitorias

- `POST /api/professor/monitorias` - Criar monitoria
- `GET /api/professor/monitorias` - Listar monitorias do professor
- `GET /api/professor/monitorias/em-andamento` - Listar monitorias em andamento
- `GET /api/professor/monitorias/{id}` - Buscar monitoria por ID
- `PUT /api/professor/monitorias/{id}` - Atualizar monitoria
- `PATCH /api/professor/monitorias/{id}/finalizar` - Finalizar monitoria
- `POST /api/professor/monitorias/associar-aluno` - Associar aluno à monitoria
- `DELETE /api/professor/monitorias/monitor/{monitorId}` - Remover aluno da monitoria
- `GET /api/professor/monitorias/{id}/quantidade-alunos` - Contar alunos na monitoria

### Professor - Frequências

- `POST /api/professor/frequencias` - Registrar frequência
- `PATCH /api/professor/frequencias/{frequenciaId}` - Atualizar frequência

### Professor - Assuntos

- `POST /api/professor/assuntos` - Registrar assunto de monitoria
- `GET /api/professor/assuntos/monitoria/{monitoriaId}` - Listar assuntos por monitoria
- `DELETE /api/professor/assuntos/{assuntoId}` - Excluir assunto

## Segurança

- Autenticação via JWT (JSON Web Token)
- Autorização baseada em roles (ADMIN, PROFESSOR)
- Senhas criptografadas com BCrypt
- CORS configurado para permitir acesso do frontend Angular
- Todas as requisições (exceto login) requerem autenticação

## Estrutura de Pastas

```
src/main/java/br/ucsal/monitoriaweb/
├── config/              # Configurações (Security, JWT)
├── controller/          # Controllers REST
├── dto/                 # DTOs de request e response
├── entity/              # Entidades JPA
├── exception/           # Exceções customizadas e handlers
├── repository/          # Repositories JPA
└── service/             # Services com lógica de negócio
```

## Autor

Sistema desenvolvido para a UCSAL - Universidade Católica do Salvador
