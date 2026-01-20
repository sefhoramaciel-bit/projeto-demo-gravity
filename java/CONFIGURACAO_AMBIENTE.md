# Guia de Configuração do Ambiente Java (JAVA_HOME)

Você está recebendo o erro `The JAVA_HOME environment variable is not defined correctly` porque o caminho que você definiu (`C:\Caminho\Para\JDK-21`) não existe. Você precisa colocar o caminho **real** onde o Java está instalado no seu computador.

Detectamos que o Java 21 provavelmente está instalado nesta pasta:
👉 **`C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot`**

---

## Opção 1: Configuração Rápida (Apenas para o terminal atual)

Se você quer apenas rodar agora, copie e cole o comando abaixo no seu **CMD**:

```cmd
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot
```
*(Certifique-se de que não há espaços extras antes ou depois do sinal de igual).*

Depois, tente rodar o Maven novamente:
```cmd
mvnw clean install -DskipTests
```

---

## Opção 2: Configuração Definitiva (Recomendado)

Para não precisar digitar isso toda vez que abrir o CMD, configure no Windows:

1. Clique no menu **Iniciar** e digite: `Variáveis de ambiente`.
2. Selecione a opção **"Editar as variáveis de ambiente do sistema"**.
3. Clique no botão **"Variáveis de Ambiente..."** (no canto inferior direito).
4. Na seção de baixo (**Variáveis do sistema**), procure por `JAVA_HOME`.
   - **Se existir:** Selecione e clique em "Editar".
   - **Se NÃO existir:** Clique em "Novo".
5. Preencha os campos:
   - **Nome da variável:** `JAVA_HOME`
   - **Valor da variável:** `C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot`
   *(Ou clique em "Procurar Diretório..." e navegue até esta pasta)*.
6. Clique em **OK** em todas as janelas.
7. **IMPORTANTE:** Feche o seu CMD atual e abra um novo para que a alteração tenha efeito.

---

## Como verificar se funcionou?

Abra um **novo** CMD e digite:

```cmd
echo %JAVA_HOME%
```

Se aparecer o caminho correto, você pode rodar o projeto com sucesso!
