--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: tbltruncate; Type: TABLE; Schema: public; Owner: lucasm; Tablespace: 
--

CREATE TABLE tbltruncate (
    truncateid integer NOT NULL,
    vmeasurementid uuid NOT NULL,
    startrelyear integer NOT NULL,
    endrelyear integer NOT NULL,
    justification text
);


ALTER TABLE public.tbltruncate OWNER TO lucasm;

--
-- Name: tbltruncate_truncateid_seq; Type: SEQUENCE; Schema: public; Owner: lucasm
--

CREATE SEQUENCE tbltruncate_truncateid_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tbltruncate_truncateid_seq OWNER TO lucasm;

--
-- Name: tbltruncate_truncateid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: lucasm
--

ALTER SEQUENCE tbltruncate_truncateid_seq OWNED BY tbltruncate.truncateid;


--
-- Name: truncateid; Type: DEFAULT; Schema: public; Owner: lucasm
--

ALTER TABLE tbltruncate ALTER COLUMN truncateid SET DEFAULT nextval('tbltruncate_truncateid_seq'::regclass);


--
-- Name: pkey_tbltruncate; Type: CONSTRAINT; Schema: public; Owner: lucasm; Tablespace: 
--

ALTER TABLE ONLY tbltruncate
    ADD CONSTRAINT pkey_tbltruncate PRIMARY KEY (truncateid);


--
-- Name: uniq_truncate_vmeasurement; Type: CONSTRAINT; Schema: public; Owner: lucasm; Tablespace: 
--

ALTER TABLE ONLY tbltruncate
    ADD CONSTRAINT uniq_truncate_vmeasurement UNIQUE (vmeasurementid);


--
-- Name: fkey_tbltruncate-tblvmeasurement; Type: FK CONSTRAINT; Schema: public; Owner: lucasm
--

ALTER TABLE ONLY tbltruncate
    ADD CONSTRAINT "fkey_tbltruncate-tblvmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

