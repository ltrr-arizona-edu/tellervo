
--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.4
-- Dumped by pg_dump version 9.5.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Data for Name: tlkpuserdefinedfield; Type: TABLE DATA; Schema: public; Owner: tellervo
--


COPY tlkpuserdefinedfield (userdefinedfieldid, fieldname, description, attachedto, datatype, longfieldname, dictionarykey) FROM stdin;
027a186a-b312-4bc1-af2d-a4021a387d19	icms.accession	ICMS Accession code	4	xs:string	Accession code	\N
30fbebff-ccfb-4714-b53a-6f9b0a711e07	icms.statusdate	Fiscal year for which the status applies	4	xs:int	Status date	\N
1b973165-1a97-4115-aae7-43ee822b4548	icms.ctrlprop	Whether the sample is an item of value, firearm etc	4	xs:boolean	Control property	\N
6a246298-639a-4adc-9509-b3527cc694f0	icms.catalog	ICMS Catalog Code.  Used to uniquely identifer a record in ICMS	4	xs:string	Catalog code	\N
b4d65273-feba-472e-b2a2-6d3bed8c0b59	icms.itemcount	Number of parts the sample is in	4	xs:int	Item count	\N
15bc0104-446d-4c2a-ae46-2b9ae17c4516	icms.catalogdateoverride	Date the sample was cataloged.  If left blank the automated createdtimestamp field is used	4	xs:string	Catalog date override	\N
c887a8a5-070d-4acb-8eed-0649a55fecfa	icms.identdate	Date the sample was dated	4	xs:string	Dated date	\N
457c87fc-f176-4458-a6f0-1b7fe3732af1	icms.statesite	Site code given by the relevant state archeological inventory	4	xs:string	State site code	\N
46928774-299b-436b-891f-4b2055b0853f	icms.fieldsite	The site code given by the original collector if different to the Tellervo code	4	xs:string	Field site code	\N
d576088a-c18b-4623-8ffe-16101249178f	icms.fieldspecimen	Original code given to this sample by the collector	4	xs:string	Field specimen code	\N
775d7b76-b13b-4606-8d2a-153978355f53	icms.cataloger	Person would initially cataloged the sample	4	xs:string	Cataloger	icmsperson
2f0f84ef-f554-4435-8701-2eefe78d65ee	icms.datedby	The person who dated this sample	4	xs:string	Dated by	icmsperson
dc142808-e5d3-4dc1-8457-c2e1f0badf5e	icms.culturalid	Cultural affiliation of the material or the person associated with this sample	4	xs:string	Cultural ID	icms.culturalid
8a9911c4-a1b5-4f6e-99e5-0bbf4dc945d9	icms.histcultper	Historic/Cultural period this sample is from	4	xs:string	Historic cultural period	icms.histcultper
2897f42a-86a0-4478-bd71-0cdbf20c536d	icms.identifiedby	Enter the name of person, last name first, who identified the object	4	xs:string	Identified by	\N

\.



