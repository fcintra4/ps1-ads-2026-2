-- =========================================================
-- ÍNDICES PARA A TABELA CUSTOMERS
-- =========================================================

-- Otimiza a busca direta por documento (CPF/CNPJ) e garante unicidade
CREATE UNIQUE INDEX IF NOT EXISTS idx_customers_ident_document 
ON customers(ident_document);

-- Otimiza a busca direta por e-mail e garante unicidade
CREATE UNIQUE INDEX IF NOT EXISTS idx_customers_email 
ON customers(email);

-- Otimiza ordenações e buscas por nome do cliente
CREATE INDEX IF NOT EXISTS idx_customers_name 
ON customers(name);

-- Otimiza relatórios e buscas filtradas por estado e cidade
CREATE INDEX IF NOT EXISTS idx_customers_state_municipality 
ON customers(state, municipality);


-- =========================================================
-- ÍNDICES PARA A TABELA CARS
-- =========================================================

-- CRÍTICO: Otimiza junções (JOINs) e integridade referencial com a tabela de clientes
CREATE INDEX IF NOT EXISTS idx_cars_customer_id 
ON cars(customer_id);

-- Otimiza a busca combinada por marca e modelo do veículo
CREATE INDEX IF NOT EXISTS idx_cars_brand_model 
ON cars(brand, model);

-- Otimiza relatórios de vendas e buscas por intervalo de datas
CREATE INDEX IF NOT EXISTS idx_cars_selling_date 
ON cars(selling_date);