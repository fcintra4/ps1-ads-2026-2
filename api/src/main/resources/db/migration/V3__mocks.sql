-- 10 customers
INSERT INTO customers (name, ident_document, birth_date, street_name, house_number, complements, district, municipality, state, phone, email) VALUES
('João Silva', '123.456.789-00', '1985-03-15', 'Rua das Flores', '120', 'Apto 101', 'Centro', 'São Paulo', 'SP', '(11) 98765-4321', 'joao.silva@email.com'),
('Maria Santos', '987.654.321-00', '1990-07-22', 'Av. Brasil', '450', NULL, 'Jardim América', 'Rio de Janeiro', 'RJ', '(21) 99876-5432', 'maria.santos@email.com'),
('Pedro Oliveira', '456.789.123-00', '1978-11-08', 'Rua das Acácias', '89', 'Bloco B', 'Boa Viagem', 'Recife', 'PE', '(81) 97654-3210', 'pedro.oliveira@email.com'),
('Ana Costa', '321.654.987-00', '1995-05-30', 'Rua do Comércio', '230', NULL, 'Centro', 'Belo Horizonte', 'MG', '(31) 96543-2109', 'ana.costa@email.com'),
('Carlos Souza', '789.123.456-00', '1982-09-14', 'Av. Paulista', '1000', 'Conj 502', 'Bela Vista', 'São Paulo', 'SP', '(11) 95432-1098', 'carlos.souza@email.com'),
('Fernanda Lima', '159.753.486-00', '1988-01-25', 'Rua das Palmeiras', '67', NULL, 'Centro', 'Curitiba', 'PR', '(41) 94321-0987', 'fernanda.lima@email.com'),
('Rafael Almeida', '753.951.852-00', '1975-12-03', 'Av. Central', '340', 'Casa 2', 'Setor Oeste', 'Goiânia', 'GO', '(62) 93210-9876', 'rafael.almeida@email.com'),
('Juliana Pereira', '951.753.258-00', '1992-04-18', 'Rua do Sol', '156', NULL, 'Praia Grande', 'Santos', 'SP', '(13) 92109-8765', 'juliana.pereira@email.com'),
('Marcos Rodrigues', '357.159.951-00', '1986-08-27', 'Rua das Figueiras', '78', 'Fundos', 'Centro', 'Porto Alegre', 'RS', '(51) 91098-7654', 'marcos.rodrigues@email.com'),
('Camila Barbosa', '852.654.321-00', '1998-02-10', 'Av. das Nações', '567', NULL, 'Centro', 'Florianópolis', 'SC', '(48) 90987-6543', 'camila.barbosa@email.com');

-- 15 cars (some with customer_id, some NULL)
INSERT INTO cars (brand, model, color, year_manufacture, imported, plates, selling_date, selling_price, customer_id) VALUES
('Toyota', 'Corolla', 'Prata', 2020, FALSE, 'ABC1D23', '2023-06-15', 85000.00, 1),
('Honda', 'Civic', 'Preto', 2019, FALSE, 'DEF2E34', '2023-07-20', 78000.00, 2),
('Ford', 'Mustang', 'Vermelho', 2022, TRUE, 'GHI3F45', '2023-08-10', 350000.00, 3),
('Chevrolet', 'Onix', 'Branco', 2021, FALSE, 'JKL4G56', '2023-09-05', 62000.00, 4),
('Volkswagen', 'Golf', 'Azul', 2018, FALSE, 'MNO5H67', '2023-10-12', 55000.00, NULL),
('BMW', 'X5', 'Cinza', 2023, TRUE, 'PQR6I78', NULL, NULL, 5),
('Mercedes-Benz', 'C200', 'Prata', 2020, TRUE, 'STU7J89', '2023-11-18', 180000.00, 6),
('Fiat', 'Argo', 'Vermelho', 2022, FALSE, 'VWX8K90', '2023-12-01', 58000.00, 7),
('Renault', 'Kwid', 'Branco', 2021, FALSE, 'YZA9L01', '2024-01-15', 45000.00, NULL),
('Hyundai', 'HB20', 'Preto', 2020, FALSE, 'BCD0M12', '2024-02-20', 52000.00, 8),
('Jeep', 'Compass', 'Verde', 2023, FALSE, 'EFG1N34', '2024-03-10', 130000.00, 9),
('Audi', 'A4', 'Cinza', 2019, TRUE, 'HIJ2O45', '2024-04-05', 140000.00, NULL),
('Nissan', 'Kicks', 'Prata', 2022, FALSE, 'KLM3P56', '2024-05-12', 75000.00, 10),
('Kia', 'Sportage', 'Branco', 2021, TRUE, 'NOP4Q67', '2024-06-18', 95000.00, 1),
('Peugeot', '208', 'Azul', 2020, FALSE, 'QRS5R78', NULL, NULL, 2);