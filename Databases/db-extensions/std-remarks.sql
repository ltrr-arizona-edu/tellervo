COPY tlkpreadingnote (readingnoteid, note, vocabularyid, standardisedid, parentreadingid, parentvmrelyearreadingnoteid) FROM stdin;
147	fire damage	1	\N	\N	\N
148	frost damage	1	\N	\N	\N
149	crack	1	\N	\N	\N
150	false ring(s)	1	\N	\N	\N
151	compression wood	1	\N	\N	\N
152	tension wood	1	\N	\N	\N
153	traumatic ducts	1	\N	\N	\N
154	unspecified injury	1	\N	\N	\N
155	single pinned	1	\N	\N	\N
156	double pinned	1	\N	\N	\N
157	triple pinned	1	\N	\N	\N
158	missing ring	1	\N	\N	\N
159	radius shift up	1	\N	\N	\N
160	radius shift down	1	\N	\N	\N
161	moon ring(s)	1	\N	\N	\N
162	diffuse latewood	1	\N	\N	\N
163	density fluctuation	1	\N	\N	\N
164	wide late wood	1	\N	\N	\N
165	wide early wood	1	\N	\N	\N
166	insect damage	2	165	\N	\N
\.

Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar - position undetermined', 4, 'U');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury - position undetermined', 4, 'u');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in latewood', 4, 'A');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in latewood', 4, 'a');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in dormant position', 4, 'D');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in dormant position', 4, 'd');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in first third of earlywood', 4, 'E');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in first third of earlywood', 4, 'e');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in middle third of earlywood', 4, 'M');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in middle third of earlywood', 4, 'm');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire scar in last third of earlywood', 4, 'L');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Fire injury in last third of earlywood', 4, 'l');
Insert into tlkpreadingnote (note, vocabularyid, standardisedid) values ('Not recording fires', 4, '.');
