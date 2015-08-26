/*** The question subject to voting by citizens ***/
CREATE TABLE IF NOT EXISTS questions (
	id BIGSERIAL PRIMARY KEY,
	/* - */
	title VARCHAR(1024) NOT NULL,					/* question title (english version) */
	description VARCHAR(4080) NOT NULL,		/* a short description of the question (english version) */
	html_content VARCHAR(65000),					/* question body (English version), allowing for HTML content */
	picture_path VARCHAR(120),						/* relative server path for picture accompanying the question */
	wordy_url_id VARCHAR(120),						/* reasonable identifier by words in the title to form an URL id */
	picture_attribution VARCHAR(120),			/* copyright notice or attribution of the picture */
	is_published BOOLEAN DEFAULT false,		/* is this question approved/published and ready for voting */
	is_archived BOOLEAN DEFAULT false,		/* is this question archived */
	is_deleted BOOLEAN DEFAULT false,			/* in case we decide against physically deleting the questions */
	proposed_by BIGINT NOT NULL,					/* the platform partner that introduced this question */
	support BIGINT DEFAULT 1,							/* number of upvotes (support) that a question gained in order to be published */
	popular_votes BIGINT DEFAULT 0,				/* total votes cast by the public (on the website) */
	popular_vote_tally NUMERIC(6,3),			/* summary of the YES votes percentage of the popular vote (on the website) */
	open_at TIMESTAMP WITH TIME ZONE,			/* the question become open for voting at the specified date/time */
	closed_at TIMESTAMP WITH TIME ZONE,		/* the question become closed for voting at the specified date/time */
	archived_at TIMESTAMP WITH TIME ZONE,	/* the question was archived at the specified date/time */
	archival_conclusion VARCHAR(4092),		/* text describing the conclusion of the archival */
	/* - */
	campaign_name VARCHAR(120),						/* name of the civic movement/campain regarding this question/topic */
	campaign_link VARCHAR(1024),					/* link to the civic movement/campain */
	facebook_page VARCHAR(1024),					/* dedicated FB page of this question */
	twitter_hashtag VARCHAR(64),					/* dedicated Twitter #hashtag */
	correspondence_email VARCHAR(120),		/* dedicated email addres for correspondence on this topic */
	/* - */
	is_public_agenda BOOLEAN,							/* is this question a public-agenda topic (i.e. EU Parliament, etc.)? or an ancilary question */
	official_vote_tally NUMERIC(6,3),			/* summary of the YES votes percentage of the official/institutional vote (if public agenda topic) */
	parliament_vote_tally NUMERIC(6,3),							/* YES vote percentage of the European Parliament vote (if public agenda topic) */
	parliament_voted_on TIMESTAMP WITH TIME ZONE,		/* the EP decision was made on the specified datetime */
	parliament_vote_link VARCHAR(1024),							/* the EP decision is available at this link */
	council_vote_tally NUMERIC(6,3),								/* YES vote percentage of the European Council vote (if public agenda topic) */
	council_voted_on TIMESTAMP WITH TIME ZONE,			/* the Council decision was made on the specified datetime */
	council_vote_link VARCHAR(1024),								/* the Council decision is available at this link */
	commission_decision BOOLEAN,										/* YES/NO decision of the European Commission (if public agenda topic) */
	commission_decided_on TIMESTAMP WITH TIME ZONE,	/* the Commission decision was made on the specified datetime */
	commission_decision_link VARCHAR(1024),					/* the Commission decision is available at this link */

	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
	/*		- see translations for the one-to-many polymorphic translations of title, description and html_content */
	/*		- see questions_tags for the many-to-many tagging of the questions */
	/*		- see questions_upvotes for the one-to-many upvotes received by this question */
	/*		- see questions_comments for the one-to-many comments made upon this question */

);
