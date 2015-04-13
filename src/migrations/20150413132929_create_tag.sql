/*** The enum of the searcheable topics/tags pertaining to the questions list ***/
CREATE TABLE IF NOT EXISTS tags (
	id SMALLINT PRIMARY KEY,
	/* - */
	label_en VARCHAR(120),							/* label of the language (english version) */
	label_group BIGINT,								/* link to the multi-language labes of the tag */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);