/*** Chunks of text translated in the various (EU) languages - polymorphic child ***/
CREATE TABLE IF NOT EXISTS translations (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	parent_id BIGINT NOT NULL,						/* polymorphic parent (fake composite PK component) */
	parent_type VARCHAR(120) NOT NULL,		/* groups the same "semantic" accros its may polymorphic parents (fake composite PK component) */
	field_type VARCHAR(120),							/* the type of label indicating its scope and/or purpose */
	lang CHAR(2) NOT NULL,								/* code of the language (fake composite PK component) */
	text VARCHAR(65000),									/* translation body allowing for HTML content */
	created_by BIGINT,										/* the platform partner that made the translation */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);
