/*** The enum of the languages (primarily EU languages) available for internationalization (both for the UI and the content) ***/
CREATE TABLE IF NOT EXISTS langs (
	id BIGINT PRIMARY KEY,
	/* - */
	code CHAR(2) NOT NULL,								/* ISO 639-1 two-letter code of the language */
	label_en VARCHAR(120),								/* label of the language (english version) */
	label_native VARCHAR(120),						/* label of the language (expressed in the language itself) */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);
