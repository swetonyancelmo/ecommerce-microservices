CREATE TABLE inventory (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL UNIQUE,
    quantity INT NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO inventory (product_id, quantity) VALUES
(1, 100),
(2, 0),
(3, 150),
(4, 300),
(5, 250);