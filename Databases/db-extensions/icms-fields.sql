--
-- Data for Name: tlkpuserdefinedfield; Type: TABLE DATA; Schema: public; Owner: tellervo
--

COPY tlkpuserdefinedfield (userdefinedfieldid, fieldname, description, attachedto, datatype, longfieldname, dictionarykey) FROM stdin;
027a186a-b312-4bc1-af2d-a4021a387d19    icms.accession  ICMS Accession code     4       xs:string       Accession code  \N
30fbebff-ccfb-4714-b53a-6f9b0a711e07    icms.statusdate Fiscal year for which the status applies        4       xs:int  Status date     \N
1b973165-1a97-4115-aae7-43ee822b4548    icms.ctrlprop   Whether the sample is an item of value, firearm etc     4       xs:boolean      Control property        \N
6a246298-639a-4adc-9509-b3527cc694f0    icms.catalog    ICMS Catalog Code.  Used to uniquely identifer a record in ICMS 4       xs:string       Catalog code    \N
b4d65273-feba-472e-b2a2-6d3bed8c0b59    icms.itemcount  Number of parts the sample is in        4       xs:int  Item count      \N
15bc0104-446d-4c2a-ae46-2b9ae17c4516    icms.catalogdateoverride        Date the sample was cataloged.  If left blank the automated createdtimestamp field is used      4       xs:string       Catalog date override   \N
c887a8a5-070d-4acb-8eed-0649a55fecfa    icms.identdate  Date the sample was dated       4       xs:string       Dated date      \N
457c87fc-f176-4458-a6f0-1b7fe3732af1    icms.statesite  Site code given by the relevant state archeological inventory   4       xs:string       State site code \N
46928774-299b-436b-891f-4b2055b0853f    icms.fieldsite  The site code given by the original collector if different to the Tellervo code 4       xs:string       Field site code \N
d576088a-c18b-4623-8ffe-16101249178f    icms.fieldspecimen      Original code given to this sample by the collector     4       xs:string       Field specimen code     \N
775d7b76-b13b-4606-8d2a-153978355f53    icms.cataloger  Person would initially cataloged the sample     4       xs:string       Cataloger       icmsperson
2f0f84ef-f554-4435-8701-2eefe78d65ee    icms.datedby    The person who dated this sample        4       xs:string       Dated by        icmsperson
dc142808-e5d3-4dc1-8457-c2e1f0badf5e    icms.culturalid Cultural affiliation of the material or the person associated with this sample  4       xs:string       Cultural ID     icms.culturalid
8a9911c4-a1b5-4f6e-99e5-0bbf4dc945d9    icms.histcultper        Historic/Cultural period this sample is from    4       xs:string       Historic cultural period        icms.histcultper
2897f42a-86a0-4478-bd71-0cdbf20c536d    icms.identifiedby       Enter the name of person, last name first, who identified the object    4       xs:string       Identified by   \N
\.
