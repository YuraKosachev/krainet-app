CREATE TABLE notifications
(
    id         UUID                        NOT NULL,
    subject    VARCHAR(2000)               NOT NULL,
    body       TEXT                        NOT NULL,
    emails     TEXT                        NOT NULL,
    status     VARCHAR(255)                NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);