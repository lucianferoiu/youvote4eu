/*** The main actor of the voting process, an anonymous citizen ***/
CREATE TABLE IF NOT EXISTS citizens (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	country CHAR(2),											/* if the citizen chose the country - used exclusively for statistics */
	lang CHAR(2) DEFAULT 'EN',						/* preferred language of the citizen (null defaults to English) */
	validated BOOLEAN DEFAULT false,			/* the citizen authenticated his/her presence (i.e. via email) */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);

/**/
