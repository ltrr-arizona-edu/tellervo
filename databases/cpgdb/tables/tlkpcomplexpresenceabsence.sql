--
-- Name: tlkpcomplexpresenceabsence; Type: TABLE; Schema: public; Owner: aps03pwb; Tablespace: 
--

CREATE TABLE tlkpcomplexpresenceabsence (
    complexpresenceabsenceid integer NOT NULL,
    complexpresenceabsence character varying NOT NULL
);


ALTER TABLE public.tlkpcomplexpresenceabsence OWNER TO aps03pwb;

--
-- Name: tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq; Type: SEQUENCE; Schema: public; Owner: aps03pwb
--

CREATE SEQUENCE tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq OWNER TO aps03pwb;

--
-- Name: tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: aps03pwb
--

ALTER SEQUENCE tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq OWNED BY tlkpcomplexpresenceabsence.complexpresenceabsenceid;


--
-- Name: complexpresenceabsenceid; Type: DEFAULT; Schema: public; Owner: aps03pwb
--

ALTER TABLE tlkpcomplexpresenceabsence ALTER COLUMN complexpresenceabsenceid SET DEFAULT nextval('tlkpcomplexpresenceabsence_complexpresenceabsenceid_seq'::regclass);


--
-- Name: pkey_complexpresenceabsence; Type: CONSTRAINT; Schema: public; Owner: aps03pwb; Tablespace: 
--

ALTER TABLE ONLY tlkpcomplexpresenceabsence
    ADD CONSTRAINT pkey_complexpresenceabsence PRIMARY KEY (complexpresenceabsenceid);


--
-- Data for Name: tlkpcomplexpresenceabsence; Type: TABLE DATA; Schema: public; Owner: aps03pwb
--

COPY tlkpcomplexpresenceabsence (complexpresenceabsenceid, complexpresenceabsence) FROM stdin;
3       absent
2       incomplete
4       not applicable
5       unknown
0       [Custom]
1       complete
\.


--
-- Name: tlkpcomplexpresenceabsence; Type: ACL; Schema: public; Owner: aps03pwb
--

REVOKE ALL ON TABLE tlkpcomplexpresenceabsence FROM PUBLIC;
REVOKE ALL ON TABLE tlkpcomplexpresenceabsence FROM aps03pwb;
GRANT ALL ON TABLE tlkpcomplexpresenceabsence TO aps03pwb;
GRANT ALL ON TABLE tlkpcomplexpresenceabsence TO "Webgroup";
