# Ajustes para Banco de Dados Existente

## ⚠️ Importante

A aplicação foi configurada para **NÃO modificar** o schema do banco de dados existente. As seguintes alterações foram feitas:

## 🔧 Alterações Realizadas

### 1. **application.properties**
- Alterado `spring.jpa.hibernate.ddl-auto` de `update` para `validate`
- Isso garante que o Hibernate apenas **valida** se as entidades correspondem ao schema existente
- **Nenhuma tabela ou coluna será criada ou modificada automaticamente**

### 2. **Estrutura Esperada das Tabelas**

A aplicação espera as seguintes tabelas no banco `farmacia_db`:

#### **usuarios**
- `id` (UUID ou tipo compatível)
- `nome` (VARCHAR, NOT NULL)
- `email` (VARCHAR, NOT NULL, UNIQUE)
- `senha` (VARCHAR, NOT NULL)
- `role` (VARCHAR, NOT NULL) - valores: 'ADMIN' ou 'VENDEDOR'
- `avatar_url` (VARCHAR, nullable)
- `created_at` (TIMESTAMP, nullable)

#### **categorias**
- `id` (BIGSERIAL ou INTEGER, PRIMARY KEY)
- `nome` (VARCHAR, NOT NULL, UNIQUE)

#### **medicamentos**
- `id` (BIGSERIAL ou INTEGER, PRIMARY KEY)
- `nome` (VARCHAR, NOT NULL, UNIQUE)
- `preco` (NUMERIC/DECIMAL, NOT NULL)
- `estoque` (INTEGER, NOT NULL)
- `validade` (DATE, NOT NULL)
- `ativo` (BOOLEAN, NOT NULL, default: true)
- `categoria_id` (BIGINT, NOT NULL, FOREIGN KEY -> categorias.id)

#### **clientes**
- `id` (BIGSERIAL ou INTEGER, PRIMARY KEY)
- `cpf` (VARCHAR, NOT NULL, UNIQUE)
- `nome` (VARCHAR, NOT NULL)
- `email` (VARCHAR, NOT NULL)
- `data_nascimento` (DATE, NOT NULL)

#### **vendas**
- `id` (BIGSERIAL ou INTEGER, PRIMARY KEY)
- `cliente_id` (BIGINT, FOREIGN KEY -> clientes.id)
- `vendedor_id` (UUID ou tipo compatível, FOREIGN KEY -> usuarios.id, nullable)
- `total` (NUMERIC/DECIMAL, NOT NULL)
- `data_venda` (TIMESTAMP, NOT NULL)

#### **itens_venda**
- `id` (BIGSERIAL ou INTEGER, PRIMARY KEY)
- `venda_id` (BIGINT, NOT NULL, FOREIGN KEY -> vendas.id)
- `medicamento_id` (BIGINT, NOT NULL, FOREIGN KEY -> medicamentos.id)
- `quantidade` (INTEGER, NOT NULL)
- `preco_unitario` (NUMERIC/DECIMAL, NOT NULL)

#### **logs_auditoria**
- `id` (BIGSERIAL ou INTEGER, PRIMARY KEY)
- `usuario` (VARCHAR, nullable)
- `data_hora` (TIMESTAMP, NOT NULL)
- `tipo` (VARCHAR, NOT NULL)
- `detalhes` (TEXT, nullable)

## 🔍 Como Verificar a Estrutura do Seu Banco

1. **Execute o script SQL** `consultar_estrutura_banco.sql` no pgAdmin ou psql
2. **Compare** os resultados com a estrutura esperada acima
3. **Ajuste as entidades Java** se necessário para corresponder ao seu banco

## 📝 Possíveis Ajustes Necessários

### Se os nomes das colunas forem diferentes:

Exemplo: Se no banco a coluna é `data_nascimento` mas a entidade usa `dataNascimento`:

```java
@Column(name = "data_nascimento")  // Adicione esta anotação
private LocalDate dataNascimento;
```

### Se os tipos de dados forem diferentes:

- **UUID vs VARCHAR**: Se `usuarios.id` for VARCHAR ao invés de UUID, você pode precisar ajustar
- **TIMESTAMP vs DATE**: Verifique se os tipos de data correspondem
- **NUMERIC vs DECIMAL**: Ambos são compatíveis, mas verifique a precisão

### Se houver colunas adicionais no banco:

As colunas extras no banco não causarão problemas, mas se você quiser mapeá-las, adicione campos nas entidades.

## 🚀 Próximos Passos

1. Execute o script `consultar_estrutura_banco.sql` para ver a estrutura real
2. Compare com as entidades Java
3. Ajuste as anotações `@Column(name = "...")` se os nomes forem diferentes
4. Teste a aplicação - o Hibernate validará se tudo está correto

## ⚠️ Erros Comuns

### Erro: "Schema-validation: missing table"
**Causa**: A tabela não existe no banco.

**Solução**: Crie a tabela manualmente ou ajuste o nome da tabela na anotação `@Table`.

### Erro: "Schema-validation: missing column"
**Causa**: A coluna não existe na tabela do banco.

**Solução**: 
- Adicione a coluna no banco, OU
- Remova o campo da entidade se não for necessário, OU
- Use `@Column(name = "nome_real_no_banco")` se o nome for diferente

### Erro: "Wrong column type"
**Causa**: O tipo de dado no banco não corresponde ao tipo Java.

**Solução**: Ajuste o tipo no banco ou use `@Column(columnDefinition = "...")` para especificar o tipo exato.

## 📚 Referências

- [Hibernate Schema Validation](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#configurations-hbmddl)
- [Spring Data JPA - Schema Management](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization)


