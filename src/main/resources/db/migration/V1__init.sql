CREATE SCHEMA IF NOT EXISTS scrooge;

CREATE TABLE scrooge.provider
(
    id          SERIAL            NOT NULL,
    name        CHARACTER VARYING NOT NULL,
    description CHARACTER VARYING,

    CONSTRAINT provider_pkey PRIMARY KEY (id)
);

CREATE TABLE scrooge.account
(
    id          BIGSERIAL         NOT NULL,
    currency    CHARACTER VARYING NOT NULL,
    number      CHARACTER VARYING NOT NULL,
    provider_id INT               NOT NULL,

    CONSTRAINT account_pkey PRIMARY KEY (id),
    CONSTRAINT account_provider_f_key FOREIGN KEY (provider_id) REFERENCES scrooge.provider (id)
);

CREATE TABLE scrooge.balance
(
    id         BIGSERIAL         NOT NULL,
    value      CHARACTER VARYING NOT NULL,
    timestamp  TIMESTAMP WITHOUT TIME ZONE,
    account_id BIGINT            NOT NULL,

    CONSTRAINT balance_account_pkey PRIMARY KEY (id),
    CONSTRAINT balance_account_f_key FOREIGN KEY (account_id) REFERENCES scrooge.account (id)
);

CREATE TABLE scrooge.terminal
(
    id          BIGSERIAL         NOT NULL,
    name        CHARACTER VARYING NOT NULL,
    description CHARACTER VARYING,
    provider_id INT               NOT NULL,

    CONSTRAINT terminal_pkey PRIMARY KEY (id),
    CONSTRAINT terminal_provider_f_key FOREIGN KEY (provider_id) REFERENCES scrooge.provider (id)
);

CREATE TABLE scrooge.adapter
(
    id          BIGSERIAL         NOT NULL,
    url         CHARACTER VARYING NOT NULL,
    provider_id INT               NOT NULL,

    CONSTRAINT adapter_pkey PRIMARY KEY (id),
    CONSTRAINT adapter_provider_f_key FOREIGN KEY (provider_id) REFERENCES scrooge.provider (id)
);

CREATE TABLE scrooge.option
(
    id         BIGSERIAL         NOT NULL,
    key        CHARACTER VARYING NOT NULL,
    value      CHARACTER VARYING NOT NULL,
    adapter_id BIGINT            NOT NULL,

    CONSTRAINT option_pkey PRIMARY KEY (id),
    CONSTRAINT option_adapter_f_key FOREIGN KEY (adapter_id) REFERENCES scrooge.adapter (id)
)

