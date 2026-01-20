-- Script SQL para criar/atualizar o usuário admin no banco de dados
-- Execute este script no pgAdmin ou psql se o usuário admin não existir ou tiver senha incorreta

-- IMPORTANTE: A senha deve ser hasheada com BCrypt
-- Use um gerador online de BCrypt ou execute o código Java para gerar o hash

-- Exemplo de hash BCrypt para "admin123" (pode variar):
-- $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

-- 1. Verificar se o usuário admin existe
SELECT id, nome, email, password, role FROM usuarios WHERE email = 'admin@farmacia.com';

-- 2. Se não existir, criar (substitua o UUID e o hash da senha):
-- INSERT INTO usuarios (id, nome, email, password, role, created_at)
-- VALUES (
--     gen_random_uuid(),
--     'Administrador',
--     'admin@farmacia.com',
--     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- Hash BCrypt de "admin123"
--     'ADMIN',
--     NOW()
-- );

-- 3. Se existir mas a senha estiver errada, atualizar:
-- UPDATE usuarios 
-- SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' -- Hash BCrypt de "admin123"
-- WHERE email = 'admin@farmacia.com';

-- NOTA: Para gerar o hash BCrypt correto, você pode:
-- 1. Executar a aplicação e deixar o DataSeeder criar
-- 2. Usar um gerador online de BCrypt
-- 3. Executar este código Java:
--    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
--    String hash = encoder.encode("admin123");
--    System.out.println(hash);


