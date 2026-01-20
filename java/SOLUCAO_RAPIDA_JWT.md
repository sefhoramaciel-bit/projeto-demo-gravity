# ⚡ Solução Rápida - Erro JWT 360 bits

## 🚨 Problema

A aplicação ainda está usando a chave JWT antiga (45 caracteres) mesmo após a atualização.

## ✅ Solução Imediata

### Opção 1: Recompilar (Recomendado)

```cmd
cd C:\Projetos\Projeto-DPSP\projeto-demo-gravity\java

# 1. Pare a aplicação (Ctrl+C se estiver rodando)

# 2. Limpe e recompile
mvnw.cmd clean install -DskipTests

# 3. Reinicie
mvnw.cmd spring-boot:run
```

### Opção 2: Copiar Arquivo Manualmente (Temporário)

Se não puder recompilar agora:

```cmd
cd C:\Projetos\Projeto-DPSP\projeto-demo-gravity\java
copy src\main\resources\application.properties target\classes\application.properties
```

Depois **reinicie a aplicação** (pare e inicie novamente).

## 🔍 Verificação

Após reiniciar, a chave deve ter **100 caracteres**. Verifique:

```cmd
type target\classes\application.properties | findstr "app.jwt.secret"
```

Deve mostrar:
```
app.jwt.secret=9a4f2c8d3b7a1e6f4g5h8i0j2k4l6m8n0p2r4t6v8x0z2a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6
```

## ⚠️ Importante

- A aplicação **DEVE ser reiniciada** para carregar a nova chave
- Apenas atualizar o arquivo não é suficiente se a aplicação já está rodando
- O Spring Boot carrega o `application.properties` apenas na inicialização

## 🧪 Teste

Após reiniciar, teste o login:

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

Deve retornar um token JWT válido.


