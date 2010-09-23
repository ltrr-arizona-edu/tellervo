--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: dummy; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE dummy WITH TEMPLATE = template0 ENCODING = 'UTF8';


\connect dummy

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: tbldummy; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE tbldummy (
    dummy integer NOT NULL
);


--
-- Name: tbldummy_dummy_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE tbldummy_dummy_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: tbldummy_dummy_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE tbldummy_dummy_seq OWNED BY tbldummy.dummy;


--
-- Name: tbldummy_dummy_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('tbldummy_dummy_seq', 1, false);


--
-- Name: dummy; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE tbldummy ALTER COLUMN dummy SET DEFAULT nextval('tbldummy_dummy_seq'::regclass);


--
-- Data for Name: tbldummy; Type: TABLE DATA; Schema: public; Owner: -
--

COPY tbldummy (dummy) FROM stdin;
\.


--
-- Name: public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

