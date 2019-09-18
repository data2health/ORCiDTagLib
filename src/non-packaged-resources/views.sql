select unnest(xpath('/record:record/person:person/person:name/personal-details:family-name/text()',
              raw,
              '{{record,http://www.orcid.org/ns/record},{person,http://www.orcid.org/ns/person},
                {personal-details,http://www.orcid.org/ns/personal-details}
               }'
        ))
from xml;

-- need to also explore xtable as a means of disassembling this

-- this one returns the person name construct as a valid xml construct

select id,orcid_id,xmltable.* from 
xml,
xmltable( xmlnamespaces('http://www.orcid.org/ns/record' as record, 'http://www.orcid.org/ns/person' as person, 'http://www.orcid.org/ns/personal-details' as "personal-details"),
    '//record:record'
    passing raw
    columns
        path text path '@path',
        person xml path 'person:person/person:name'
        ) limit 5;

-- this one returns the person's family name

		
-- trying a repeating field

select id,xmltable.* from 
xml,
xmltable( xmlnamespaces(
            'http://www.orcid.org/ns/record' as record,
            'http://www.orcid.org/ns/person' as person,
            'http://www.orcid.org/ns/keyword' as keyword,
            'http://www.orcid.org/ns/activities' as activities,
            'http://www.orcid.org/ns/education' as education,
            'http://www.orcid.org/ns/personal-details' as "personal-details"
            ),
    '//record:record/activities:activities-summary/activities:educations/education:education-summary'
    passing raw
    columns
        ordinality FOR ORDINALITY,
        institution text path 'education:organization/person:name/text()',
        department text path 'education:department-name/text()',
        degree text path 'education:role-title/text()'
        ) limit 5;

-- ----- view definitions

create view staging_history as
select id,xmltable.* from 
xml,
xmltable( xmlnamespaces(
            'http://www.orcid.org/ns/record' as record,
            'http://www.orcid.org/ns/history' as history,
            'http://www.orcid.org/ns/common' as common
            ),
    '//record:record/history:history'
    passing raw
    columns
        creation_method text path 'history:creation-method/text()',
        submission_date timestamp path 'history:submission-date/text()',
        last_modified timestamp path 'history:submission-date/text()',
        claimed boolean path 'history:claimed/text()',
        verified_email boolean path 'history:verified-email/text()',
        verified_primary_email boolean path 'history:verified-primary-email/text()'
        );

create view staging_person as
select id,orcid_id,xmltable.* from 
xml,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/record' as record,
        'http://www.orcid.org/ns/person' as person,
        'http://www.orcid.org/ns/personal-details' as "personal-details",
        'http://www.orcid.org/ns/other-name' as "other-name",
        'http://www.orcid.org/ns/email' as email,
        'http://www.orcid.org/ns/address' as address,
        'http://www.orcid.org/ns/keyword' as keyword,
        'http://www.orcid.org/ns/external-identifier' as "external-identifier",
        'http://www.orcid.org/ns/researcher-url' as "researcher-url"
        ),
    '//record:record'
    passing raw
    columns
        given_names text path 'person:person/person:name/personal-details:given-names/text()',
        family_name text path 'person:person/person:name/personal-details:family-name/text()',
        credit_name text path 'person:person/person:name/personal-details:credit-name/text()',
        other_names xml path 'person:person/other-name:other-names',
        biography xml path 'person:person/person:biography',
        researcher_urls xml path 'person:person/researcher-url:researcher-urls',
        emails xml path 'person:person/email:emails',
        addresses xml path 'person:person/address:addresses',
        keywords xml path 'person:person/keyword:keywords',
        external_identifiers xml path 'person:person/external-identifier:external-identifiers'
        );

create view staging_other_name as
select id,orcid_id,xmltable.* from 
staging_person,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/other-name' as "other-name",
        'http://www.orcid.org/ns/keyword' as keyword
        ),
    '//other-name:other-names/other-name:other-name'
    passing other_names
    columns
        seqnum FOR ORDINALITY,
        other_name text path 'keyword:content/text()'
        );

