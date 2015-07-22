/* Flag for partner's ability to validate and lock/unlock a translation for a question */
ALTER TABLE partners
ADD COLUMN can_validate_translation BOOLEAN DEFAULT false;

/* A flag marking a translation as verified */
ALTER TABLE translations
ADD COLUMN verified BOOLEAN DEFAULT false;