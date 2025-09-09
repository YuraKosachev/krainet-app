CREATE TABLE accounts
(
    id              UUID                        NOT NULL,
    username        VARCHAR(255)                NOT NULL,
    mail            VARCHAR(255)                NOT NULL,
    first_name      VARCHAR(255)                NOT NULL,
    last_name       VARCHAR(255)                NOT NULL,
    hashed_password VARCHAR(60)                 NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status          SMALLINT DEFAULT 0          NOT NULL,
    CONSTRAINT pk_accounts PRIMARY KEY (id)
);

CREATE TABLE tokens
(
    id                    UUID                        NOT NULL,
    account_id            UUID                        NOT NULL,
    access_token          TEXT                        NOT NULL,
    refresh_token         TEXT                        NOT NULL,
    access_token_expires  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    refresh_token_expires TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status                SMALLINT DEFAULT 0          NOT NULL,
    created_at            TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_tokens PRIMARY KEY (id)
);

ALTER TABLE accounts
    ADD CONSTRAINT uc_accounts_mail UNIQUE (mail);

ALTER TABLE accounts
    ADD CONSTRAINT uc_accounts_username UNIQUE (username);

ALTER TABLE tokens
    ADD CONSTRAINT FK_TOKENS_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES accounts (id);