--
-- Data for Name: tlkpuserdefinedfield; Type: TABLE DATA; Schema: public; Owner: tellervo
--

COPY tlkpuserdefinedfield (userdefinedfieldid, fieldname, description, attachedto, datatype, longfieldname, dictionarykey) FROM stdin;
5625746e-357f-4486-bb28-0b751e1315c2    ltrr.barkyear   Year of the bark where measurable or can be determined with confidence  4       xs:int  Bark year       \N
454f7ae1-cbdf-43c9-bf64-92ebd7842632    ltrr.firstyear  First year that is dated/measured       4       xs:int  First year      \N
41aba235-a06c-45f0-a07d-8e2000a93a5f    ltrr.lastyear   Last year that was measured/dated       4       xs:int  Last year       \N
bb44223d-7f69-4f3e-b728-a878f263a825    swarch.innercode        Southwestern Dendroarchaeology code for inner date      4       xs:string       Inner ring code \N
87e2e372-910c-499f-9c33-b125f0014d93    swarch.outercode        Southwestern Dendroarchaeology code for outer date      4       xs:string       Outer ring code \N
a1f3a040-0296-4b57-a760-8080305023ba    ltrr.pithpresent        Whether the pith is present on the sample or not        4       xs:boolean      Pith present    \N
31a778c1-2ee4-4bbe-8285-2705e5cc0bb1    ltrr.pithyear   Year of the pith where measurable or can be determined with confidence  4       xs:int  Pith year       \N
17310eca-36c6-46ed-a5b6-5d7a8fee6cc1    ltrr.unmeasuredinnerrings       Ring count of unmeasured rings towards the pith 4       xs:int  Unmeasured inner rings  \N
b1c793bf-7bb6-42a3-86a5-58740d1d9962    ltrr.unmeasuredouterrings       Ring count of unmeasured rings towards the bark 4       xs:int  Unmeasured outer rings  \N
d402b857-218c-484d-a09e-066998cb41a5    ltrr.lastringunderbark  Whether the last ring under the bark is present or not  4       xs:boolean      Last ring under bark    \N
\.