create view staging_biography as
select id,orcid_id,xmltable.* from 
staging_person,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/person' as person,
        'http://www.orcid.org/ns/other-name' as "other-name",
        'http://www.orcid.org/ns/keyword' as keyword
        ),
    '//person:biography'
    passing biography
    columns
        seqnum FOR ORDINALITY,
        biography text path 'keyword:content/text()'
        );

create view staging_researcher_url as
select id,orcid_id,xmltable.* from 
staging_person,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/person' as person,
        'http://www.orcid.org/ns/researcher-url' as url
        ),
    '//url:researcher-urls/url:researcher-url'
    passing researcher_urls
    columns
        seqnum FOR ORDINALITY,
        url text path 'url:url/text()',
        name text path 'url:url-name/text()'
        );

create view staging_email as
select id,orcid_id,xmltable.* from 
staging_person,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/person' as person,
        'http://www.orcid.org/ns/email' as email
        ),
    '//email:emails/email:email'
    passing emails
    columns
        seqnum FOR ORDINALITY,
        email text path 'email:email/text()'
        );

create view staging_address as
select id,orcid_id,xmltable.* from 
staging_person,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/person' as person,
        'http://www.orcid.org/ns/address' as address
        ),
    '//address:addresses/address:address'
    passing addresses
    columns
        seqnum FOR ORDINALITY,
        country text path 'address:country/text()'
        );

create view staging_keyword as
select id,orcid_id,xmltable.* from 
staging_person,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/person' as person,
        'http://www.orcid.org/ns/keyword' as keyword
        ),
    '//keyword:keywords/keyword:keyword'
    passing keywords
    columns
        seqnum FOR ORDINALITY,
        keyword text path 'keyword:content/text()'
        );

create view staging_external_identifier as
select id,orcid_id,xmltable.* from 
staging_person,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/person' as person,
        'http://www.orcid.org/ns/common' as common,
        'http://www.orcid.org/ns/external-identifier' as external
        ),
    '//external:external-identifiers/external:external-identifier'
    passing external_identifiers
    columns
        seqnum FOR ORDINALITY,
        type text path 'common:external-id-type/text()',
        value text path 'common:external-id-value/text()',
        url text path 'common:external-id-url/text()',
        relationship text path 'common:external-id-relationship/text()'
        );

create view staging_activity as
select id,orcid_id,xmltable.* from 
xml,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/record' as record,
        'http://www.orcid.org/ns/activities' as activities
        ),
    '//record:record'
    passing raw
    columns
        educations xml path 'activities:activities-summary/activities:educations',
        employments xml path 'activities:activities-summary/activities:employments',
        fundings xml path 'activities:activities-summary/activities:fundings',
        peer_reviews xml path 'activities:activities-summary/activities:peer-reviews',
        works xml path 'activities:activities-summary/activities:works'
        );

create view staging_education as
select id,orcid_id,xmltable.* from 
staging_activity,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/activities' as activities,
        'http://www.orcid.org/ns/person' as person,
        'http://www.orcid.org/ns/common' as common,
        'http://www.orcid.org/ns/address' as address,
        'http://www.orcid.org/ns/education' as education
        ),
    '//activities:educations/education:education-summary'
    passing educations
    columns
        seqnum FOR ORDINALITY,
        department text path 'education:department-name/text()',
        title text path 'education:role-title/text()',
        start_year text path 'common:start-date/common:year/text()',
        start_month text path 'common:start-date/common:month/text()',
        start_day text path 'common:start-date/common:day/text()',
        end_year text path 'common:end-date/common:year/text()',
        end_month text path 'common:end-date/common:month/text()',
        end_day text path 'common:end-date/common:day/text()',
        organization text path 'education:organization/person:name/text()',
        city text path 'education:organization/address:address/common:city/text()',
        region text path 'education:organization/address:address/common:region/text()',
        country text path 'education:organization/address:address/address:country/text()',
        org_id text path 'education:organization/common:disambiguated-organization/common:disambiguated-organization-identifier/text()',
        id_source text path 'education:organization/common:disambiguated-organization/common:disambiguation-source/text()'
        );

