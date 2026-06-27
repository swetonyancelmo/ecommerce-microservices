CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO products(name, description, price) VALUES
('Teclado Mecânico', 'Teclado RGB switch azul', 89.90),
('Mouse Gamer', 'Mouse com sensor óptico de alta precisão', 59.90),
('Monitor 24"', 'Monitor Full HD com taxa de atualização de 144Hz', 499.90),
('Headset Gamer', 'Headset com microfone e som surround', 129.90),
('Cadeira Gamer', 'Cadeira ergonômica com ajuste de altura e reclinação', 399.90);