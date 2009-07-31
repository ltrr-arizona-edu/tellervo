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
-- Name: tblredate; Type: TABLE; Schema: public; Owner: lucasm; Tablespace: 
--

CREATE TABLE tblredate (
    redateid integer NOT NULL,
    vmeasurementid uuid NOT NULL,
    startyear integer NOT NULL,
    redatingtypeid integer,
    justification text
);


ALTER TABLE public.tblredate OWNER TO lucasm;

--
-- Name: tblredate_redateid_seq; Type: SEQUENCE; Schema: public; Owner: lucasm
--

CREATE SEQUENCE tblredate_redateid_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tblredate_redateid_seq OWNER TO lucasm;

--
-- Name: tblredate_redateid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: lucasm
--

ALTER SEQUENCE tblredate_redateid_seq OWNED BY tblredate.redateid;


--
-- Name: redateid; Type: DEFAULT; Schema: public; Owner: lucasm
--

ALTER TABLE tblredate ALTER COLUMN redateid SET DEFAULT nextval('tblredate_redateid_seq'::regclass);


--
-- Name: pkey_tblredate; Type: CONSTRAINT; Schema: public; Owner: lucasm; Tablespace: 
--

ALTER TABLE ONLY tblredate
    ADD CONSTRAINT pkey_tblredate PRIMARY KEY (redateid);


--
-- Name: uniq_redate_vmeasurement; Type: CONSTRAINT; Schema: public; Owner: lucasm; Tablespace: 
--

ALTER TABLE ONLY tblredate
    ADD CONSTRAINT uniq_redate_vmeasurement UNIQUE (vmeasurementid);


--
-- Name: fkey_tblredate-tblvmeasurement; Type: FK CONSTRAINT; Schema: public; Owner: lucasm
--

ALTER TABLE ONLY tblredate
    ADD CONSTRAINT "fkey_tblredate-tblvmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fkey_tblredate-tlkpdatingtype; Type: FK CONSTRAINT; Schema: public; Owner: lucasm
--

ALTER TABLE ONLY tblredate
    ADD CONSTRAINT "fkey_tblredate-tlkpdatingtype" FOREIGN KEY (redatingtypeid) REFERENCES tlkpdatingtype(datingtypeid) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- PostgreSQL database dump complete
--

