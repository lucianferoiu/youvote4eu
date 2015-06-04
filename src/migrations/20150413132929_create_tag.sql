/*** The enum of the searcheable topics/tags pertaining to the questions list ***/
CREATE TABLE IF NOT EXISTS tags (
	id BIGINT PRIMARY KEY,
	/* - */
	label VARCHAR(120) NOT NULL,							/* label of the tag (english version) */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
	/*		- see translations for the one-to-many polymorphic translations of the tag label */
);