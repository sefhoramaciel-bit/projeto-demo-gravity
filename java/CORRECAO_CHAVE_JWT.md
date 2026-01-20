# Correção da Chave JWT - Erro HS512

## 🔍 Problema

Erro ao fazer login:
```
The signing key's size is 360 bits which is not secure enough for the HS512 algorithm.
The JWT JWA Specification (RFC 7518, Section 3.2) states that keys used with HS512 
MUST have a size >= 512 bits (the key size must be greater than or equal to the hash output size).
```

## ✅ Solução Aplicada

### 1. **Chave JWT Atualizada**

A chave JWT foi atualizada de **45 caracteres** (360 bits) para **100 caracteres** (800 bits), que é mais do que suficiente para o algoritmo HS512.

**Antes:**
```
app.jwt.secret=9a4f2c8d3b7a1e6f4g5h8i0j2k4l6m8n0p2r4t6v8x0z2
```

**Depois:**
```
app.jwt.secret=9a4f2c8d3b7a1e6f4g5h8i0j2k4l6m8n0p2r4t6v8x0z2a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6
```

### 2. **Validação Adicionada no Código**

Adicionada validação no `JwtTokenProvider` para garantir que a chave tenha pelo menos 64 caracteres:

```java
if (keyBytes.length < 64) {
    throw new IllegalArgumentException(
        "JWT secret key must be at least 64 characters (512 bits) for HS512 algorithm.");
}
```

## 🔐 Requisitos de Segurança

- **HS512** requer chave de **pelo menos 512 bits** (64 bytes/64 caracteres)
- A chave atual tem **100 caracteres** (800 bits), que é segura
- Para produção, considere usar uma chave ainda mais longa e aleatória

## 🚀 Próximos Passos

1. **Reinicie a aplicação** para carregar a nova chave
2. **Teste o login:**
   ```bash
   curl -X 'POST' \
     'http://localhost:8081/auth/login' \
     -H 'accept: application/json' \
     -H 'Content-Type: application/json' \
     -d '{
     "email": "admin@farmacia.com",
     "senha": "admin123"
   }'
   ```

## ⚠️ Importante para Produção

Para produção, você deve:

1. **Gerar uma chave aleatória segura:**
   ```bash
   # Linux/Mac
   openssl rand -base64 64
   
   # Ou use um gerador online seguro
   ```

2. **Armazenar a chave de forma segura:**
   - Use variáveis de ambiente
   - Use um gerenciador de secrets (AWS Secrets Manager, Azure Key Vault, etc.)
   - Nunca commite a chave no repositório

3. **Exemplo de uso com variável de ambiente:**
   ```properties
   app.jwt.secret=${JWT_SECRET:chave-padrao-desenvolvimento}
   ```

## 📝 Notas

- A chave antiga tinha apenas 45 caracteres (360 bits)
- A nova chave tem 100 caracteres (800 bits), que é mais que suficiente
- O algoritmo HS512 é seguro quando usado com chaves de tamanho adequado


