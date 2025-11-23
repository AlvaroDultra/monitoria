-- ==============================================================================
-- SCRIPT SQL PARA CRIAR USUÁRIO ADMIN
-- ==============================================================================
-- 
-- Este script cria um usuário admin no banco de dados MonitoriaWeb
-- 
-- Credenciais padrão:
--   Username: admin
--   Password: admin123
--   Email: admin@ucsal.br
--   Role: ROLE_ADMIN
--
-- IMPORTANTE: O hash BCrypt abaixo foi gerado para a senha "admin123"
-- Se você quiser usar outra senha, gere um novo hash usando:
--   - https://www.bcrypt-generator.com/
--   - Ou o script criar-admin.sh
--
-- ==============================================================================

-- Verificar se o usuário já existe
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM usuarios WHERE username = 'admin') THEN
        -- Inserir usuário admin
        INSERT INTO usuarios (username, email, password, role, ativo)
        VALUES (
            'admin',
            'admin@ucsal.br',
            '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- Hash BCrypt para "admin123"
            'ROLE_ADMIN',
            true
        );
        
        RAISE NOTICE 'Usuário admin criado com sucesso!';
    ELSE
        RAISE NOTICE 'Usuário admin já existe no banco de dados.';
    END IF;
END $$;

-- Verificar se foi criado
SELECT id, username, email, role, ativo 
FROM usuarios 
WHERE username = 'admin';

