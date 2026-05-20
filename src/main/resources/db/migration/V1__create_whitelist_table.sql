CREATE TABLE white_list (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT uq_whitelist_user_product UNIQUE (user_id, product_id)
);
