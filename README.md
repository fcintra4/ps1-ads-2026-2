# ps1-ads-2026-2 Repository of the Script Programming I (Back-End) course

## Autenticação e recuperação de senha

- `POST /users/login` recebe `usernameOrEmail` e `password`; em caso de sucesso, `data.token` contém o JWT.
- `POST /users/forgot-password` recebe `email`. A resposta é genérica para não revelar se o e-mail está cadastrado. Para contas existentes, envia um link de redefinição.
- `POST /users/reset-password` recebe `token` e `newPassword` (8 a 72 caracteres). O token expira em 15 minutos e só pode ser usado uma vez.

Configure o envio de e-mail pelas variáveis de ambiente `SMTP_HOST`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD` e `SMTP_FROM`. `SMTP_AUTH` e `SMTP_STARTTLS` são opcionais (padrão `true`). Configure `PASSWORD_RESET_URL` com a URL da tela de redefinição do cliente; essa tela deve enviar o token e a nova senha para `POST /users/reset-password`.

## Rate limiting

A API limita cada endereço IP a 100 requisições a cada 60 segundos, usando uma janela deslizante mantida em memória por instância. Ao exceder o limite, retorna HTTP `429 Too Many Requests`, o contrato padrão de erro e o cabeçalho `Retry-After`. O limite e a janela podem ser configurados pelas variáveis `RATE_LIMIT_MAX_REQUESTS` e `RATE_LIMIT_WINDOW_SECONDS`.

## Validação de usuários, veículos e clientes

- Usuários: nome de 2 a 100 caracteres com letras e separadores de nome; username de 3 a 100 caracteres (`A-Z`, `a-z`, números, ponto, sublinhado e hífen); e-mail válido de até 254 caracteres; senha de 8 a 72 caracteres. Username e e-mail duplicados retornam `409 Conflict`; e-mails são armazenados em minúsculas.
- Veículos: placa no formato brasileiro antigo (`AAA9999`) ou Mercosul (`AAA1A11`), ano de fabricação entre 1886 e 2100, data de venda não futura e preço não negativo com até 10 dígitos inteiros e 2 decimais. Ano fora da faixa ou preço negativo retornam `409 Conflict`, conforme a regra desta API; placa duplicada também retorna `409`. Um `customerId` inexistente retorna `404 Not Found`.
- Clientes: documento deve ser CPF ou CNPJ válido, com ou sem pontuação; telefone deve ser brasileiro, com DDD e número fixo ou celular; UF deve corresponder a uma unidade federativa; data de nascimento não pode ser futura. Documentos são armazenados sem pontuação e em maiúsculas, UFs em maiúsculas e e-mails em minúsculas. Documento ou e-mail duplicado retorna `409 Conflict`.
- Demais valores inválidos retornam `400 Bad Request` no contrato padrão, com os campos e restrições que falharam.