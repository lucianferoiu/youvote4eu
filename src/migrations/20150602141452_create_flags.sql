/*** The enum of the flags set on a question ***/
CREATE TABLE IF NOT EXISTS flags (
	id SMALLINT PRIMARY KEY,
	/* - */
	is_negative BOOLEAN DEFAULT true,				/* the flag may be a good/positive or a bad/negative thing for the question - most are show-stoppers for the question publication */
	label_en VARCHAR(120),							/* label of the language (english version) */
	label_group BIGINT,								/* link to the multi-language labes of the flag */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);