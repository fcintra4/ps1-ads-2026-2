# ps1-ads-2026-2
Repositório da disciplina de Programação de Scripts I (Back-End) - Fatec Franca 2026/2

## Execução da API

Requisito: Java 21 ou superior e um banco PostgreSQL disponível.
Configure as variáveis de ambiente `DB_URL` (URL JDBC do PostgreSQL),
`DB_USERNAME`, `DB_PASSWORD` e `JWT_SECRET` (chave de pelo menos 32 bytes).
As migrações Flyway criam as tabelas no banco configurado.

Na raiz do repositório:

```bash
cd api
./mvnw spring-boot:run
```

A API atende em `http://localhost:8888`. Para autenticar, envie
`POST /users/login` com `usernameOrEmail` e `password` de um usuário existente
no banco. Use o `token` retornado em **Auth → Bearer Token** nas requisições
protegidas.

As validações de ano de fabricação, dados de venda e placa duplicada retornam
`409`, com `message` e `type` no JSON. O cadastro válido de um carro retorna `200`.
Os prints da Prova 2 estão na pasta `PRINTS_PROVA2`, fora de `api`.

## Testes

Testes unitários, sem conexão com banco:

```bash
cd api
./mvnw test -Dtest='!ApiApplicationTests'
```

Para executar também o teste de inicialização da aplicação, configure as
variáveis de ambiente de um PostgreSQL de testes e execute `./mvnw test`.
