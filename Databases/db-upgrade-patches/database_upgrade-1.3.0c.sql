ALTER TABLE tlkpprojectcategory DROP CONSTRAINT IF EXISTS "fkey_projectcategory-vocabulary";

ALTER TABLE tlkpprojectcategory ADD CONSTRAINT "fkey_projectcategory-vocabulary2" FOREIGN KEY (vocabularyid)
      REFERENCES tlkpvocabulary (vocabularyid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
