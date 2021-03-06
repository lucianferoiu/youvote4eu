/*** The enum of the countries (primarily EU member states) ***/
CREATE TABLE IF NOT EXISTS countries (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	code CHAR(2) NOT NULL,								/* ISO 3166-1 alpha-2 two-letter code of the country */
	is_eu BOOLEAN DEFAULT false,					/* is this country an EU member state? */
	label VARCHAR(120),										/* label of the country (english version) */
	preferred_lang CHAR(2) DEFAULT 'en',	/* preferred language of the country (null or 'en' if multi-language country) */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
	/*		- see translations for the one-to-many polymorphic translations of the label */

);
