/** Add the banning of partner information **/
ALTER TABLE partners
	ADD COLUMN banned_at TIMESTAMP WITH TIME ZONE;

ALTER TABLE partners
	ADD COLUMN ban_reason VARCHAR(4098);

ALTER TABLE partners
	ADD COLUMN banned_by BIGINT;
