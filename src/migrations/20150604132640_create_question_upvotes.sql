/*** Upvotes (partner support) for a question ***/
CREATE TABLE IF NOT EXISTS upvotes (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	question_id BIGINT NOT NULL,					/* the FK of the parent question */
	created_by BIGINT NOT NULL,						/* the platform partner that made the comment */
	value SMALLINT DEFAULT 1,						/* the value of the upvote (if we intend to use an upvoting algorithm based on the partner reputation) */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);
