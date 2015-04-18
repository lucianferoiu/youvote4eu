/*** Chunks of text translated in the various (EU) languages ***/
CREATE TABLE IF NOT EXISTS labels (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	label_group BIGINT NOT NULL,					/* groups the same "semantic" label accros its translations (fake composite PK component) */
	lang CHAR(2) NOT NULL,							/* code of the language (fake composite PK component) */
	text VARCHAR(65000),							/* question body (English version), allowing for HTML content */
	label_type SMALLINT,							/* the type of label indicating its scope and/or purpose */
	created_by BIGINT,								/* the platform partner that made the translation */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);
