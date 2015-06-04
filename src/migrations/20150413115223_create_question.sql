/*** The question subject to voting by citizens ***/
CREATE TABLE IF NOT EXISTS questions (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	title VARCHAR(1024) NOT NULL,					/* question title (english version) */
	description VARCHAR(4080) NOT NULL,				/* a short description of the question (english version) */
	html_content VARCHAR(65000),					/* question body (English version), allowing for HTML content */
	picture_path VARCHAR(120),						/* relative server path for picture accompanying the question */
	is_public_agenda BOOLEAN,						/* is this question a public-agenda topic (i.e. EU Parliament, etc.)? or an ancilary question */
	is_published BOOLEAN DEFAULT false,				/* is this question approved/published and ready for voting */
	is_archived BOOLEAN DEFAULT false,				/* is this question archived */
	is_deleted BOOLEAN DEFAULT false,				/* in case we decide against physically deleting the questions */
	proposed_by BIGINT NOT NULL,					/* the platform partner that introduced this question */
	upvotes BIGINT DEFAULT 1,						/* number of upvotes (support) that a question gained in order to be published */
	popular_votes BIGINT DEFAULT 0,					/* total votes cast by the public (on the website) */
	popular_vote_tally NUMERIC(3),					/* summary of the YES votes percentage of the popular vote (on the website) */
	official_vote_tally NUMERIC(3),					/* summary of the YES votes percentage of the official/institutional vote (if public agenda topic) */
	open_at TIMESTAMP WITH TIME ZONE,				/* the question become open for voting at the specified date/time */
	closed_at TIMESTAMP WITH TIME ZONE,				/* the question become closed for voting at the specified date/time */
	archived_at TIMESTAMP WITH TIME ZONE,			/* the question was archived at the specified date/time */
	
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
	/*		- see translations for the one-to-many polymorphic translations of title, description and html_content */
	/*		- see questions_tags for the many-to-many tagging of the questions */
	/*		- see questions_upvotes for the one-to-many upvotes received by this question */
	/*		- see questions_comments for the one-to-many comments made upon this question */
	
);