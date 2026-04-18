# Food Delivery Management System 🍔🚚

Un sistem backend pentru gestionarea unei platforme de food delivery, dezvoltat în Java. Acest proiect demonstrează aplicarea practică a principiilor fundamentale de Programare Orientată pe Obiecte (OOP), precum moștenirea, polimorfismul, încapsularea și utilizarea colecțiilor (List, Map) pentru gestionarea fluxurilor de date.

## 📦 Arhitectura Sistemului (Clase / Obiecte)

Sistemul este extins și securizat față de concepția inițială. Acum este construit în jurul a 9 entități principale și un enum pentru stări:

1. **`User`** - Clasă de bază (abstractă) pentru toți actorii din platformă. Implementează validări stricte (format email, telefon 10 cifre).
2. **`Customer`** - Moștenește `User`; reprezintă clienții care plasează comenzi. Include gestionarea sigură a adreselor (validare null/empty/duplicate).
3. **`Driver`** - Moștenește `User`; gestionează disponibilitatea și vehiculul de livrare.
4. **`Manager`** - Moștenește `User`; are drepturi de administrare asupra unui local.
5. **`Restaurant`** - Reprezintă restaurantul. Gestionează liste de meniuri, manageri și recenzii, care sunt exportate în siguranță ca liste nemodificabile (`Collections.unmodifiableList()`).
6. **`Menu`** - Entitate nouă, dedicată. Gestionează organizarea produselor folosind categorii ca și chei într-un `Map`.
7. **`Product`** - Un element care conține detalii de vânzare, cu validări noi pentru câmpuri de preț, stoc, nume și care implementează `Comparable`.
8. **`Order`** - Entitatea centrală ce face legătura între `Customer`, produse, `Driver` și cost total calculat automat.
9. **`Review`** - Permite clienților să lase un rating (validat între 1 și 5 inclusiv) și un comentariu asociat.
10. **`OrderStatus`** - Un `Enum` complet dedicat care definește întreg ciclul de viață pentru preluarea și livrarea unei comenzi.

## ⚙️ Funcționalități și Interogări (Acțiuni)

Platforma suportă (și va implementa până la finalul dezvoltării) următoarele operațiuni principale:

1. **Înregistrarea unui utilizator nou** (`Customer`, `Driver` sau `Manager`) cu validarea datelor personale.
2. **Adăugarea unui local nou** (`Restaurant`) în platformă și asocierea acestuia cu un `Manager`.
3. **Gestionarea meniului** (`Menu` - adăugare de produse pe categorii), acțiune permisă cu validare strictă doar pentru Managerul localului respectiv.
4. **Interogarea meniului** unui local pentru a afișa toate produsele disponibile, sortate alfabetic.
5. **Căutarea unui local** în platformă pe baza numelui său.
6. **Plasarea unei comenzi** (`Order`) de către un `Customer`, incluzând calculul automat al costului total al produselor.
7. **Interogarea listei de șoferi disponibili** (filtrarea șoferilor care nu sunt asignați unei alte livrări).
8. **Asignarea unui șofer** (`Driver`) disponibil la o comandă abia plasată.
9. **Actualizarea stării unei comenzi** pe parcursul fluxului folosind `OrderStatus` (Plasată -> În Preparare -> Preluată -> Livrată).
10. **Interogarea istoricului de comenzi** pentru a afișa activitatea trecută a unui anumit `Customer`.
11. **Adăugarea unei recenzii** (`Review` cu notă 1-5 și text) de către un Client pentru un local.
12. **Afișarea recenziilor** lăsate pentru un anumit local.

## 🛠️ Tehnologii Utilizate
* **Limbaj:** Java
* **Concepte:** OOP (Inheritance, Polymorphism, Encapsulation îmbunătățită contra mutațiilor externe), Collections Framework (ArrayList, HashMap), Enums.


## Structura proiectului (separarea logicii de business si a modelului):

