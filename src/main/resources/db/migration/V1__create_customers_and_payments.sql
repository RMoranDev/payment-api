-- ============================================
-- V1: Create customers and payments tables
-- ============================================

-- CUSTOMERS
CREATE TABLE customers
(
    id            UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    customer_type VARCHAR(20)  NOT NULL,
    full_name     VARCHAR(150) NOT NULL,
    email         VARCHAR(150) NOT NULL,
    document      VARCHAR(14)  NOT NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_customers_email
        UNIQUE (email),

    CONSTRAINT uk_customers_document
        UNIQUE (document),

    CONSTRAINT ck_customers_type
        CHECK (customer_type IN ('INDIVIDUAL', 'BUSINESS')),

    CONSTRAINT ck_customers_status
        CHECK (status IN ('ACTIVE', 'INACTIVE')),

    CONSTRAINT ck_customers_document
        CHECK (
            (customer_type = 'INDIVIDUAL' AND document ~ '^[0-9]{11}$')
                OR
            (customer_type = 'BUSINESS' AND document ~ '^[0-9]{14}$')
            )
);

-- PAYMENTS
CREATE TABLE payments
(
    id             UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    customer_id    UUID           NOT NULL,
    amount         NUMERIC(12, 2) NOT NULL,
    currency       CHAR(3)        NOT NULL DEFAULT 'BRL',
    description    VARCHAR(255),
    status         VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(30)    NOT NULL,
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    paid_at        TIMESTAMPTZ,

    CONSTRAINT fk_payments_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers (id),

    CONSTRAINT ck_payments_amount
        CHECK (amount > 0),

    CONSTRAINT ck_payments_currency
        CHECK (currency = 'BRL'),

    CONSTRAINT ck_payments_status
        CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED')),

    CONSTRAINT ck_payments_method
        CHECK (payment_method IN ('PIX', 'CREDIT_CARD', 'DEBIT_CARD')),

    CONSTRAINT ck_payments_paid_at
        CHECK (paid_at IS NULL OR status = 'APPROVED')
);

-- INDEXES
CREATE INDEX idx_payments_customer_id
    ON payments (customer_id);

CREATE INDEX idx_payments_status
    ON payments (status);
