/*** The platform partners, users capable of managing the content of the application ***/
CREATE TABLE IF NOT EXISTS partners (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	email VARCHAR(120) NOT NULL,						/* email to be used for communication/notifications - also acts as 'username' and is used for validation of the account */
	password VARCHAR,									/* the SHA-512 digest of the password, encoded in base 64 - we never store clear text passwords */
	name VARCHAR(120),									/* optional name to be used in the interface */
	auth_token VARCHAR(120),							/* "remember me" autologin authentication token */
	verified BOOLEAN DEFAULT false,						/* authentication is forbidden until the validation via email */
	is_organization BOOLEAN DEFAULT false,				/* to distinguish between individual partners and organizations like partner NGOs, etc. */
	first_login TIMESTAMP WITH TIME ZONE,
	last_login TIMESTAMP WITH TIME ZONE,
	/* - authorization - */
	enabled BOOLEAN DEFAULT true,						/* account can be retired/suspended etc. */
	can_add_question BOOLEAN DEFAULT true,				/* adding a question on whatever topic */
	can_edit_own_question BOOLEAN DEFAULT true,			/* in case we need to restrict access to only questions that were added by each partner */
	can_edit_any_question BOOLEAN DEFAULT false,
	can_delete_any_question BOOLEAN DEFAULT false,
	can_archive_any_question BOOLEAN DEFAULT false,
	can_change_translation BOOLEAN DEFAULT false,		/* this should be combined (AND-ed) with the edit any/own questions */
	can_approve_question BOOLEAN DEFAULT false,			/* for questions vetting/approval */
	can_view_statistics BOOLEAN DEFAULT true,
	can_manage_partners BOOLEAN DEFAULT false,			/* sysadmin-like */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);
