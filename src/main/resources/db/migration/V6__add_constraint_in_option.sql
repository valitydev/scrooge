ALTER TABLE scrooge.option DROP CONSTRAINT IF EXISTS unique_option_key;
ALTER TABLE scrooge.option
    ADD CONSTRAINT unique_option_row UNIQUE (key, adapter_id, terminal_ref, account_id);