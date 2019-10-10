
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


CREATE TABLE public.mail (
    id bigint NOT NULL,
    available_password_recovery boolean NOT NULL,
    email character varying(255),
    expiry_password_recovery timestamp without time zone,
    expiry_token timestamp without time zone,
    token character varying(255),
    try_token integer NOT NULL
);



CREATE SEQUENCE public.mail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



ALTER SEQUENCE public.mail_id_seq OWNED BY public.mail.id;



CREATE TABLE public.role (
    id bigint NOT NULL,
    name character varying(255)
);



CREATE SEQUENCE public.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



ALTER SEQUENCE public.role_id_seq OWNED BY public.role.id;



CREATE TABLE public.users (
    id bigint NOT NULL,
    active boolean NOT NULL,
    expiry_connection timestamp without time zone,
    pass_word character varying(255),
    pass_word_confirm character varying(255),
    try_connection integer NOT NULL,
    user_name character varying(255),
    mail_id bigint
);




CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;



CREATE TABLE public.users_roles (
    user_id bigint NOT NULL,
    roles_id bigint NOT NULL
);



ALTER TABLE ONLY public.mail ALTER COLUMN id SET DEFAULT nextval('public.mail_id_seq'::regclass);



ALTER TABLE ONLY public.role ALTER COLUMN id SET DEFAULT nextval('public.role_id_seq'::regclass);



ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);



ALTER TABLE ONLY public.mail
    ADD CONSTRAINT mail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);




ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fk15d410tj6juko0sq9k4km60xq FOREIGN KEY (roles_id) REFERENCES public.role(id);



ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fk2o0jvgh89lemvvo17cbqvdxaa FOREIGN KEY (user_id) REFERENCES public.users(id);



ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkk5cjn4a5pvwno2bnxeng0ae1e FOREIGN KEY (mail_id) REFERENCES public.mail(id);




