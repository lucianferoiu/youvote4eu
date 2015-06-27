CREATE FUNCTION cleanup() RETURNS void AS $$
	DECLARE
	    rec RECORD;
	    cmd text;
	BEGIN
	    cmd := '';

	    FOR rec IN SELECT
	            'DROP SEQUENCE ' || quote_ident(n.nspname) || '.'
	                || quote_ident(c.relname) || ' CASCADE;' AS name
	        FROM
	            pg_catalog.pg_class AS c
	        LEFT JOIN
	            pg_catalog.pg_namespace AS n
	        ON
	            n.oid = c.relnamespace
	        WHERE
	            relkind = 'S' AND
	            n.nspname NOT IN ('pg_catalog', 'pg_toast') AND
	            pg_catalog.pg_table_is_visible(c.oid)
	    LOOP
	        cmd := cmd || rec.name;
	    END LOOP;

	    FOR rec IN SELECT
	            'DROP TABLE ' || quote_ident(n.nspname) || '.'
	                || quote_ident(c.relname) || ' CASCADE;' AS name
	        FROM
	            pg_catalog.pg_class AS c
	        LEFT JOIN
	            pg_catalog.pg_namespace AS n
	        ON
	            n.oid = c.relnamespace WHERE relkind = 'r' AND
	            n.nspname NOT IN ('pg_catalog', 'pg_toast') AND
	            pg_catalog.pg_table_is_visible(c.oid)
	    LOOP
	        cmd := cmd || rec.name;
	    END LOOP;


    EXECUTE cmd;
    RETURN;
END;
$$ LANGUAGE plpgsql;
