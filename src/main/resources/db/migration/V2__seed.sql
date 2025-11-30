-- Seed de dados de demonstração (usuarios via DataSeeder/Java)

insert into cliente (nome, cpf, email, telefone, endereco, ativo, created_at, updated_at)
values
 ('João da Silva','12345678901','joao@example.com','11999990001','Rua A, 123', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 ('Maria Oliveira','98765432100','maria@example.com','11999990002','Rua B, 456', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 ('Carlos Souza','11122233344','carlos@example.com','11999990003','Av. Central, 789', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into veiculo (marca, modelo, ano, cor, placa, renavam, quilometragem, tipoCombustivel, cambio, precoCompra, precoVendaSugerido, quantidade_estoque, status, ativo, created_at, updated_at)
values
 ('Toyota','Corolla',2020,'Prata','ABC1D23','REN123',35000,'GASOLINA','AUTOMATICO', 85000.00, 98000.00, 5, 'DISPONIVEL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 ('Volkswagen','Gol',2018,'Branco','EFG4H56','REN456',42000,'FLEX','MANUAL', 32000.00, 38000.00, 3, 'DISPONIVEL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 ('Chevrolet','Onix',2021,'Preto','IJK7L89','REN789',21000,'FLEX','AUTOMATICO', 65000.00, 72000.00, 4, 'DISPONIVEL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
