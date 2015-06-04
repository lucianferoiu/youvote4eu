/*** The persistent authentication token used to track the anonymous citizen ***/
CREATE TABLE IF NOT EXISTS tokens (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	citizen_id BIGINT NOT NULL,						/* the citizen tracked by this token - add FK constraint here */
	token VARCHAR(36) NOT NULL,						/* a randomly-generated unique string (should be strong enough to avoid collision) */
	validated BOOLEAN DEFAULT false,				/* the token was validated (i.e. via email) as belonging to a real user */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);