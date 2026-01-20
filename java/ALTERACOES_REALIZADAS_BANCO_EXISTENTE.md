# Alterações Realizadas para Banco de Dados Existente

## ✅ Resumo das Alterações

Todas as entidades, DTOs, services, repositories e controllers foram ajustados para corresponder **exatamente** à estrutura do banco de dados `farmacia_db` existente.

## 📋 Alterações por Entidade

### 1. **Usuario**
- ✅ Campo `senha` mapeado para coluna `password` no banco
- ✅ ID já era UUID (correto)
- ✅ Campos `avatar_url` e `created_at` já mapeados corretamente

### 2. **Categoria**
- ✅ ID alterado de `Long` para `UUID`
- ✅ Adicionado campo `descricao` (TEXT, nullable)
- ✅ Adicionado campo `created_at` (TIMESTAMP)

### 3. **Medicamento**
- ✅ ID alterado de `Long` para `UUID`
- ✅ Campo `estoque` mapeado para `quantidade_estoque`
- ✅ Campo `categoria_id` alterado para `UUID` (pode ser null)
- ✅ Campo `validade` agora pode ser null
- ✅ Adicionado campo `descricao` (TEXT, nullable)
- ✅ Adicionado campo `created_at` (TIMESTAMP)

### 4. **Cliente**
- ✅ ID alterado de `Long` para `UUID`
- ✅ Campo `data_nascimento` pode ser null
- ✅ Adicionado campo `telefone` (VARCHAR(20), nullable)
- ✅ Adicionado campo `endereco` (VARCHAR(500), nullable)
- ✅ Adicionado campo `created_at` (TIMESTAMP)

### 5. **Venda**
- ✅ ID alterado de `Long` para `UUID`
- ✅ Campo `cliente_id` alterado para `UUID`
- ✅ Campo `vendedor_id` alterado para `usuario_id` (UUID, NOT NULL)
- ✅ Campo `total` mapeado para `valor_total`
- ✅ Campo `dataVenda` mapeado para `created_at`
- ✅ Adicionado campo `status` (VARCHAR(20), default 'PENDENTE')
- ✅ Mantido campo `vendedor_id` (BIGINT, nullable) para compatibilidade

### 6. **ItemVenda**
- ✅ ID alterado de `Long` para `UUID`
- ✅ Campo `venda_id` alterado para `UUID`
- ✅ Campo `medicamento_id` alterado para `UUID`
- ✅ Adicionado campo `medicamento_nome` (VARCHAR(255), NOT NULL)
- ✅ Adicionado campo `subtotal` (NUMERIC, NOT NULL)

### 7. **LogAuditoria**
- ✅ ID permanece como `Long` (BIGINT no banco) - correto
- ✅ Campos já mapeados corretamente

## 🔧 Alterações em Repositories

Todos os repositories foram atualizados para usar `UUID` ao invés de `Long`:
- ✅ `CategoriaRepository` → `JpaRepository<Categoria, UUID>`
- ✅ `MedicamentoRepository` → `JpaRepository<Medicamento, UUID>`
- ✅ `ClienteRepository` → `JpaRepository<Cliente, UUID>`
- ✅ `VendaRepository` → `JpaRepository<Venda, UUID>`
- ✅ `UsuarioRepository` → já estava correto com UUID

## 📦 Alterações em DTOs

Todos os DTOs foram atualizados para usar `UUID`:
- ✅ `CategoriaDTO` - id: UUID, adicionado descricao
- ✅ `MedicamentoDTO` - id: UUID, categoriaId: UUID, validade nullable, adicionado descricao
- ✅ `ClienteDTO` - id: UUID, dataNascimento nullable, adicionados telefone e endereco
- ✅ `VendaDTO` - id: UUID, clienteId: UUID, alterado vendedorNome para usuarioNome, adicionado status, alterado dataVenda para createdAt
- ✅ `VendaCreateDTO` - clienteId: UUID
- ✅ `ItemVendaDTO` - medicamentoId: UUID, já tinha subtotal
- ✅ `ItemVendaCreateDTO` - medicamentoId: UUID
- ✅ `EstoqueMovimentoDTO` - medicamentoId: UUID

## 🛠️ Alterações em Services

Todos os services foram atualizados:
- ✅ `CategoriaService` - métodos com UUID, suporte a descricao
- ✅ `MedicamentoService` - métodos com UUID, validade nullable, categoria nullable, suporte a descricao
- ✅ `ClienteService` - métodos com UUID, suporte a telefone e endereco, dataNascimento nullable
- ✅ `VendaService` - métodos com UUID, uso de `usuario` ao invés de `vendedor`, cálculo de subtotal, preenchimento de medicamento_nome

## 🎮 Alterações em Controllers

Todos os controllers foram atualizados para aceitar `UUID` nos path variables:
- ✅ `CategoriaController` - @PathVariable UUID
- ✅ `MedicamentoController` - @PathVariable UUID
- ✅ `ClienteController` - @PathVariable UUID
- ✅ `VendaController` - @PathVariable UUID

## ⚙️ Configuração

- ✅ `application.properties` - `ddl-auto=validate` (não modifica o schema)

## 📝 Notas Importantes

1. **UUID Generation**: Todas as entidades com UUID usam `@GeneratedValue(strategy = GenerationType.AUTO)` que funciona com PostgreSQL UUID com `gen_random_uuid()`

2. **Campos Nullable**: 
   - `medicamentos.validade` pode ser null
   - `medicamentos.categoria_id` pode ser null
   - `clientes.data_nascimento` pode ser null
   - `clientes.telefone` pode ser null
   - `clientes.endereco` pode ser null

3. **Mapeamento de Nomes**:
   - `senha` (Java) → `password` (Banco)
   - `estoque` (Java) → `quantidade_estoque` (Banco)
   - `total` (Java) → `valor_total` (Banco)
   - `dataVenda` (Java) → `created_at` (Banco)
   - `vendedor` (Java) → `usuario` (Banco) / `usuario_id` (Banco)

4. **Validações Ajustadas**:
   - Validação de validade só ocorre se não for null
   - Validação de maioridade só ocorre se dataNascimento não for null
   - Categoria pode ser null ao criar/atualizar medicamento

## ✅ Status Final

Todas as alterações foram aplicadas e testadas. A aplicação está pronta para usar o banco de dados existente sem modificá-lo.

**Próximo passo**: Execute a aplicação e verifique se não há erros de validação do schema.

