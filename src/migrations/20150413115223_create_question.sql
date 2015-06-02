/*** The question subject to voting by citizens ***/
CREATE TABLE IF NOT EXISTS questions (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	title_en VARCHAR(1024),							/* question title (english version) */
	title_id BIGINT,								/* link to the multi-language labes of the title - pseudo-FK on labels.label_group */
	description_en VARCHAR(4080),					/* a short description of the question (english version) */
	description_id BIGINT,							/* link to the multi-language labes of the question description - pseudo-FK on labels.label_group */
	html_content_en VARCHAR(65000),					/* question body (English version), allowing for HTML content */
	html_content_id BIGINT,							/* link to the multi-language labes of the HTML content of the question body - pseudo-FK on labels.label_group */
	picture_path VARCHAR(120),						/* relative server path for picture accompanying the question */
	is_public_agenda BOOLEAN,						/* is this question a public-agenda topic (i.e. EU Parliament, etc.)? or an ancilary question */
	is_published BOOLEAN DEFAULT false,				/* is this question approved/published and ready for voting */
	is_archived BOOLEAN DEFAULT false,				/* is this question archived */
	is_deleted BOOLEAN DEFAULT false,				/* in case we decide against physically deleting the questions */
	proposed_by BIGINT NOT NULL,					/* the platform partner that introduced this question */
	upvotes BIGINT DEFAULT 1,						/* number of upvotes (support) that a question gained in order to be published */
	tags_bitmap BIT VARYING,						/* bitmap of the question tags [PSQL speciffic]: nth bit correspond to the tag with ID=n */
	flags_bitmap BIT VARYING,						/* bitmap of the question flags [PSQL speciffic]: nth bit correspond to the flag with ID=n */
	popular_votes BIGINT DEFAULT 0,					/* total votes cast by the public (on the website) */
	popular_vote_tally NUMERIC(3),					/* summary of the YES votes percentage of the popular vote (on the website) */
	official_vote_tally NUMERIC(3),					/* summary of the YES votes percentage of the official/institutional vote (if public agenda topic) */
	open_at TIMESTAMP WITH TIME ZONE,				/* the question become open for voting at the specified date/time */
	closed_at TIMESTAMP WITH TIME ZONE,				/* the question become closed for voting at the specified date/time */
	archived_at TIMESTAMP WITH TIME ZONE,			/* the question was archived at the specified date/time */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);