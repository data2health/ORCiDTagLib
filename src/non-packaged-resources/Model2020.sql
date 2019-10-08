CREATE TABLE orcid.person (
       id INT NOT NULL
     , orcid_id TEXT
     , given_names TEXT
     , family_name TEXT
     , credit_name TEXT
     , PRIMARY KEY (id)
);

CREATE TABLE orcid.work (
       id INT NOT NULL
     , orcid_id TEXT
     , seqnum INT NOT NULL
     , title TEXT
     , type TEXT
     , pub_year TEXT
     , pub_month TEXT
     , pub_day TEXT
     , PRIMARY KEY (id, seqnum)
     , CONSTRAINT FK_work_1 FOREIGN KEY (id)
                  REFERENCES orcid.person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orcid.biography (
       id INT NOT NULL
     , orcid_id TEXT
     , seqnum INT NOT NULL
     , biography TEXT
     , PRIMARY KEY (id, seqnum)
     , CONSTRAINT FK_biography_1 FOREIGN KEY (id)
                  REFERENCES orcid.person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orcid.education (
       id INT NOT NULL
     , orcid_id TEXT
     , seqnum INT NOT NULL
     , department TEXT
     , title TEXT
     , start_year TEXT
     , start_month TEXT
     , start_day TEXT
     , end_year TEXT
     , end_month TEXT
     , end_day TEXT
     , organization TEXT
     , city TEXT
     , region TEXT
     , country TEXT
     , org_id TEXT
     , id_source TEXT
     , PRIMARY KEY (id, seqnum)
     , CONSTRAINT FK_education_1 FOREIGN KEY (id)
                  REFERENCES orcid.person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orcid.email (
       id INT NOT NULL
     , orcid_id TEXT
     , seqnum INT NOT NULL
     , email TEXT
     , PRIMARY KEY (id, seqnum)
     , CONSTRAINT FK_email_1 FOREIGN KEY (id)
                  REFERENCES orcid.person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orcid.employment (
       id INT NOT NULL
     , orcid_id TEXT
     , seqnum INT NOT NULL
     , department TEXT
     , title TEXT
     , start_year TEXT
     , start_month TEXT
     , start_day TEXT
     , end_year TEXT
     , end_month TEXT
     , end_day TEXT
     , organization TEXT
     , city TEXT
     , region TEXT
     , country TEXT
     , org_id TEXT
     , id_source TEXT
     , PRIMARY KEY (id, seqnum)
     , CONSTRAINT FK_employment_1 FOREIGN KEY (id)
                  REFERENCES orcid.person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orcid.external_identifier (
       id INT NOT NULL
     , orcid_id TEXT
     , seqnum INT NOT NULL
     , type TEXT
     , value TEXT
     , url TEXT
     , relationship TEXT
     , PRIMARY KEY (id, seqnum)
     , CONSTRAINT FK_external_identifier_1 FOREIGN KEY (id)
                  REFERENCES orcid.person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orcid.funding (
       id INT NOT NULL
     , orcid_id TEXT
     , seqnum INT NOT NULL
     , title TEXT
     , translated_title TEXT
     , id_type TEXT
     , id_value TEXT
     , id_relationship TEXT
     , start_year TEXT
     , start_month TEXT
     , start_day TEXT
     , end_year TEXT
     , end_month TEXT
     , end_day TEXT
     , organization TEXT
     , city TEXT
     , region TEXT
     , country TEXT
     , org_id TEXT
     , id_source TEXT
     , PRIMARY KEY (id, seqnum)
     , CONSTRAINT FK_funding_1 FOREIGN KEY (id)
                  REFERENCES orcid.person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orcid.history (
       id INT NOT NULL
     , creation_method TEXT
     , submission_date TIMESTAMP
     , last_modified TIMESTAMP
     , claimed BOOL
     , verified_email BOOL
     , verified_primary_email BOOL
     , PRIMARY KEY (id)
     , CONSTRAINT FK_history_1 FOREIGN KEY (id)
                  REFERENCES orcid.person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orcid.keyword (
       id INT NOT NULL
     , orcid_id TEXT
     , seqnum INT NOT NULL
     , keyword TEXT
     , PRIMARY KEY (id, seqnum)
     , CONSTRAINT FK_keyword_1 FOREIGN KEY (id)
                  REFERENCES orcid.person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orcid.other_name (
       id INT NOT NULL
     , orcid_id TEXT
     , seqnum INT NOT NULL
     , other_name TEXT
     , PRIMARY KEY (id, seqnum)
     , CONSTRAINT FK_other_name_1 FOREIGN KEY (id)
                  REFERENCES orcid.person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orcid.researcher_url (
       id INT NOT NULL
     , orcid_id TEXT
     , seqnum INT NOT NULL
     , url TEXT
     , name TEXT
     , PRIMARY KEY (id, seqnum)
     , CONSTRAINT FK_researcher_url_1 FOREIGN KEY (id)
                  REFERENCES orcid.person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orcid.work_external_id (
       id INT NOT NULL
     , orcid_id TEXT
     , seqnum INT NOT NULL
     , seqnum2 INT NOT NULL
     , type TEXT
     , value TEXT
     , relationship TEXT
     , PRIMARY KEY (id, seqnum, seqnum2)
     , CONSTRAINT FK_work_external_id_1 FOREIGN KEY (id, seqnum)
                  REFERENCES orcid.work (id, seqnum) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orcid.address (
       id INT NOT NULL
     , orcid_id TEXT
     , seqnum INT NOT NULL
     , country TEXT
     , PRIMARY KEY (id, seqnum)
     , CONSTRAINT FK_address_1 FOREIGN KEY (id)
                  REFERENCES orcid.person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

