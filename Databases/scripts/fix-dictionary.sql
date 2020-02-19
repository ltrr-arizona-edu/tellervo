-- Fix element type dictionary when missing entry for 500-log means larger pkeys are offset by 1

alter table tblelement drop constraint "fkey_element-elementtype";

update tlkpelementtype set elementtypeid=elementtypeid+10001 where elementtypeid>499;

insert into tlkpelementtype (elementtypeid, elementtype, vocabularyid) values (500, 'log', 5);

update tlkpelementtype set elementtypeid=elementtypeid-10000 where elementtypeid>10000;

alter table tblelement add constraint "fkey_element-elementtype" FOREIGN KEY (elementtypeid) REFERENCES tlkpelementtype(elementtypeid);
