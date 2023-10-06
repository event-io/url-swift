-- DELETION PROCEDURE
-- DDL URL SWIFT
DROP TABLE IF EXISTS url_swift.link_shortening_registry CASCADE;
DROP TABLE IF EXISTS url_swift.link_usages CASCADE;
DROP SCHEMA IF EXISTS url_swift;


-- CREATION PROCEDURE
-- DDL URL SWIFT
CREATE schema url_swift;

CREATE TABLE url_swift.link_shortening_registry (
    shortened_link CHAR(7) NOT NULL UNIQUE,
    original_link VARCHAR(512) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    PRIMARY KEY (shortened_link)
);

CREATE TABLE url_swift.link_usages (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    shortened_link CHAR(7) NOT NULL,
    used_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    session_id UUID NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (shortened_link) REFERENCES url_swift.link_shortening_registry(shortened_link)
);

-- RLS Policies
GRANT USAGE ON SCHEMA url_swift TO anon, authenticated, service_role;
GRANT ALL ON ALL TABLES IN SCHEMA url_swift TO anon, authenticated, service_role;
GRANT ALL ON ALL ROUTINES IN SCHEMA url_swift TO anon, authenticated, service_role;
GRANT ALL ON ALL SEQUENCES IN SCHEMA url_swift TO anon, authenticated, service_role;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA url_swift GRANT ALL ON TABLES TO anon, authenticated, service_role;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA url_swift GRANT ALL ON ROUTINES TO anon, authenticated, service_role;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA url_swift GRANT ALL ON SEQUENCES TO anon, authenticated, service_role;

ALTER TABLE url_swift.link_shortening_registry enable row level security;
ALTER TABLE url_swift.link_usages enable row level security;

CREATE POLICY "Only authorized user can select by id" ON url_swift.link_shortening_registry FOR SELECT TO authenticated
USING ( (auth.jwt() ->> 'email'::text) = 'admin@event.io'::text );

CREATE POLICY "Only authorized user can insert links" ON url_swift.link_shortening_registry FOR INSERT TO authenticated
WITH CHECK ( (auth.jwt() ->> 'email'::text) = 'admin@event.io'::text );

CREATE POLICY "Only authorized user can insert tracking" ON url_swift.link_usages FOR INSERT TO authenticated
WITH CHECK ( (auth.jwt() ->> 'email'::text) = 'admin@event.io'::text );
