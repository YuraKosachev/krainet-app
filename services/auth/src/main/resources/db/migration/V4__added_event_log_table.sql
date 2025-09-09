CREATE TABLE event_logs
(
    id          UUID                        NOT NULL,
    description VARCHAR(255)                NOT NULL,
    subject     VARCHAR(255),
    status      SMALLINT                    NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_event_logs PRIMARY KEY (id)
);