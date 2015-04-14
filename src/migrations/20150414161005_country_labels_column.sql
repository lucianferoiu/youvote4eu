/* The name of the country should also be translated.. link to the multi-language labes of the question description - pseudo-FK on labels.label_group */
ALTER TABLE countries DROP COLUMN label_native;
ALTER TABLE countries ADD COLUMN label_id BIGINT;