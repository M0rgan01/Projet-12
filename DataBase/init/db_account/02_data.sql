
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


INSERT INTO public.mail (id, available_password_recovery, email, expiry_password_recovery, expiry_token, token, try_token) VALUES (1, false, 'pichat.morgan@gmail.com', NULL, NULL, NULL, 0);
INSERT INTO public.mail (id, available_password_recovery, email, expiry_password_recovery, expiry_token, token, try_token) VALUES (2, false, 'test2@account.fr', NULL, NULL, NULL, 0);


INSERT INTO public.role (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.role (id, name) VALUES (2, 'ROLE_USER');



INSERT INTO public.users (id, active, expiry_connection, pass_word, pass_word_confirm, try_connection, user_name, mail_id) VALUES (1, true, NULL, '$2a$10$RhQn4NmwxdoxRdcqNhWvDu9k5YwiSEPktMwyjznDFXWbtSrQC1dle', NULL, 0, 'Admin', 1);
INSERT INTO public.users (id, active, expiry_connection, pass_word, pass_word_confirm, try_connection, user_name, mail_id) VALUES (2, true, NULL, '$2a$10$/2FO1yrdrHVT.oTwkyy3duAMzIPwdUCk9N2GPeS3Maaf8RDRPkMNq', NULL, 0, 'User', 2);



INSERT INTO public.users_roles (user_id, roles_id) VALUES (1, 1);
INSERT INTO public.users_roles (user_id, roles_id) VALUES (1, 2);
INSERT INTO public.users_roles (user_id, roles_id) VALUES (2, 2);



SELECT pg_catalog.setval('public.mail_id_seq', 2, true);



SELECT pg_catalog.setval('public.role_id_seq', 2, true);



SELECT pg_catalog.setval('public.users_id_seq', 2, true);


