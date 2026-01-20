# ⚠️ IMPORTANTE: Reiniciar a Aplicação

## 🔍 Problema

A chave JWT foi atualizada no arquivo `application.properties`, mas a aplicação ainda está usando a versão antiga porque **não foi recompilada e reiniciada**.

## ✅ Solução

### 1. **Pare a Aplicação Atual**

Se a aplicação estiver rodando:
- Pressione `Ctrl+C` no terminal onde está rodando
- Ou encerre o processo Java

### 2. **Limpe e Recompile**

```cmd
cd C:\Projetos\Projeto-DPSP\projeto-demo-gravity\java
mvnw.cmd clean install -DskipTests
```

Isso irá:
- Limpar arquivos compilados antigos
- Recompilar com a nova chave JWT
- Copiar o novo `application.properties` para `target/classes/`

### 3. **Reinicie a Aplicação**

```cmd
mvnw.cmd spring-boot:run
```

### 4. **Teste o Login**

Após a aplicação iniciar completamente, teste:

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

## 🔍 Verificação

Para verificar se a chave foi atualizada corretamente:

1. **Verifique o arquivo fonte:**
   ```cmd
   type src\main\resources\application.properties | findstr "app.jwt.secret"
   ```
   Deve mostrar uma linha com **100 caracteres** após o `=`

2. **Verifique o arquivo compilado (após recompilar):**
   ```cmd
   type target\classes\application.properties | findstr "app.jwt.secret"
   ```
   Deve mostrar a mesma linha com **100 caracteres**

## ⚠️ Se o Erro Persistir

Se após recompilar e reiniciar o erro ainda ocorrer:

1. **Verifique se há múltiplas instâncias rodando:**
   ```cmd
   netstat -ano | findstr :8081
   ```
   Encerre todas as instâncias

2. **Limpe completamente:**
   ```cmd
   mvnw.cmd clean
   rmdir /s /q target
   mvnw.cmd install -DskipTests
   ```

3. **Verifique os logs da aplicação** para ver qual chave está sendo carregada

## 📝 Nota

A chave JWT antiga tinha **45 caracteres** (360 bits).
A nova chave tem **100 caracteres** (800 bits), que é mais que suficiente para HS512.