COPY tlkpuserdefinedterm (userdefinedtermid, term, dictionarykey) FROM stdin;
75246ff6-0da0-11e7-819b-67820f83d250	AKIMEL O'ODHAM (PIMA)	icms.culturalid
75246ff7-0da0-11e7-819b-7b7c1bbb420a	ALGONQUIN	icms.culturalid
75246ff8-0da0-11e7-819b-e71d89979702	AMERICAN	icms.culturalid
75246ff9-0da0-11e7-819b-b735da738532	ANASAZI	icms.culturalid
75246ffa-0da0-11e7-819b-8b864fa2fcd7	ANASAZI, CHACO	icms.culturalid
75246ffb-0da0-11e7-819b-ff0b03d08434	ANASAZI, CHUSKA	icms.culturalid
75246ffc-0da0-11e7-819b-1fa59fdc4183	ANASAZI, KAYENTA	icms.culturalid
75246ffd-0da0-11e7-819b-ab1ae0bf53d2	ANASAZI, LITTLE COLORADO	icms.culturalid
75246ffe-0da0-11e7-819b-578bfa522296	ANASAZI, MESA VERDE	icms.culturalid
75246fff-0da0-11e7-819b-8f1f64e33720	ANASAZI, MIXED	icms.culturalid
75247000-0da0-11e7-819b-dbfa9b7cc7f4	ANASAZI, RIO GRANDE	icms.culturalid
75247001-0da0-11e7-819b-27533becb5c8	ANASAZI, SAN JUAN	icms.culturalid
75247002-0da0-11e7-819b-77f3869c9836	ANASAZI, VIRGIN	icms.culturalid
75247003-0da0-11e7-819b-c3080717ee6a	ANGLO	icms.culturalid
75247004-0da0-11e7-819b-8b2bb047257a	ANIMAS	icms.culturalid
75247005-0da0-11e7-819b-47625fd82e28	ANIMAS, CLOVERDALE	icms.culturalid
75247006-0da0-11e7-819b-a30d148b2a31	APACHE	icms.culturalid
75247007-0da0-11e7-819b-db952d2e6469	APACHE, CHIRICAHUA	icms.culturalid
75247008-0da0-11e7-819b-0f9dddc62bd1	APACHE, JICARILLA	icms.culturalid
75247009-0da0-11e7-819b-0fd38f7adfd2	APACHE, MESCALERO	icms.culturalid
7524700a-0da0-11e7-819b-4784020618c6	APACHE, SAN CARLOS	icms.culturalid
7524700b-0da0-11e7-819b-03ac33b8d5c6	APACHE, TONTO	icms.culturalid
7524700c-0da0-11e7-819b-d76f22f79644	APACHE, WESTERN	icms.culturalid
7524700d-0da0-11e7-819b-a3d23a74e9fd	ARCHAIC	icms.culturalid
7524700e-0da0-11e7-819b-9fe50cbc0c2e	ARCHAIC, DESERT	icms.culturalid
7524700f-0da0-11e7-819b-033d94d45868	ARCHAIC, JAY	icms.culturalid
75247010-0da0-11e7-819b-d3a7239d5c09	ARCHAIC, LATE	icms.culturalid
75247011-0da0-11e7-819b-f795ed2c3f87	ARCHAIC, LITTLE COLORADO	icms.culturalid
75247012-0da0-11e7-819b-77b36a895041	ARCHAIC, PINTO	icms.culturalid
75247013-0da0-11e7-819b-db9139ab82b1	ARCHAIC, SAN PEDRO	icms.culturalid
75247014-0da0-11e7-819b-0f49a250b193	ARCHAIC, UNCOMPAHGRE	icms.culturalid
75247015-0da0-11e7-819b-9beecf2c590c	ARGENTINIAN	icms.culturalid
75247016-0da0-11e7-819b-8784ef06e842	ASIAN	icms.culturalid
75247017-0da0-11e7-819b-8b5c098dd8d9	AUSTRIAN	icms.culturalid
75247018-0da0-11e7-819b-4bdc5fbbb91a	BASKETMAKER	icms.culturalid
75247019-0da0-11e7-819b-8f145c32310b	BAVARIAN	icms.culturalid
7524701a-0da0-11e7-819b-8b106491b7f8	BELGIAN	icms.culturalid
7524701b-0da0-11e7-819b-6f785887048f	BRITISH	icms.culturalid
7524701c-0da0-11e7-819b-43e27ce671ab	CASAS GRANDES	icms.culturalid
7524701d-0da0-11e7-819b-c33294551213	CHINESE	icms.culturalid
7524701e-0da0-11e7-819b-f7603ed58d81	CHIRIQUI	icms.culturalid
7524701f-0da0-11e7-819b-7f4ab8bf6e23	CHUMASH	icms.culturalid
75247020-0da0-11e7-819b-ef73a6e14007	COCHISE	icms.culturalid
75247021-0da0-11e7-819b-5fe63760f0fd	COCHISE, SAN PEDRO	icms.culturalid
75247022-0da0-11e7-819b-97fb2627798e	COCHITI	icms.culturalid
75247023-0da0-11e7-819b-8ffe01137625	COHONINA	icms.culturalid
75247024-0da0-11e7-819b-53ad796ef0e8	CROW	icms.culturalid
75247025-0da0-11e7-819b-d301d0c9998a	CZECHOSLOVAKIAN	icms.culturalid
75247026-0da0-11e7-819b-77b402f54576	DAKOTA	icms.culturalid
75247027-0da0-11e7-819b-2f5959dbd0a3	DENEID	icms.culturalid
75247028-0da0-11e7-819b-d3e45914e3da	DUTCH	icms.culturalid
75247029-0da0-11e7-819b-974c0fabe188	ENGLISH	icms.culturalid
7524702a-0da0-11e7-819b-23feff14167a	EUROPEAN	icms.culturalid
7524702b-0da0-11e7-819b-db75ac2e277c	FREMONT	icms.culturalid
7524702c-0da0-11e7-819b-0f6b35dee811	FRENCH	icms.culturalid
7524702d-0da0-11e7-819b-976644a63247	FRONTIER MILITARY	icms.culturalid
7524702e-0da0-11e7-819b-9708acef7cd3	GERMAN	icms.culturalid
7524702f-0da0-11e7-819b-efae848c2891	HIA C-ED O'ODHAM (SAND PAPAGO)	icms.culturalid
75247030-0da0-11e7-819b-0ff21448ba01	HISPANIC	icms.culturalid
75247031-0da0-11e7-819b-7f572fb66dc9	HISPANIC, RIO GRANDE	icms.culturalid
75247032-0da0-11e7-819b-af456c0881cb	HISTORIC PUEBLO	icms.culturalid
75247033-0da0-11e7-819b-ab8926fc9b7f	HOHOKAM	icms.culturalid
75247034-0da0-11e7-819b-135728e5398a	HOHOKAM, DESERT	icms.culturalid
75247035-0da0-11e7-819b-6b2d9aa558ce	HOHOKAM, RIVER	icms.culturalid
75247036-0da0-11e7-819b-1b3c4ff4f648	HOHOKAM, TUCSON BASIN	icms.culturalid
75247037-0da0-11e7-819b-33befaecb5ad	HOPI	icms.culturalid
75247038-0da0-11e7-819b-e3085f464e1f	HOPI, PROTOHISTORIC	icms.culturalid
75247039-0da0-11e7-819b-6f7124aea41e	HUALAPAI	icms.culturalid
7524703a-0da0-11e7-819b-d774ce3bedad	IRISH	icms.culturalid
7524703b-0da0-11e7-819b-7399b11c9930	ISLETA	icms.culturalid
7524703c-0da0-11e7-819b-fbc3d44703d0	ITALIAN	icms.culturalid
7524703d-0da0-11e7-819b-57e82f838a72	JAPANESE	icms.culturalid
7524703e-0da0-11e7-819b-efc2e60908a7	JUMANO	icms.culturalid
7524703f-0da0-11e7-819b-072b83dff165	LAKOTA	icms.culturalid
75247040-0da0-11e7-819b-bfab597cb302	LAQUISH	icms.culturalid
75247041-0da0-11e7-819b-3bd8544c7b59	MARICOPA	icms.culturalid
75247042-0da0-11e7-819b-e354e20667b7	MAYAN	icms.culturalid
75247043-0da0-11e7-819b-878830e189f4	MEXICAN	icms.culturalid
75247044-0da0-11e7-819b-1b4b419a1fdb	MIMBRES	icms.culturalid
75247045-0da0-11e7-819b-834cc84e8302	MIXTEC	icms.culturalid
75247046-0da0-11e7-819b-8bb451be9d98	MOGOLLON	icms.culturalid
75247047-0da0-11e7-819b-570eaf98987f	MOGOLLON, FORESTDALE	icms.culturalid
75247048-0da0-11e7-819b-d721d33932f6	MOGOLLON, JORNADA	icms.culturalid
75247049-0da0-11e7-819b-93cda3340170	MOGOLLON, LITTLE COLORADO	icms.culturalid
7524704a-0da0-11e7-819b-539779861c66	MOGOLLON, MIMBRES	icms.culturalid
7524704b-0da0-11e7-819b-9b7ba46daa4b	MOGOLLON, MIXED	icms.culturalid
7524704c-0da0-11e7-819b-9b846f32a5ba	MOGOLLON, NORTHERN	icms.culturalid
7524704d-0da0-11e7-819b-27877baeb595	MOGOLLON, PINE LAWN VALLEY	icms.culturalid
7524704e-0da0-11e7-819b-9397ebca5fda	MOGOLLON, RESERVE	icms.culturalid
7524704f-0da0-11e7-819b-5b9e5adbf432	MOGOLLON, SAN SIMON	icms.culturalid
75247050-0da0-11e7-819b-6390fea809a9	MOGOLLON, TULAROSA	icms.culturalid
75247051-0da0-11e7-819b-17a06085a165	MOGOLLON, WHITE MOUNTAIN	icms.culturalid
75247052-0da0-11e7-819b-8bad2986ec2f	MOGOLLON, WHITE MOUNTAIN (PROTO-ZUNI)	icms.culturalid
75247053-0da0-11e7-819b-b3b2de1660f6	MOHAVE	icms.culturalid
75247054-0da0-11e7-819b-076b14bb54d6	NAVAJO	icms.culturalid
75247055-0da0-11e7-819b-97b55a8aa9ca	NUMIC	icms.culturalid
75247056-0da0-11e7-819b-c7f3fa3e3b04	O'ODHAM (PAPAGO)	icms.culturalid
75247057-0da0-11e7-819b-437bc57a848e	O'ODHAM (PIMAN)	icms.culturalid
75247058-0da0-11e7-819b-2384570c346c	ORIENTAL	icms.culturalid
75247059-0da0-11e7-819b-ab843d975e10	ORIENTAL (CHINESE?)	icms.culturalid
7524705a-0da0-11e7-819b-bb97e22cef8f	PAIUTE	icms.culturalid
7524705b-0da0-11e7-819b-174227771dc8	PAIUTE, SOUTHERN	icms.culturalid
7524705c-0da0-11e7-819b-9b674da6a20d	PALEOINDIAN	icms.culturalid
7524705d-0da0-11e7-819b-8b783d1f682d	PALEOINDIAN, FOLSOM	icms.culturalid
7524705e-0da0-11e7-819b-8f7dd7fed615	PALEOINDIAN, LATE	icms.culturalid
7524705f-0da0-11e7-819b-a74cf9774080	PALEOINDIAN, PLANO	icms.culturalid
75247060-0da0-11e7-819b-47f717b48b4c	PALEOINDIAN, SAN DIEGUITO	icms.culturalid
75247061-0da0-11e7-819b-77b4d1043a5c	PALEOINDIAN, SOUTHERN CODY	icms.culturalid
75247062-0da0-11e7-819b-5350a418a482	PAROWAN	icms.culturalid
75247063-0da0-11e7-819b-9397d58c75c6	PATAYAN	icms.culturalid
75247064-0da0-11e7-819b-072edffaffc0	PATAYAN, CERBAT	icms.culturalid
75247065-0da0-11e7-819b-13f5a1d4f76e	PATAYAN, PRESCOTT	icms.culturalid
75247066-0da0-11e7-819b-471f2d506a24	PHILIPPINE	icms.culturalid
75247067-0da0-11e7-819b-6f24c77fbd25	PIMA, UPPER	icms.culturalid
75247068-0da0-11e7-819b-c3d060bf32b6	PIMAN	icms.culturalid
75247069-0da0-11e7-819b-9310c39cb0a7	PLAINS	icms.culturalid
7524706a-0da0-11e7-819b-53e1222c13a6	PLAINS, SOUTHERN	icms.culturalid
7524706b-0da0-11e7-819b-038d34e496a2	POMO	icms.culturalid
7524706c-0da0-11e7-819b-070d0eb136a7	PRE-UTE	icms.culturalid
7524706d-0da0-11e7-819b-8f9cba62a7b5	PROTO-ZUNI	icms.culturalid
7524706e-0da0-11e7-819b-fb9b4a6a7731	PUEBLO	icms.culturalid
7524706f-0da0-11e7-819b-57fe4dd946b5	PUEBLO, RIO GRANDE	icms.culturalid
75247070-0da0-11e7-819b-8b35524f8d7e	PUEBLO, WESTERN	icms.culturalid
75247071-0da0-11e7-819b-67c28e2b7623	SALADO	icms.culturalid
75247072-0da0-11e7-819b-2b0eece6f732	SALADO, TONTO BASIN	icms.culturalid
75247073-0da0-11e7-819b-2392b885e17c	SAN ILDEFONSO	icms.culturalid
75247074-0da0-11e7-819b-6bda93d401e6	SAN JUAN	icms.culturalid
75247075-0da0-11e7-819b-1338519918a4	SANTA ANA	icms.culturalid
75247076-0da0-11e7-819b-7f05574b1dc1	SANTO DOMINGO	icms.culturalid
75247077-0da0-11e7-819b-ef53cdaca367	SCOTTISH	icms.culturalid
75247078-0da0-11e7-819b-1fb12181997b	SERI	icms.culturalid
75247079-0da0-11e7-819b-13ce17dce05f	SHASTA	icms.culturalid
7524707a-0da0-11e7-819b-1f5f7544a74c	SHOSHONE	icms.culturalid
7524707b-0da0-11e7-819b-cf525064bdf1	SHOSHONE, DEATH VALLEY	icms.culturalid
7524707c-0da0-11e7-819b-ebdbeade3b99	SHOSHONE, WESTERN	icms.culturalid
7524707d-0da0-11e7-819b-371dd08bc125	SINAGUA	icms.culturalid
7524707e-0da0-11e7-819b-9b3758d7a036	SINAGUA, MIXED	icms.culturalid
7524707f-0da0-11e7-819b-7f44abae2626	SINAGUA, NORTHERN	icms.culturalid
75247080-0da0-11e7-819b-d7a91b174660	SINAGUA, SOUTHERN	icms.culturalid
75247081-0da0-11e7-819b-67543794fb14	SOBAIPURI	icms.culturalid
75247082-0da0-11e7-819b-43c0d5e8d15c	SPANISH	icms.culturalid
75247083-0da0-11e7-819b-fb09b3e23f68	SPANISH COLONIAL	icms.culturalid
75247084-0da0-11e7-819b-f74818fe8b89	SPANISH-AMERICAN?	icms.culturalid
75247085-0da0-11e7-819b-9726a1eb94ac	TANO	icms.culturalid
75247086-0da0-11e7-819b-2f060bf2b0cf	TAOS	icms.culturalid
75247087-0da0-11e7-819b-f35d9261e351	TESUQUE	icms.culturalid
75247088-0da0-11e7-819b-bbbb0014aa36	TEWA	icms.culturalid
75247089-0da0-11e7-819b-273d9dac776c	TEWA, NORTHERN	icms.culturalid
7524708a-0da0-11e7-819b-a7b24468e533	TIWA, NORTHERN	icms.culturalid
7524708b-0da0-11e7-819b-ff6ce2c123a8	TOHONO O'ODHAM (PAPAGO)	icms.culturalid
7524708c-0da0-11e7-819b-6b6d7872679b	TOMPIRO	icms.culturalid
7524708d-0da0-11e7-819b-47a726093724	TOWA	icms.culturalid
7524708e-0da0-11e7-819b-d79ff09eed1f	TRINCHERAS	icms.culturalid
7524708f-0da0-11e7-819b-8bab0834022c	UNIDENTIFIED	icms.culturalid
75247090-0da0-11e7-819b-9b73faed1a9d	UTE	icms.culturalid
75247091-0da0-11e7-819b-af9a6f44b0b9	UTE-PAIUTE	icms.culturalid
75247092-0da0-11e7-819b-0f8eb281c6e4	WESTERN PUEBLO	icms.culturalid
75247093-0da0-11e7-819b-cb8ba7c0c86d	WINONA	icms.culturalid
75247094-0da0-11e7-819b-0f230652e78e	YAVAPAI	icms.culturalid
75247095-0da0-11e7-819b-3f643579cf66	YAVAPAI, NORTHEASTERN	icms.culturalid
75247096-0da0-11e7-819b-e3366fa0e1fb	YUMAN	icms.culturalid
75247097-0da0-11e7-819b-777f4fb53f78	ZIA	icms.culturalid
75247098-0da0-11e7-819b-9be436f96126	ZUNI	icms.culturalid
d80454e2-0da0-11e7-819b-8f4edb1b00ff	17TH C, EARLY	icms.histcultper
d80454e3-0da0-11e7-819b-5f8b27ebfff6	17TH C, LATE	icms.histcultper
d80454e4-0da0-11e7-819b-2f85e671c058	17TH C, MIDDLE	icms.histcultper
d80454e5-0da0-11e7-819b-b3624733191d	19TH C, LATE	icms.histcultper
d80454e6-0da0-11e7-819b-cb1bd950a7ab	19TH C, MIDDLE	icms.histcultper
d80454e7-0da0-11e7-819b-5f1d01aca7af	19TH C, SECOND HALF	icms.histcultper
d80454e8-0da0-11e7-819b-8bfb4fe52326	20TH C	icms.histcultper
d80454e9-0da0-11e7-819b-6f7add305e33	20TH C, EARLY	icms.histcultper
d80454ea-0da0-11e7-819b-e7b07ec5595b	ACKMEN	icms.histcultper
d80454eb-0da0-11e7-819b-efed32c6abd3	AMARGOSA	icms.histcultper
d80454ec-0da0-11e7-819b-d3f96c88dc7c	AMARGOSA I	icms.histcultper
d80454ed-0da0-11e7-819b-1f5e44b6e36b	AMARGOSA II	icms.histcultper
d80454ee-0da0-11e7-819b-0bdf17e6225c	AMERICAN	icms.histcultper
d80454ef-0da0-11e7-819b-a7ff486b3b61	ANGELL	icms.histcultper
d80454f0-0da0-11e7-819b-831d39c6087e	ANIMAS	icms.histcultper
d80454f1-0da0-11e7-819b-3f4d93185aca	APACHE WARS	icms.histcultper
d80454f2-0da0-11e7-819b-fb78dfc401b8	APISHAPA PHASE	icms.histcultper
d80454f3-0da0-11e7-819b-931412f9b0f1	ARCHAIC	icms.histcultper
d80454f4-0da0-11e7-819b-3b55076a9e4f	ARCHAIC, EARLY	icms.histcultper
d80454f5-0da0-11e7-819b-5f8c7c070304	ARCHAIC, LATE	icms.histcultper
d80454f6-0da0-11e7-819b-375dd989ab52	ARCHAIC, MIDDLE	icms.histcultper
d80454f7-0da0-11e7-819b-2bc6b609ce1d	BAKER	icms.histcultper
d80454f8-0da0-11e7-819b-cf4dd818259a	BASKETMAKER	icms.histcultper
d80454f9-0da0-11e7-819b-4b7480cb4055	BASKETMAKER II	icms.histcultper
d80454fa-0da0-11e7-819b-a75f3465bebd	BASKETMAKER III	icms.histcultper
d80454fb-0da0-11e7-819b-73ad37a694b9	BASKETMAKER II-III	icms.histcultper
d80454fc-0da0-11e7-819b-97c1d9790127	BLACK ROCK	icms.histcultper
d80454fd-0da0-11e7-819b-d708601a1268	BONITO	icms.histcultper
d80454fe-0da0-11e7-819b-679753e5781e	BONNEVILLE	icms.histcultper
d80454ff-0da0-11e7-819b-afae6345643e	CAMP VERDE	icms.histcultper
d8045500-0da0-11e7-819b-5f24baf93663	CAMP VERDE, EARLY	icms.histcultper
d8045501-0da0-11e7-819b-13605b47d9b0	CAMP VERDE, LATE	icms.histcultper
d8045502-0da0-11e7-819b-0b65de03bb4c	CANYON DEL ORO	icms.histcultper
d8045503-0da0-11e7-819b-6326dcf1ab44	CERAMIC, EARLY	icms.histcultper
d8045504-0da0-11e7-819b-e3882323fef0	CERROS	icms.histcultper
d8045505-0da0-11e7-819b-0755f1015dc3	CHIRICAHUA	icms.histcultper
d8045506-0da0-11e7-819b-cf0b431198a7	CINDER PARK	icms.histcultper
d8045507-0da0-11e7-819b-73cd13854748	CIVANO	icms.histcultper
d8045508-0da0-11e7-819b-ab73e020c327	CIVIL WAR	icms.histcultper
d8045509-0da0-11e7-819b-3756798c6de3	CLASSIC	icms.histcultper
d804550a-0da0-11e7-819b-df8d4de3b852	CLASSIC, EARLY	icms.histcultper
d804550b-0da0-11e7-819b-c3cc70286779	CLASSIC, LATE	icms.histcultper
d804550c-0da0-11e7-819b-3716ab067bc6	CLASSIC, MIDDLE	icms.histcultper
d804550d-0da0-11e7-819b-5b8bf20a47b8	CLOVERDALE	icms.histcultper
d804550e-0da0-11e7-819b-ffa976a798cd	CLOVIS	icms.histcultper
d804550f-0da0-11e7-819b-836ffdf32e2b	COLONIAL	icms.histcultper
d8045510-0da0-11e7-819b-3bdb584d7871	COLONIAL, LATE	icms.histcultper
d8045511-0da0-11e7-819b-7764bca0849c	COWHORN	icms.histcultper
d8045512-0da0-11e7-819b-6bbced905c2a	DE CHELLY	icms.histcultper
d8045513-0da0-11e7-819b-877b909e3850	DEATH VALLEY I	icms.histcultper
d8045514-0da0-11e7-819b-1f750f152031	DEATH VALLEY II	icms.histcultper
d8045515-0da0-11e7-819b-e75e95c891af	DEATH VALLEY II, LATE	icms.histcultper
d8045516-0da0-11e7-819b-0f32aa73f87e	DEATH VALLEY III	icms.histcultper
d8045517-0da0-11e7-819b-27689ca92475	DEATH VALLEY IV	icms.histcultper
d8045518-0da0-11e7-819b-fbe5a62c49ba	DEATH VALLEY V	icms.histcultper
d8045519-0da0-11e7-819b-97c55c885ec6	DEL MUERTO	icms.histcultper
d804551a-0da0-11e7-819b-3f5fc8d1361c	DEPRESSION ERA	icms.histcultper
d804551b-0da0-11e7-819b-23bc457957a5	EARLY HISTORIC	icms.histcultper
d804551c-0da0-11e7-819b-e389d7ef97ec	EL PASO	icms.histcultper
d804551d-0da0-11e7-819b-132ae4a754c8	EL TOVAR	icms.histcultper
d804551e-0da0-11e7-819b-97443ea811ca	EL TOVAR, POST	icms.histcultper
d804551f-0da0-11e7-819b-ababb730a0da	EL TOVAR, PRE	icms.histcultper
d8045520-0da0-11e7-819b-53061fef3c1a	ELDEN	icms.histcultper
d8045521-0da0-11e7-819b-9f9cbfa36f4a	EN MEDIO	icms.histcultper
d8045522-0da0-11e7-819b-4be42838faa9	ENCINAS	icms.histcultper
d8045523-0da0-11e7-819b-574230a985ed	ESTRELLA	icms.histcultper
d8045524-0da0-11e7-819b-6f72d542a870	FOLSOM	icms.histcultper
d8045525-0da0-11e7-819b-376e235462bb	FORMATIVE	icms.histcultper
d8045526-0da0-11e7-819b-5f2e4f6b7f81	GALIURO	icms.histcultper
d8045527-0da0-11e7-819b-7bf3f0f5b186	GILA	icms.histcultper
d8045528-0da0-11e7-819b-7fb6b29d82b1	GILA BUTTE	icms.histcultper
d8045529-0da0-11e7-819b-47d0085525f2	GOBERNADOR, EARLY	icms.histcultper
d804552a-0da0-11e7-819b-a3efb81cf66a	HARDT	icms.histcultper
d804552b-0da0-11e7-819b-576a20ec04ea	HISTORIC	icms.histcultper
d804552c-0da0-11e7-819b-ebcca24ec0cb	HISTORIC, EARLY	icms.histcultper
d804552d-0da0-11e7-819b-eb7a7ede960a	HOMESTEADING, INDIAN WARS	icms.histcultper
d804552e-0da0-11e7-819b-23d0d453be86	HOMESTEADING, RANCHING	icms.histcultper
d804552f-0da0-11e7-819b-6b71a510d81d	HONANKI	icms.histcultper
d8045530-0da0-11e7-819b-0760bf6fd7ca	HOSTA BUTTE	icms.histcultper
d8045531-0da0-11e7-819b-abbcd2bbf2f4	INDIAN WARS	icms.histcultper
d8045532-0da0-11e7-819b-abb143f8b1ce	KLONDIKE	icms.histcultper
d8045533-0da0-11e7-819b-9376de792625	LA PLATA	icms.histcultper
d8045534-0da0-11e7-819b-1f6cfc51228c	LATE PREHISTORIC	icms.histcultper
d8045535-0da0-11e7-819b-239cf2002eeb	LLANO	icms.histcultper
d8045536-0da0-11e7-819b-9f38a3d42999	MANCOS	icms.histcultper
d8045537-0da0-11e7-819b-0b34c1eb7052	MANGAS	icms.histcultper
d8045538-0da0-11e7-819b-3f0391591646	MARANA	icms.histcultper
d8045539-0da0-11e7-819b-cfc64339cb2c	MCELMO	icms.histcultper
d804553a-0da0-11e7-819b-ebec0edbfc89	MEDIO	icms.histcultper
d804553b-0da0-11e7-819b-83ba904ce8fe	MESA VERDE	icms.histcultper
d804553c-0da0-11e7-819b-e799ef8c813d	MIAMI	icms.histcultper
d804553d-0da0-11e7-819b-b3289b2f3ffe	MISSION	icms.histcultper
d804553e-0da0-11e7-819b-9b6a18df3415	MISSION, EARLY	icms.histcultper
d804553f-0da0-11e7-819b-13d45b22f3db	MISSION, LATE	icms.histcultper
d8045540-0da0-11e7-819b-d7c1c0ca92df	MODERN	icms.histcultper
d8045541-0da0-11e7-819b-6f1020fbb1ea	NAVAJO	icms.histcultper
d8045542-0da0-11e7-819b-d755d8f1b8e1	NAVAJO, EARLY	icms.histcultper
d8045543-0da0-11e7-819b-37d00796ca74	NAVAJO, LATE	icms.histcultper
d8045544-0da0-11e7-819b-6f84488eb420	PADRE	icms.histcultper
d8045545-0da0-11e7-819b-a359f9cbb188	PATAYAN I	icms.histcultper
d8045546-0da0-11e7-819b-7360813ac551	PATAYAN II	icms.histcultper
d8045547-0da0-11e7-819b-7f217c3a01fc	PATAYAN III	icms.histcultper
d8045548-0da0-11e7-819b-3f15afb7ea9b	PATAYAN III, LATE	icms.histcultper
d8045549-0da0-11e7-819b-67bbda94df4a	PIEDRA	icms.histcultper
d804554a-0da0-11e7-819b-4b7a8aa1046f	PIEDRA, LATE	icms.histcultper
d804554b-0da0-11e7-819b-ef71fa2f3cd4	PINTO	icms.histcultper
d804554c-0da0-11e7-819b-7b5bc6a8aa16	PIONEER	icms.histcultper
d804554d-0da0-11e7-819b-37ce7b998021	PIONEER, LATE	icms.histcultper
d804554e-0da0-11e7-819b-1ffb3e67e0f4	PRECLASSIC	icms.histcultper
d804554f-0da0-11e7-819b-63b1ea82572f	PROTOHISTORIC	icms.histcultper
d8045550-0da0-11e7-819b-572bb9181593	PUEBLO	icms.histcultper
d8045551-0da0-11e7-819b-7f77114ff746	PUEBLO I	icms.histcultper
d8045552-0da0-11e7-819b-9b221b95581f	PUEBLO I, EARLY	icms.histcultper
d8045553-0da0-11e7-819b-9f4e02ca968e	PUEBLO I, LATE	icms.histcultper
d8045554-0da0-11e7-819b-5b10e7050405	PUEBLO I, MIDDLE	icms.histcultper
d8045555-0da0-11e7-819b-97dd5d3b1ff2	PUEBLO II	icms.histcultper
d8045556-0da0-11e7-819b-5fd627e1e4b0	PUEBLO II, EARLY	icms.histcultper
d8045557-0da0-11e7-819b-c7a418594007	PUEBLO II, LATE	icms.histcultper
d8045558-0da0-11e7-819b-0b5d3ccae294	PUEBLO II, MIDDLE	icms.histcultper
d8045559-0da0-11e7-819b-6ff0d14eeae4	PUEBLO III	icms.histcultper
d804555a-0da0-11e7-819b-db932ac304fe	PUEBLO III, EARLY	icms.histcultper
d804555b-0da0-11e7-819b-a76b645dc5ca	PUEBLO III, LATE	icms.histcultper
d804555c-0da0-11e7-819b-9fc8844e93f2	PUEBLO III, MIDDLE	icms.histcultper
d804555d-0da0-11e7-819b-435aefc47a4b	PUEBLO IV	icms.histcultper
d804555e-0da0-11e7-819b-fb82f399bcd4	PUEBLO V	icms.histcultper
d804555f-0da0-11e7-819b-530b39a52c1e	RESERVE	icms.histcultper
d8045560-0da0-11e7-819b-ff5a3deb1a2d	RILLITO	icms.histcultper
d8045561-0da0-11e7-819b-0722e41741bd	RINCON	icms.histcultper
d8045562-0da0-11e7-819b-13eae83dd950	RINCON, EARLY	icms.histcultper
d8045563-0da0-11e7-819b-97c12f5a4a3c	RINCON, LATE	icms.histcultper
d8045564-0da0-11e7-819b-b7050df8397a	RIO DE FLAG	icms.histcultper
d8045565-0da0-11e7-819b-335a80fcbc55	ROOSEVELT	icms.histcultper
d8045566-0da0-11e7-819b-eba36b85cc82	ROSA	icms.histcultper
d8045567-0da0-11e7-819b-973b54c705b1	RUSSIAN	icms.histcultper
d8045568-0da0-11e7-819b-1fab2241ea94	SACATON	icms.histcultper
d8045569-0da0-11e7-819b-efce567f0719	SACATON, EARLY	icms.histcultper
d804556a-0da0-11e7-819b-8f7ad32026e6	SACATON, LATE	icms.histcultper
d804556b-0da0-11e7-819b-672d4be3c174	SAN DIEGUITO	icms.histcultper
d804556c-0da0-11e7-819b-6f76842e0fd4	SAN DIEGUITO I	icms.histcultper
d804556d-0da0-11e7-819b-734a3f5250aa	SAN DIEGUITO II	icms.histcultper
d804556e-0da0-11e7-819b-676a3f233cd2	SAN FRANCISCO	icms.histcultper
d804556f-0da0-11e7-819b-37457204b625	SAN JOSE	icms.histcultper
d8045570-0da0-11e7-819b-4f46761e1a65	SAN PEDRO	icms.histcultper
d8045571-0da0-11e7-819b-830448f972dd	SANTA CRUZ	icms.histcultper
d8045572-0da0-11e7-819b-9b4c39c094bc	SANTA CRUZ, LATE	icms.histcultper
d8045573-0da0-11e7-819b-0f22ac8286c5	SANTAN	icms.histcultper
d8045574-0da0-11e7-819b-b7fdaacdf8af	SEDENTARY	icms.histcultper
d8045575-0da0-11e7-819b-63e0de80f2c1	SEDENTARY, EARLY	icms.histcultper
d8045576-0da0-11e7-819b-9b7f014f5e28	SEDENTARY, LATE	icms.histcultper
d8045577-0da0-11e7-819b-ab5799baa340	SELLS	icms.histcultper
d8045578-0da0-11e7-819b-13f665959b30	SELLS, EARLY	icms.histcultper
d8045579-0da0-11e7-819b-97801bc33c57	SNAKETOWN	icms.histcultper
d804557a-0da0-11e7-819b-4b79a4d11b85	SOHO	icms.histcultper
d804557b-0da0-11e7-819b-f365e5d2f7f0	SQUAW PEAK	icms.histcultper
d804557c-0da0-11e7-819b-9bec6d329fc0	SUNSET	icms.histcultper
d804557d-0da0-11e7-819b-536dea1d8486	SWEETWATER	icms.histcultper
d804557e-0da0-11e7-819b-53065738c375	TANQUE VERDE	icms.histcultper
d804557f-0da0-11e7-819b-af41109a1e10	TANQUE VERDE, EARLY	icms.histcultper
d8045580-0da0-11e7-819b-475819b1b1df	TRADING POST/TOURIST	icms.histcultper
d8045581-0da0-11e7-819b-6fb08eaa4368	TUCSON	icms.histcultper
d8045582-0da0-11e7-819b-7bcb366bd4be	TULAROSA	icms.histcultper
d8045583-0da0-11e7-819b-9386992b320e	TURKEY HILL	icms.histcultper
d8045584-0da0-11e7-819b-abe29fc8b155	TUZIGOOT	icms.histcultper
d8045585-0da0-11e7-819b-4bb8cee09bcf	VAHKI	icms.histcultper
d8045586-0da0-11e7-819b-c75b41ccb87b	VAMORI	icms.histcultper
d8045587-0da0-11e7-819b-eb839613815f	VENTANA	icms.histcultper
d8045588-0da0-11e7-819b-2733f31ad002	WENDOVER	icms.histcultper
d8045589-0da0-11e7-819b-c39d1766bdbc	WORLD WAR I	icms.histcultper
d804558a-0da0-11e7-819b-238f8ff18cb5	WORLD WAR II	icms.histcultper
d804558b-0da0-11e7-819b-9ba860909138	WORLD WAR II INTERNMENT	icms.histcultper
d804558c-0da0-11e7-819b-671ee8e28ca6	YUMAN II	icms.histcultper
d804558d-0da0-11e7-819b-2b35e9ae2f8f	YUMAN III	icms.histcultper
\.


