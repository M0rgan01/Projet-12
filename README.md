# Projet 12 "Aidez la communauté en tant que développeur d'application Java"

Application de e-commerce d'une petite association d'agriculteur

## Structure du projet

Architecture micro-service en back-end :

- micro-service de configuration, P12-configuration -> à éxécuté en 1er
- micro-service de registre, P12-configuration -> à éxécuté en 2eme
- micro-service API-gateway, P12-api-gateway 
- micro-service stock, P12-stock
- micro-service account, P12-account
- micro-service order, P12-order
- micro-service batch, P12-batch

Angular 8 en front-end -> P12-UI

## Mise en route

Le lancement du script "build_application.sh" mettra en place l'application dans un environement docker. Le site web sera accesible depuis localhost:80.

## Base de données

Les données de l'application sont répartis dans 3 base de données, l'ensemble des scripts sont situés et dans leur dossier respectif du dossier "Database". Les scritps sont éxécuté automatiquement par docker.
L'ensemble des données peuvent être récupéré dans les dossier "data" créé par docker.

## Logs

L'ensemble des logs peuvent être récupéré dans le dossier "logs" créé par docker.

