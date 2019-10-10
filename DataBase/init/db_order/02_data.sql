--
-- PostgreSQL database dump
--

-- Dumped from database version 11.4
-- Dumped by pg_dump version 11.4

-- Started on 2019-10-09 22:35:27

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
-- TOC entry 2823 (class 0 OID 37108)
-- Dependencies: 199
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.orders (id, cancel, date, paid, reception, reference, total_price, user_id) VALUES (1, false, '2019-10-09 21:42:11.193', false, '2019-10-10', '00000-10/9', 13.1799999999999997, 1);
INSERT INTO public.orders (id, cancel, date, paid, reception, reference, total_price, user_id) VALUES (2, false, '2019-10-09 21:42:45.94', false, '2019-10-15', '00001-10/9', 12.0599999999999987, 1);
INSERT INTO public.orders (id, cancel, date, paid, reception, reference, total_price, user_id) VALUES (3, false, '2019-10-09 21:44:14.094', false, '2019-10-11', '00002-10/9', 14.8200000000000003, 2);
INSERT INTO public.orders (id, cancel, date, paid, reception, reference, total_price, user_id) VALUES (4, true, '2019-10-09 21:44:40.987', false, '2019-10-26', '00003-10/9', 22.3999999999999986, 2);
INSERT INTO public.orders (id, cancel, date, paid, reception, reference, total_price, user_id) VALUES (5, false, '2019-10-09 21:45:11.248', false, '2019-10-18', '00004-10/9', 9.07000000000000028, 2);


--
-- TOC entry 2821 (class 0 OID 37100)
-- Dependencies: 197
-- Data for Name: order_product; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.order_product (id, order_quantity, product_id, product_old_price, product_price, real_quantity, total_price_row, order_id) VALUES (1, 2, 2, 2, 1.8899999999999999, 2, 3.7799999999999998, 1);
INSERT INTO public.order_product (id, order_quantity, product_id, product_old_price, product_price, real_quantity, total_price_row, order_id) VALUES (2, 3, 3, 0.900000000000000022, 0.800000000000000044, 3, 2.40000000000000036, 1);
INSERT INTO public.order_product (id, order_quantity, product_id, product_old_price, product_price, real_quantity, total_price_row, order_id) VALUES (3, 2, 5, 3.60000000000000009, 3.5, 2, 7, 1);
INSERT INTO public.order_product (id, order_quantity, product_id, product_old_price, product_price, real_quantity, total_price_row, order_id) VALUES (4, 3, 8, 4.90000000000000036, 4.01999999999999957, 3, 12.0599999999999987, 2);
INSERT INTO public.order_product (id, order_quantity, product_id, product_old_price, product_price, real_quantity, total_price_row, order_id) VALUES (5, 1, 2, 2, 1.8899999999999999, 1, 1.8899999999999999, 3);
INSERT INTO public.order_product (id, order_quantity, product_id, product_old_price, product_price, real_quantity, total_price_row, order_id) VALUES (6, 1, 9, 0, 5.99000000000000021, 1, 5.99000000000000021, 3);
INSERT INTO public.order_product (id, order_quantity, product_id, product_old_price, product_price, real_quantity, total_price_row, order_id) VALUES (7, 1, 10, 3.5, 2.06000000000000005, 1, 2.06000000000000005, 3);
INSERT INTO public.order_product (id, order_quantity, product_id, product_old_price, product_price, real_quantity, total_price_row, order_id) VALUES (8, 2, 11, 0, 2.43999999999999995, 2, 4.87999999999999989, 3);
INSERT INTO public.order_product (id, order_quantity, product_id, product_old_price, product_price, real_quantity, total_price_row, order_id) VALUES (9, 3, 1, 0, 2, 3, 6, 4);
INSERT INTO public.order_product (id, order_quantity, product_id, product_old_price, product_price, real_quantity, total_price_row, order_id) VALUES (10, 3, 3, 0.900000000000000022, 0.800000000000000044, 3, 2.40000000000000036, 4);
INSERT INTO public.order_product (id, order_quantity, product_id, product_old_price, product_price, real_quantity, total_price_row, order_id) VALUES (11, 4, 5, 3.60000000000000009, 3.5, 4, 14, 4);
INSERT INTO public.order_product (id, order_quantity, product_id, product_old_price, product_price, real_quantity, total_price_row, order_id) VALUES (12, 1, 7, 0, 5.04999999999999982, 1, 5.04999999999999982, 5);
INSERT INTO public.order_product (id, order_quantity, product_id, product_old_price, product_price, real_quantity, total_price_row, order_id) VALUES (13, 1, 8, 4.90000000000000036, 4.01999999999999957, 1, 4.01999999999999957, 5);


--
-- TOC entry 2829 (class 0 OID 0)
-- Dependencies: 196
-- Name: order_product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.order_product_id_seq', 13, true);


--
-- TOC entry 2830 (class 0 OID 0)
-- Dependencies: 198
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.orders_id_seq', 5, true);


-- Completed on 2019-10-09 22:35:28

--
-- PostgreSQL database dump complete
--

