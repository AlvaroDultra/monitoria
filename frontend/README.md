# Sistema de Monitoria Web - Frontend

Frontend em React para o Sistema de Gestão de Monitorias desenvolvido com Spring Boot.

## Tecnologias Utilizadas

- **React 18** - Biblioteca para construção de interfaces
- **Vite** - Build tool e dev server
- **React Router DOM** - Roteamento
- **Axios** - Cliente HTTP
- **TailwindCSS** - Framework CSS
- **Lucide React** - Ícones
- **React Hook Form** - Gerenciamento de formulários

## Estrutura do Projeto

```
src/
├── components/
│   ├── common/          # Componentes reutilizáveis (Button, Input, Table, etc.)
│   ├── layout/          # Componentes de layout (Header, Sidebar, Layout)
│   ├── admin/           # Componentes específicos do admin
│   └── professor/       # Componentes específicos do professor
├── pages/
│   ├── auth/            # Páginas de autenticação
│   ├── admin/           # Páginas do administrador
│   └── professor/       # Páginas do professor
├── services/            # Serviços de API
├── context/             # Contextos React (AuthContext)
├── constants/           # Constantes e enums
└── utils/               # Funções utilitárias
```

## Funcionalidades

### Administrador (ROLE_ADMIN)
- ✅ Dashboard com estatísticas
- ✅ Gerenciar Escolas (criar, editar, ativar/inativar)
- ✅ Gerenciar Professores (criar, ativar/inativar)
- ✅ Gerenciar Disciplinas (criar, editar, inativar)

### Professor (ROLE_PROFESSOR)
- ✅ Dashboard com estatísticas pessoais
- ✅ Gerenciar Monitorias (criar, editar, finalizar)
- ✅ Gerenciar Alunos (criar, editar, inativar)
- 🔄 Registrar Frequências (em desenvolvimento)
- 🔄 Gerenciar Assuntos (em desenvolvimento)

## Pré-requisitos

- Node.js 18+ e npm
- Backend Spring Boot rodando em `http://localhost:8080`

## Como Executar

1. **Instalar dependências:**
```bash
npm install
```

2. **Executar em modo de desenvolvimento:**
```bash
npm run dev
```

O frontend estará disponível em `http://localhost:5173`

3. **Build para produção:**
```bash
npm run build
```

4. **Preview da build de produção:**
```bash
npm run preview
```

## Configuração da API

A URL da API está configurada em `src/services/api.js`:

```javascript
const API_URL = 'http://localhost:8080/api';
```

Para alterar a URL da API, edite este arquivo.

## Autenticação

O sistema utiliza JWT (JSON Web Tokens) para autenticação:

1. O usuário faz login em `/login`
2. O backend retorna um token JWT
3. O token é armazenado no localStorage
4. Todas as requisições subsequentes incluem o token no header `Authorization: Bearer {token}`

### Usuários de Teste

Certifique-se de que o backend tem usuários cadastrados para teste. Exemplo:
- **Admin:** username: `admin`, password: `admin123`
- **Professor:** username: `professor01`, password: `professor123`

## Rotas Disponíveis

### Públicas
- `/login` - Página de login

### Admin (requer ROLE_ADMIN)
- `/admin` - Dashboard do administrador
- `/admin/escolas` - Gerenciar escolas
- `/admin/professores` - Gerenciar professores
- `/admin/disciplinas` - Gerenciar disciplinas

### Professor (requer ROLE_PROFESSOR)
- `/professor` - Dashboard do professor
- `/professor/monitorias` - Gerenciar monitorias
- `/professor/alunos` - Gerenciar alunos

## Componentes Principais

### Layout
- **Header:** Exibe informações do usuário e botão de logout
- **Sidebar:** Menu de navegação baseado no role do usuário
- **Layout:** Container principal que combina Header e Sidebar

### Componentes Reutilizáveis
- **Button:** Botão customizável com variantes (primary, secondary, danger, success, outline)
- **Input:** Campo de entrada com label e validação
- **Select:** Campo de seleção com options
- **Table:** Tabela responsiva com colunas customizáveis
- **Modal:** Modal/Dialog para formulários
- **Card:** Container de conteúdo estilizado
- **Loading:** Indicador de carregamento

## Estilização

O projeto utiliza TailwindCSS com uma paleta de cores customizada definida em `tailwind.config.js`:

```javascript
primary: {
  50: '#eff6ff',
  100: '#dbeafe',
  200: '#bfdbfe',
  // ... até 900
}
```

## Tratamento de Erros

- **401 Unauthorized:** Redireciona automaticamente para `/login`
- **Outros erros:** São logados no console (pode ser melhorado com toast notifications)

## Próximos Passos

- [ ] Implementar páginas de Frequência
- [ ] Implementar páginas de Assuntos
- [ ] Adicionar toast notifications para feedback
- [ ] Implementar paginação nas tabelas
- [ ] Adicionar filtros e busca
- [ ] Melhorar validação de formulários
- [ ] Adicionar testes unitários
- [ ] Implementar dark mode

## Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## Licença

Este projeto é parte do Sistema de Monitoria Web - UCSAL.
