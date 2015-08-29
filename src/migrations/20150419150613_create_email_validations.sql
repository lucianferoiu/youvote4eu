/*** The table holding pending email validations ***/
CREATE TABLE IF NOT EXISTS email_validations (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	email VARCHAR(1000) NOT NULL,							/* email to be validated - digested information, no clear-text email addresses */
	token VARCHAR(1000) NOT NULL, 						/* a randomly-generated unique string sent to email address and expecting to be returned by HTTP(S) */
	valid_until TIMESTAMP WITH TIME ZONE,			/* the deadline until this validation is available - batch delete if past date */
	validated BOOLEAN DEFAULT false,					/* the token was validated via email (i.e. this validation is obsolete - batch delete tonight) */
	is_citizen BOOLEAN DEFAULT false,					/* to distinguish between citizen validations and partner registrations */
	is_registration BOOLEAN DEFAULT false,		/* mutually exclusive with the above */
	is_pwd_renew BOOLEAN DEFAULT false,				/* partner validation for password renewal */
	added_by BIGINT,													/* partner who is subscribing a new partner (null in all other cases) or the validating citizen */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);
