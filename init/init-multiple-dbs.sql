 IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'krainet_db') THEN
        CREATE DATABASE krainet_db;
    END IF;

 IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'notify_db') THEN
        CREATE DATABASE notify_db;
    END IF;
