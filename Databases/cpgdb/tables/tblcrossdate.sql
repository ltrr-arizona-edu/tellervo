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
-- Name: tblcrossdate; Type: TABLE; Schema: public; Owner: aps03pwb; Tablespace: 
--

CREATE TABLE tblcrossdate (
    crossdateid integer NOT NULL,
    vmeasurementid uuid NOT NULL,
    mastervmeasurementid uuid NOT NULL,
    startyear integer NOT NULL,
    justification text,
    confidence integer DEFAULT 1 NOT NULL,
    CONSTRAINT "chk_confidence-max" CHECK ((confidence <= 5)),
    CONSTRAINT "chk_confidence-min" CHECK ((confidence >= 0))
);


ALTER TABLE public.tblcrossdate OWNER TO aps03pwb;

--
-- Name: tblcrossdate_crossdateid_seq; Type: SEQUENCE; Schema: public; Owner: aps03pwb
--

CREATE SEQUENCE tblcrossdate_crossdateid_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tblcrossdate_crossdateid_seq OWNER TO aps03pwb;

--
-- Name: tblcrossdate_crossdateid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: aps03pwb
--

ALTER SEQUENCE tblcrossdate_crossdateid_seq OWNED BY tblcrossdate.crossdateid;


--
-- Name: crossdateid; Type: DEFAULT; Schema: public; Owner: aps03pwb
--

ALTER TABLE tblcrossdate ALTER COLUMN crossdateid SET DEFAULT nextval('tblcrossdate_crossdateid_seq'::regclass);


--
-- Name: pkey_tblcrossdate; Type: CONSTRAINT; Schema: public; Owner: aps03pwb; Tablespace: 
--

ALTER TABLE ONLY tblcrossdate
    ADD CONSTRAINT pkey_tblcrossdate PRIMARY KEY (crossdateid);


--
-- Name: uniq_vmeasurement; Type: CONSTRAINT; Schema: public; Owner: aps03pwb; Tablespace: 
--

ALTER TABLE ONLY tblcrossdate
    ADD CONSTRAINT uniq_vmeasurement UNIQUE (vmeasurementid);


--
-- Name: fkey_tblcrossdate-tblvmeasurement; Type: FK CONSTRAINT; Schema: public; Owner: aps03pwb
--

ALTER TABLE ONLY tblcrossdate
    ADD CONSTRAINT "fkey_tblcrossdate-tblvmeasurement" FOREIGN KEY (vmeasurementid) REFERENCES tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fkey_tblcrossdate-tblvmeasurement_master; Type: FK CONSTRAINT; Schema: public; Owner: aps03pwb
--

ALTER TABLE ONLY tblcrossdate
    ADD CONSTRAINT "fkey_tblcrossdate-tblvmeasurement_master" FOREIGN KEY (mastervmeasurementid) REFERENCES tblvmeasurement(vmeasurementid) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: tblcrossdate; Type: ACL; Schema: public; Owner: aps03pwb
--

REVOKE ALL ON TABLE tblcrossdate FROM PUBLIC;
REVOKE ALL ON TABLE tblcrossdate FROM aps03pwb;
GRANT ALL ON TABLE tblcrossdate TO aps03pwb;
GRANT ALL ON TABLE tblcrossdate TO "Webgroup";


--
-- Name: tblcrossdate_crossdateid_seq; Type: ACL; Schema: public; Owner: aps03pwb
--

REVOKE ALL ON SEQUENCE tblcrossdate_crossdateid_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE tblcrossdate_crossdateid_seq FROM aps03pwb;
GRANT ALL ON SEQUENCE tblcrossdate_crossdateid_seq TO aps03pwb;
GRANT ALL ON SEQUENCE tblcrossdate_crossdateid_seq TO "Webgroup";


--
-- PostgreSQL database dump complete
--

