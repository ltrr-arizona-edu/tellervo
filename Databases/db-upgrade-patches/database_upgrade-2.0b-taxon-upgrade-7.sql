INSERT INTO tlkptaxon (colid, colparentid, taxonid, parenttaxonid, taxonrankid, label, htmllabel) values ('949X', 'MLD', '949X', 'MLD', 9, 'Acer glabrum Torr.', '<i>Acer glabrum</i> Torr.') ON CONFLICT DO NOTHING;

INSERT INTO tlkptaxon (colid, colparentid, taxonrankid, label, htmllabel, taxonid, parenttaxonid) values ('TG', 'P', 3, 'Tracheophyta', 'Tracheophyta', 'TG', 'P') ON CONFLICT DO NOTHING;
insert into tlkptaxon (taxonrankid, label, htmllabel, taxonid, parenttaxonid) values ('4HQ4Z', '6Q7C', 9, 'Picea sitchensis (Bong.) Carrière', '<i>Picea sitchensis</i> (Bong.) Carrière', '4HQ4Z', '6Q7C') ON CONFLICT DO NOTHING;
