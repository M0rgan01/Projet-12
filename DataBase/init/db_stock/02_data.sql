--
-- PostgreSQL database dump
--

-- Dumped from database version 11.4
-- Dumped by pg_dump version 11.4

-- Started on 2019-10-09 22:38:42

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

--
-- TOC entry 2833 (class 0 OID 37122)
-- Dependencies: 197
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.category (id, name, photo) VALUES (1, 'Fruits', NULL);
INSERT INTO public.category (id, name, photo) VALUES (2, 'Légumes', NULL);
INSERT INTO public.category (id, name, photo) VALUES (3, 'Viandes', NULL);
INSERT INTO public.category (id, name, photo) VALUES (4, 'Boissons', NULL);


--
-- TOC entry 2835 (class 0 OID 37133)
-- Dependencies: 199
-- Data for Name: farmer; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.farmer (id, location, name, phone) VALUES (1, '175 route de Lyon 69230 Village', 'La ferme du coin', '0485868378');
INSERT INTO public.farmer (id, location, name, phone) VALUES (2, '5 chemin des vignes 01650 Lincieux', 'Le pré de Lincieux', '0685423611');


--
-- TOC entry 2837 (class 0 OID 37144)
-- Dependencies: 201
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.product (id, available, capacity, description, measure, name, old_price, photo, price, promotion, quantity, category_id, farmer_id) VALUES (4, true, 500, 'raisin blanc sans pépin', 'Gramme', 'Raisin de table blanc', 0, NULL, 3.02000000000000002, false, 32, 1, 2);
INSERT INTO public.product (id, available, capacity, description, measure, name, old_price, photo, price, promotion, quantity, category_id, farmer_id) VALUES (6, false, 2, 'Variété : Gala royal gala', 'Kilos', 'Pommes Gala', 0, NULL, 2.29999999999999982, false, 0, 1, 1);
INSERT INTO public.product (id, available, capacity, description, measure, name, old_price, photo, price, promotion, quantity, category_id, farmer_id) VALUES (2, true, 2, 'Variété : Carotte a cuisiner', 'Kilos', 'Carrotte', 2, NULL, 1.8899999999999999, true, 75, 2, 1);
INSERT INTO public.product (id, available, capacity, description, measure, name, old_price, photo, price, promotion, quantity, category_id, farmer_id) VALUES (9, true, 3, 'Farce (viande de porc, viande de boeuf, eau, chapelure (GLUTEN), échalotes', 'Unite', 'Paupiettes de veau', 0, NULL, 5.99000000000000021, false, 4, 3, 1);
INSERT INTO public.product (id, available, capacity, description, measure, name, old_price, photo, price, promotion, quantity, category_id, farmer_id) VALUES (10, true, 90, 'Pur jus de 5 fruits pressés', 'Centilitre', 'Jus d''orange', 3.5, NULL, 2.06000000000000005, true, 14, 4, 1);
INSERT INTO public.product (id, available, capacity, description, measure, name, old_price, photo, price, promotion, quantity, category_id, farmer_id) VALUES (11, true, 1, 'Un jus plein de saveurs avec de l''ananas.', 'Litre', 'Jus d''ananas', 0, NULL, 2.43999999999999995, false, 28, 4, 1);
INSERT INTO public.product (id, available, capacity, description, measure, name, old_price, photo, price, promotion, quantity, category_id, farmer_id) VALUES (1, true, 1, 'Calibre : 67/82 mm
Variété : Tomate ronde filet', 'Kilos', 'Tomate ronde', 0, NULL, 2, false, 60, 2, 1);
INSERT INTO public.product (id, available, capacity, description, measure, name, old_price, photo, price, promotion, quantity, category_id, farmer_id) VALUES (3, true, 1, NULL, 'Unite', 'Aubergine', 0.900000000000000022, NULL, 0.800000000000000044, true, 37, 2, 2);
INSERT INTO public.product (id, available, capacity, description, measure, name, old_price, photo, price, promotion, quantity, category_id, farmer_id) VALUES (5, true, 750, 'Variété : Prune rouge', 'Gramme', 'Prunes rouges', 3.60000000000000009, NULL, 3.5, true, 83, 2, 2);
INSERT INTO public.product (id, available, capacity, description, measure, name, old_price, photo, price, promotion, quantity, category_id, farmer_id) VALUES (7, true, 1, NULL, 'Unite', 'Viande bovine - Bavette Aloyau', 0, NULL, 5.04999999999999982, false, 9, 3, 2);
INSERT INTO public.product (id, available, capacity, description, measure, name, old_price, photo, price, promotion, quantity, category_id, farmer_id) VALUES (8, true, 1, NULL, 'Unite', 'Viande bovine - steak à griller', 4.90000000000000036, NULL, 4.01999999999999957, true, 3, 3, 1);


--
-- TOC entry 2843 (class 0 OID 0)
-- Dependencies: 196
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.category_id_seq', 4, true);


--
-- TOC entry 2844 (class 0 OID 0)
-- Dependencies: 198
-- Name: farmer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.farmer_id_seq', 2, true);


--
-- TOC entry 2845 (class 0 OID 0)
-- Dependencies: 200
-- Name: product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.product_id_seq', 11, true);


-- Completed on 2019-10-09 22:38:42

--
-- PostgreSQL database dump complete
--

