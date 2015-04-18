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
	is_approved BOOLEAN DEFAULT false,				/* is this question approved (assuming it comes from individual partners) */
	created_by BIGINT,								/* the platform partner that introduced this question */
	tags_bitmap BIT VARYING,						/* bitmap of the question tags [PSQL speciffic]: nth bit correspond to the tag with ID=n */
	open_at TIMESTAMP WITH TIME ZONE,				/* the question become open for voting at the specified date/time */
	closed_at TIMESTAMP WITH TIME ZONE,				/* the question become closed for voting at the specified date/time */
	/* - */
	created_at TIMESTAMP WITH TIME ZONE,
	updated_at TIMESTAMP WITH TIME ZONE
);