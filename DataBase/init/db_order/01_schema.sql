--
-- PostgreSQL database dump
--

-- Dumped from database version 11.4
-- Dumped by pg_dump version 11.4

-- Started on 2019-10-09 22:36:27

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
-- TOC entry 197 (class 1259 OID 37100)
-- Name: order_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.order_product (
    id bigint NOT NULL,
    order_quantity integer NOT NULL,
    product_id bigint,
    product_old_price double precision NOT NULL,
    product_price double precision NOT NULL,
    real_quantity integer NOT NULL,
    total_price_row double precision NOT NULL,
    order_id bigint
);


--
-- TOC entry 196 (class 1259 OID 37098)
-- Name: order_product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.order_product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2825 (class 0 OID 0)
-- Dependencies: 196
-- Name: order_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.order_product_id_seq OWNED BY public.order_product.id;


--
-- TOC entry 199 (class 1259 OID 37108)
-- Name: orders; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.orders (
    id bigint NOT NULL,
    cancel boolean,
    date timestamp without time zone NOT NULL,
    paid boolean NOT NULL,
    reception date NOT NULL,
    reference character varying(255),
    total_price double precision NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT orders_total_price_check CHECK ((total_price >= (0)::double precision))
);


--
-- TOC entry 198 (class 1259 OID 37106)
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2826 (class 0 OID 0)
-- Dependencies: 198
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- TOC entry 2691 (class 2604 OID 37103)
-- Name: order_product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_product ALTER COLUMN id SET DEFAULT nextval('public.order_product_id_seq'::regclass);


--
-- TOC entry 2692 (class 2604 OID 37111)
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- TOC entry 2695 (class 2606 OID 37105)
-- Name: order_product order_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_product
    ADD CONSTRAINT order_product_pkey PRIMARY KEY (id);


--
-- TOC entry 2697 (class 2606 OID 37114)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 2698 (class 2606 OID 37115)
-- Name: order_product fkl5mnj9n0di7k1v90yxnthkc73; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_product
    ADD CONSTRAINT fkl5mnj9n0di7k1v90yxnthkc73 FOREIGN KEY (order_id) REFERENCES public.orders(id);


-- Completed on 2019-10-09 22:36:27

--
-- PostgreSQL database dump complete
--

