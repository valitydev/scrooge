ALTER TABLE scrooge.option
    ADD COLUMN IF NOT EXISTS terminal_ref INT NOT NULL DEFAULT 0;
ALTER TABLE scrooge.option DROP CONSTRAINT option_key_adapter_id_key;
ALTER TABLE scrooge.option
    ADD CONSTRAINT unique_option_key UNIQUE (key, adapter_id, terminal_ref);