```text
src/
└── food_delivery_system/ 
    │
    ├── models/                 
    │   ├── User.java
    │   ├── Customer.java
    │   ├── Driver.java
    │   ├── Manager.java
    │   ├── Restaurant.java
    │   ├── Menu.java
    │   ├── Product.java
    │   ├── Order.java
    │   ├── Review.java
    │   └── OrderStatus.java
    │
    ├── services/               
    │   ├── FoodDeliveryService.java
    │   ├── OrderService.java
    │   ├── RestaurantService.java
    │   └── UserService.java
    │
    └── main/                   
        └── Main.java
```

# ENGLISH VERSION:

## Food Delivery Management System 🍔🚚

A backend system for managing a food delivery platform, developed in Java. This project demonstrates the practical application of fundamental Object-Oriented Programming (OOP) principles, such as inheritance, polymorphism, encapsulation, and the use of collections for data flow management.

## 📦 System Architecture (Classes / Objects)

The system has been updated from its initial conception to include better field handling and validations. It is built around 9 main entities and a lifecycle enum:

1. **`User`** - Base abstract class. Incorporates robust regex validations for properties like emails and phones.
2. **`Customer`** - Inherits from `User`; places orders and safely manages validated lists of addresses.
3. **`Driver`** - Inherits from `User`; manages availability and the delivery vehicle.
4. **`Manager`** - Inherits from `User`; holds administrative rights over a specific restaurant.
5. **`Restaurant`** - The vendor. Maintains collections of menus, managers, and reviews securely protected via `Collections.unmodifiableList()`.
6. **`Menu`** - Dedicated new class categorizing products neatly via a `Map` backend.
7. **`Product`** - An item, enhanced with input validation on prices/stocks, and implements `Comparable`.
8. **`Order`** - Links a `Customer`, products, the total pricing, a `Driver`, and handles current status transitions.
9. **`Review`** - Designed for feedback with rigid rating bounds (e.g., 1-5).
10. **`OrderStatus`** - The central Enum establishing valid states throughout standard delivery operations.

## ⚙️ Features and Queries (Actions)

The platform supports (and aims to implement by the end of development) the following main operations:

1. **Registering a new user** (`Customer`, `Driver`, or `Manager`) with validated personal data.
2. **Adding a new restaurant** (`Restaurant`) to the platform and associating it with a `Manager`.
3. **Menu management** (`Menu` - adding classified products), an action permitted with strict validation only for the `Manager` of that specific restaurant.
4. **Querying a restaurant's menu** to display all available products, sorted alphabetically.
5. **Searching for a restaurant** in the platform based on its name.
6. **Placing an order** (`Order`) by a `Customer`, including the automatic calculation of the total cost of the products.
7. **Querying the list of available drivers** (filtering drivers who are not assigned to another delivery).
8. **Assigning an available driver** (`Driver`) to a newly placed order.
9. **Updating the status of an order** throughout the flow using `OrderStatus` (Placed -> In Preparation -> Picked Up -> Delivered).
10. **Querying the order history** to display the past activity of a specific `Customer`.
11. **Adding a review** (`Review` with rating 1-5 and text) by a `Customer` for a restaurant.
12. **Displaying the reviews** left for a specific restaurant.

## 🛠️ Technologies Used
* **Language:** Java
* **Concepts:** OOP (Inheritance, Polymorphism, rigorous Encapsulation), Collections Framework (ArrayList, HashMap), Enums.

## Project Structure (separating business logic from the model):

```text
src/
└── food_delivery_system/ 
    │
    ├── models/                 
    │   ├── User.java
    │   ├── Customer.java
    │   ├── Driver.java
    │   ├── Manager.java
    │   ├── Restaurant.java
    │   ├── Menu.java
    │   ├── Product.java
    │   ├── Order.java
    │   ├── Review.java
    │   └── OrderStatus.java
    │
    ├── services/               
    │   ├── FoodDeliveryService.java
    │   ├── OrderService.java
    │   ├── RestaurantService.java
    │   └── UserService.java
    │
    └── main/                   
        └── Main.java
```