create view staging_employment as
select id,orcid_id,xmltable.* from 
staging_activity,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/activities' as activities,
        'http://www.orcid.org/ns/person' as person,
        'http://www.orcid.org/ns/common' as common,
        'http://www.orcid.org/ns/address' as address,
        'http://www.orcid.org/ns/education' as education,
        'http://www.orcid.org/ns/employment' as employment
        ),
    '//activities:employments/employment:employment-summary'
    passing employments
    columns
        seqnum FOR ORDINALITY,
        department text path 'education:department-name/text()',
        title text path 'education:role-title/text()',
        start_year text path 'common:start-date/common:year/text()',
        start_month text path 'common:start-date/common:month/text()',
        start_day text path 'common:start-date/common:day/text()',
        end_year text path 'common:end-date/common:year/text()',
        end_month text path 'common:end-date/common:month/text()',
        end_day text path 'common:end-date/common:day/text()',
        organization text path 'education:organization/person:name/text()',
        city text path 'education:organization/address:address/common:city/text()',
        region text path 'education:organization/address:address/common:region/text()',
        country text path 'education:organization/address:address/address:country/text()',
        org_id text path 'education:organization/common:disambiguated-organization/common:disambiguated-organization-identifier/text()',
        id_source text path 'education:organization/common:disambiguated-organization/common:disambiguation-source/text()'
        );

create view staging_funding as
select id,orcid_id,xmltable.* from 
staging_activity,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/activities' as activities,
        'http://www.orcid.org/ns/person' as person,
        'http://www.orcid.org/ns/common' as common,
        'http://www.orcid.org/ns/address' as address,
        'http://www.orcid.org/ns/education' as education,
        'http://www.orcid.org/ns/work' as work,
        'http://www.orcid.org/ns/funding' as funding
        ),
    '//activities:fundings/activities:group/funding:funding-summary'
    passing fundings
    columns
        seqnum FOR ORDINALITY,
        title text path 'work:title/work:title/text()',
        translated_title text path 'work:title/common:translated-title/text()',
        id_type xml path 'common:external-ids/common:external-id/common:external-id-type/text()',
        id_value xml path 'common:external-ids/common:external-id/common:external-id-value/text()',
        id_relationship xml path 'common:external-ids/common:external-id/common:external-id-relationship/text()',
        start_year text path 'common:start-date/common:year/text()',
        start_month text path 'common:start-date/common:month/text()',
        start_day text path 'common:start-date/common:day/text()',
        end_year text path 'common:end-date/common:year/text()',
        end_month text path 'common:end-date/common:month/text()',
        end_day text path 'common:end-date/common:day/text()',
        organization text path 'education:organization/person:name/text()',
        city text path 'education:organization/address:address/common:city/text()',
        region text path 'education:organization/address:address/common:region/text()',
        country text path 'education:organization/address:address/address:country/text()',
        org_id text path 'education:organization/common:disambiguated-organization/common:disambiguated-organization-identifier/text()',
        id_source text path 'education:organization/common:disambiguated-organization/common:disambiguation-source/text()'
        );

create view staging_work as
select id,orcid_id,xmltable.* from 
staging_activity,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/activities' as activities,
        'http://www.orcid.org/ns/common' as common,
        'http://www.orcid.org/ns/work' as work
        ),
    '//activities:works/activities:group/work:work-summary'
    passing works
    columns
        seqnum FOR ORDINALITY,
        title text path 'work:title/work:title/text()',
        external_ids xml path 'common:external-ids',
        type text path 'work:type/text()',
        pub_year text path 'common:publication-date/common:year/text()',
        pub_month text path 'common:publication-date/common:month/text()',
        pub_day text path 'common:publication-date/common:day/text()'
        );

create view staging_work_external_id as
select id,orcid_id,seqnum,xmltable.* from 
staging_work,
xmltable(
    xmlnamespaces(
        'http://www.orcid.org/ns/activities' as activities,
        'http://www.orcid.org/ns/common' as common,
        'http://www.orcid.org/ns/work' as work
        ),
    '//common:external-ids/common:external-id'
    passing external_ids
    columns
        seqnum2 FOR ORDINALITY,
        type text path 'common:external-id-type/text()',
        value text path 'common:external-id-value/text()',
        relationship text path 'common:external-id-relationship/text()'
        );
