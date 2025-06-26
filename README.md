# 📚 TP JEE – Design Patterns et API REST

Ce projet est une application **Spring Boot (Java 21)** combinant :
- Un TP sur les **API RESTful avec Spring Boot** (JPA, validation, exceptions, DTO…)
- Un TP sur l’implémentation de **Design Patterns (Création, Comportementaux)**

---

## 📌 Objectif pédagogique

- Implémenter une API complète en Java avec Spring Boot
- Utiliser plusieurs **Design Patterns** (Singleton, Factory, Strategy, Observer)
- Produire des tests unitaires (couverture > 50%)
- Gérer les erreurs de façon claire
- Documenter l’API avec OpenAPI

---

## 🚀 Lancer le projet

### 🔧 Prérequis
- Java 21
- Maven
- IDE IntelliJ

### ▶️ Lancement

```bash
# Compiler et lancer le projet
mvn clean spring-boot:run
```
Accès local : http://localhost:8080
Accès h2: http://localhost:8080/h2-console/

## 🔍 API REST – Endpoints de test
### 📘 Users
Méthode	Endpoint	Description
GET	/api/users	Liste des utilisateurs
GET	/api/users/{id}	Détail d’un utilisateur
POST	/api/users	Créer un utilisateur
PUT	/api/users/{id}	Modifier un utilisateur
DELETE	/api/users/{id}	Supprimer un utilisateur

### 📚 Books
Méthode	Endpoint	Description
GET	/api/books	Liste des livres (avec filtres)
GET	/api/books/{id}	Détail d’un livre
POST	/api/books	Ajouter un livre
PUT	/api/books/{id}	Modifier un livre
DELETE	/api/books/{id}	Supprimer un livre

Filtres possibles via query params (facultatif) :

GET /api/books?title=java&author=martin&status=AVAILABLE

### 🔁 Loans
Méthode	Endpoint	Description
GET	/api/loans	Liste des emprunts
POST	/api/loans	Créer un nouvel emprunt
PUT	/api/loans/{id}/return	Retourner un livre
GET	/api/loans/user/{userId}	Voir les emprunts d’un utilisateur

## 🧠 Design Patterns utilisés
Pattern	Type	Utilisation
Singleton	Création	NotificationService
Factory	Création	LoanFactory (création de prêts)
Strategy	Comportemental	Validation des DTO (Email, ISBN)
Observer	Comportemental	Notification lors d’un prêt/retour

## 🧪 Tests et qualité
  Framework de test : JUnit 5
  Mocking : Mockito
  Couverture : > 70% avec JaCoCo

  Lancer les tests :
```
mvn test
```
   Générer un rapport de couverture :
```
mvn jacoco:prepare-agent test jacoco:report
# Rapport : target/site/jacoco/index.html
```

## 📦 Structure du projet
```
src/
├── controller/
├── dto/
├── entity/
├── exception/
├── factory/
├── repository/
├── service/
└── strategy/
```
## 📄 Documentation OpenAPI

Disponible à :

http://localhost:8080/swagger-ui.html

## 👤 Auteur

Florian Tran — M1 ESGI — 2025
Projet réalisé dans le cadre des TP JEE / TP Design Pattern
