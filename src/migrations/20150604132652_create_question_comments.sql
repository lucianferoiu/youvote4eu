/*** Short comments/flags for a question ***/
CREATE TABLE IF NOT EXISTS comments (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	question_id BIGINT NOT NULL,					/* the FK of the parent question */
	created_by BIGINT NOT NULL,						/* the platform partner that made the comment */
	comment_type VARCHAR(120),						/* the type of comment indicating its purpose */
	text VARCHAR(120),										/* the text of this (short) comment */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);

/* - */
