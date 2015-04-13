/*** The record of the vote on a question by a citizen ***/
CREATE TABLE IF NOT EXISTS votes (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	question_id BIGINT NOT NULL,					/* the question on which a vote was cast - maybe add FK constraint here */
	citizen_id BIGINT NOT NULL,						/* the citizen that voted on the question - maybe add FK constraint here */
	value SMALLINT NOT NULL,						/* value of the vote - normally 0=No, 1=Yes, but we use small int instead of boolean for future multi-choice questions */
	validated BOOLEAN DEFAULT false,				/* the vote is validated when the citizen is (i.e. via email) */
	cast_at TIMESTAMP WITH TIME ZONE,				/* the vote was cast at the specified date/time */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);