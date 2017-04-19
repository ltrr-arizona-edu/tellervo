
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

5625746e-357f-4486-bb28-0b751e1315c2	ltrr.barkyear	Year of the bark where measurable or can be determined with confidence	4	xs:int	Bark year	\N
454f7ae1-cbdf-43c9-bf64-92ebd7842632	ltrr.firstyear	First year that is dated/measured	4	xs:int	First year	\N
41aba235-a06c-45f0-a07d-8e2000a93a5f	ltrr.lastyear	Last year that was measured/dated	4	xs:int	Last year	\N
bb44223d-7f69-4f3e-b728-a878f263a825	swarch.innercode	Southwestern Dendroarchaeology code for inner date	4	xs:string	Inner ring code	\N
87e2e372-910c-499f-9c33-b125f0014d93	swarch.outercode	Southwestern Dendroarchaeology code for outer date	4	xs:string	Outer ring code	\N
ebeea485-76ea-456a-883e-1c2b082ff088	ltrr.pithyear	Year of the pith where measureable or can be determined with confidence	4	xs:int	Pith year \N
\.

