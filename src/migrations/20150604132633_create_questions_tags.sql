/*** Tags associated with a question ***/
CREATE TABLE IF NOT EXISTS questions_tags (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	question_id BIGINT NOT NULL,					/* the FK of the parent question */
	tag_id BIGINT NOT NULL,							/* the FK of the parent tag */
	created_by BIGINT NOT NULL,						/* the platform partner that tagged the question */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);
