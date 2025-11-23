# Como Criar o Usuário Admin no Banco de Dados

## Métodos Disponíveis

Existem 3 formas de criar o usuário admin no banco de dados:

---

## Método 1: Usar o Script SQL (Mais Simples) ⭐ RECOMENDADO

### Passo 1: Conectar ao PostgreSQL
```bash
psql -U postgres -d monitoriaweb
```

### Passo 2: Executar o Script SQL
```sql
\i criar-admin.sql
```

Ou copie e cole o conteúdo do arquivo `criar-admin.sql` diretamente no psql.

### Passo 3: Verificar se foi criado
```sql
SELECT id, username, email, role, ativo FROM usuarios WHERE username = 'admin';
```

---

## Método 2: Executar SQL Diretamente

### Opção A: Via psql
```bash
psql -U postgres -d monitoriaweb -c "INSERT INTO usuarios (username, email, password, role, ativo) VALUES ('admin', 'admin@ucsal.br', '\$2a\$10\$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_ADMIN', true);"
```

### Opção B: Via psql interativo
```bash
psql -U postgres -d monitoriaweb
```

Depois execute:
```sql
INSERT INTO usuarios (username, email, password, role, ativo)
VALUES (
    'admin',
    'admin@ucsal.br',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'ROLE_ADMIN',
    true
);
```

---

## Método 3: Usar o Script Shell (Gera Hash Automaticamente)

```bash
./criar-admin.sh
```

O script tentará gerar o hash BCrypt automaticamente e mostrará o SQL necessário.

---

## Credenciais do Usuário Admin

Após criar o usuário, você pode fazer login com:

- **Username:** `admin`
- **Password:** `admin123`
- **Email:** `admin@ucsal.br`
- **Role:** `ROLE_ADMIN`

---

## Gerar Hash BCrypt para Outra Senha

Se você quiser usar uma senha diferente de "admin123", você pode gerar um novo hash BCrypt:

### Opção 1: Gerador Online
1. Acesse: https://www.bcrypt-generator.com/
2. Digite sua senha
3. Clique em "Generate Hash"
4. Copie o hash gerado
5. Use no SQL acima substituindo o hash existente

### Opção 2: Usar Spring Boot
Crie um endpoint temporário ou use o console do Spring Boot:

```java
@Autowired
private PasswordEncoder passwordEncoder;

// Em algum método ou endpoint
String hash = passwordEncoder.encode("sua-senha-aqui");
System.out.println(hash);
```

---

## Verificar se o Usuário Foi Criado

```sql
-- Ver todos os usuários
SELECT id, username, email, role, ativo FROM usuarios;

-- Ver apenas o admin
SELECT id, username, email, role, ativo FROM usuarios WHERE username = 'admin';
```

---

## Estrutura da Tabela usuarios

A tabela `usuarios` possui os seguintes campos:

- `id` (BIGSERIAL) - ID auto-incrementado
- `username` (VARCHAR) - Nome de usuário (único)
- `email` (VARCHAR) - Email (único)
- `password` (VARCHAR) - Hash BCrypt da senha
- `role` (VARCHAR) - Role do usuário (ROLE_ADMIN ou ROLE_PROFESSOR)
- `ativo` (BOOLEAN) - Se o usuário está ativo

---

## Troubleshooting

### Erro: "relation usuarios does not exist"
- A tabela ainda não foi criada. Inicie a aplicação Spring Boot uma vez para que o Hibernate crie as tabelas automaticamente (ddl-auto=update).

### Erro: "duplicate key value violates unique constraint"
- O usuário admin já existe. Você pode:
  - Verificar: `SELECT * FROM usuarios WHERE username = 'admin';`
  - Deletar e recriar: `DELETE FROM usuarios WHERE username = 'admin';`
  - Ou simplesmente usar o usuário existente

### Erro: "password authentication failed"
- Verifique as credenciais do PostgreSQL no arquivo `application.properties`:
  - `spring.datasource.username=postgres`
  - `spring.datasource.password=170902`

### Hash não funciona
- Certifique-se de que o hash está completo (60 caracteres começando com `$2a$10$`)
- Não adicione espaços extras no hash
- Use aspas simples no SQL: `'$2a$10$...'`

---

## Testar o Login

Após criar o usuário, teste o login:

```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -H "Origin: http://localhost:4200" \
  -d '{"username":"admin","password":"admin123"}'
```

Se funcionar, você receberá um token JWT na resposta.

---

## Próximos Passos

Após criar o usuário admin:

1. ✅ Teste o login
2. ✅ Execute os scripts de teste: `./test-completo.sh`
3. ✅ Crie escolas, professores, disciplinas, etc. através da API

