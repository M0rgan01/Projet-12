--
-- PostgreSQL database dump
--

-- Dumped from database version 11.4
-- Dumped by pg_dump version 11.4

-- Started on 2019-10-09 22:38:07

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 197 (class 1259 OID 37122)
-- Name: category; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.category (
    id bigint NOT NULL,
    name character varying(255),
    photo character varying(255)
);


--
-- TOC entry 196 (class 1259 OID 37120)
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2837 (class 0 OID 0)
-- Dependencies: 196
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;


--
-- TOC entry 199 (class 1259 OID 37133)
-- Name: farmer; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.farmer (
    id bigint NOT NULL,
    location character varying(255),
    name character varying(255),
    phone character varying(255)
);


--
-- TOC entry 198 (class 1259 OID 37131)
-- Name: farmer_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.farmer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2838 (class 0 OID 0)
-- Dependencies: 198
-- Name: farmer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.farmer_id_seq OWNED BY public.farmer.id;


--
-- TOC entry 201 (class 1259 OID 37144)
-- Name: product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.product (
    id bigint NOT NULL,
    available boolean NOT NULL,
    capacity double precision NOT NULL,
    description character varying(255),
    measure character varying(255) NOT NULL,
    name character varying(255),
    old_price double precision NOT NULL,
    photo character varying(255),
    price double precision NOT NULL,
    promotion boolean NOT NULL,
    quantity integer NOT NULL,
    category_id bigint,
    farmer_id bigint
);


--
-- TOC entry 200 (class 1259 OID 37142)
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2839 (class 0 OID 0)
-- Dependencies: 200
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.product_id_seq OWNED BY public.product.id;


--
-- TOC entry 2700 (class 2604 OID 37125)
-- Name: category id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.category ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);


--
-- TOC entry 2701 (class 2604 OID 37136)
-- Name: farmer id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.farmer ALTER COLUMN id SET DEFAULT nextval('public.farmer_id_seq'::regclass);


--
-- TOC entry 2702 (class 2604 OID 37147)
-- Name: product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product ALTER COLUMN id SET DEFAULT nextval('public.product_id_seq'::regclass);


--
-- TOC entry 2704 (class 2606 OID 37130)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 2706 (class 2606 OID 37141)
-- Name: farmer farmer_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.farmer
    ADD CONSTRAINT farmer_pkey PRIMARY KEY (id);


--
-- TOC entry 2708 (class 2606 OID 37152)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- TOC entry 2709 (class 2606 OID 37153)
-- Name: product fk1mtsbur82frn64de7balymq9s; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT fk1mtsbur82frn64de7balymq9s FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- TOC entry 2710 (class 2606 OID 37158)
-- Name: product fkr36yqwsg5o5hthvlqee5fi5a8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT fkr36yqwsg5o5hthvlqee5fi5a8 FOREIGN KEY (farmer_id) REFERENCES public.farmer(id);


-- Completed on 2019-10-09 22:38:07

--
-- PostgreSQL database dump complete
--

