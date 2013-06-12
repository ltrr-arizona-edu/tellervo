CREATE VIEW vwtblloan AS
SELECT l.loanid, l.firstname, l.lastname, l.organisation, l.duedate, l.issuedate, l.returndate, array_to_string(l.files, '><'::text) AS files, l.notes
FROM tblloan l;

ALTER TABLE tblloan ALTER COLUMN issuedate SET DEFAULT now();
ALTER TABLE tblloan ALTER COLUMN issuedate SET NOT NULL;
ALTER TABLE tblloan ADD COLUMN returndate timestamp with time zone;
