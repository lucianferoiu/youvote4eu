/*** The table holding pending email validations ***/
CREATE TABLE IF NOT EXISTS email_validations (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	email VARCHAR(120) NOT NULL,						/* email to be validated */
	token VARCHAR(36) NOT NULL,							/* a randomly-generated unique string sent to email address and expecting to be returned by HTTP(S) */
	valid_until TIMESTAMP WITH TIME ZONE,				/* the deadline until this validation is available - batch delete if past date */
	validated BOOLEAN DEFAULT false,					/* the token was validated via email (i.e. this validation is obsolete - batch delete tonight) */
	is_citizen BOOLEAN DEFAULT false,					/* to distinguish between citizen validations and partner registrations */
	is_registration BOOLEAN DEFAULT false,				/* mutually exclusive with the above */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);
