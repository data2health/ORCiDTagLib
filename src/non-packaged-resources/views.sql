select unnest(xpath('/record:record/person:person/person:name/personal-details:family-name/text()',
              raw,
              '{{record,http://www.orcid.org/ns/record},{person,http://www.orcid.org/ns/person},
                {personal-details,http://www.orcid.org/ns/personal-details}
               }'
        ))
from xml;

-- need to also explore xtable as a means of disassembling this
