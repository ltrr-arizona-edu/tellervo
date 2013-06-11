CREATE VIEW vwtblloan AS
SELECT l.loanid, l.firstname, l.lastname, l.organisation, l.duedate, l.issuedate, l.returndate, l.files, l.notes
FROM tblloan l;

ALTER TABLE tblloan ALTER COLUMN issuedate SET DEFAULT now();
ALTER TABLE tblloan ALTER COLUMN issuedate SET NOT NULL;
ALTER TABLE tblloan ADD COLUMN returndate timestamp with time zone;
