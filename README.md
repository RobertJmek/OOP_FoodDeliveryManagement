# Food Delivery Management System 🍔🚚

Un sistem backend pentru gestionarea unei platforme de food delivery, dezvoltat în Java. Acest proiect demonstrează aplicarea practică a principiilor fundamentale de Programare Orientată pe Obiecte (OOP), precum moștenirea, polimorfismul, încapsularea și utilizarea colecțiilor (List, Set) pentru gestionarea fluxurilor de date.

## 📦 Arhitectura Sistemului (Clase / Obiecte)

Sistemul este construit în jurul a 8 entități principale, structurate ierarhic:

1. **`Utilizator`** - Clasă de bază (abstractă) pentru toți actorii din platformă.
2. **`Client`** - Moștenește `Utilizator`; reprezintă clienții care plasează comenzi.
3. **`Sofer`** - Moștenește `Utilizator`; gestionează disponibilitatea și vehiculul de livrare.
4. **`Manager`** - Moștenește `Utilizator`; are drepturi de administrare asupra unui local.
5. **`Local`** - Reprezintă restaurantul/vendorul care deține un meniu.
6. **`Produs`** - Un element din meniu (implementează `Comparable` pentru sortare automată).
7. **`Comanda`** - Entitatea centrală care leagă un Client, un Local, produsele, un Șofer și starea curentă a livrării.
8. **`Recenzie`** - Permite clienților să lase un rating și un comentariu după finalizarea unei comenzi.

## ⚙️ Funcționalități și Interogări (Acțiuni)

Platforma suportă următoarele operațiuni principale:

1. **Înregistrarea unui utilizator nou** (Client, Șofer sau Manager) cu datele personale.
2. **Adăugarea unui local nou** în platformă și asocierea acestuia cu un Manager.
3. **Gestionarea meniului** (adăugare de produse), acțiune permisă cu validare strictă doar pentru Managerul localului respectiv.
4. **Interogarea meniului** unui local pentru a afișa toate produsele disponibile, sortate alfabetic.
5. **Căutarea unui local** în platformă pe baza numelui său.
6. **Plasarea unei comenzi** de către un Client, incluzând calculul automat al costului total al produselor.
7. **Interogarea listei de șoferi disponibili** (filtrarea șoferilor care nu sunt asignați unei alte livrări).
8. **Asignarea unui șofer** disponibil la o comandă abia plasată.
9. **Actualizarea stării unei comenzi** pe parcursul fluxului (Plasată -> În Preparare -> Preluată -> Livrată).
10. **Interogarea istoricului de comenzi** pentru a afișa activitatea trecută a unui anumit Client.
11. **Adăugarea unei recenzii** (notă și text) de către un Client pentru un local.
12. **Afișarea recenziilor** lăsate pentru un anumit local.

## 🛠️ Tehnologii Utilizate
* **Limbaj:** Java
* **Concepte:** OOP (Inheritance, Polymorphism, Encapsulation), Collections Framework (ArrayList, TreeSet), Enums.


## Structura proiectului(separarea logicii de business si a modelului):

```text
src/
└── food_delivery_system/ 
    │
    ├── models/                 
    │   ├── Utilizator.java
    │   ├── Client.java
    │   ├── Sofer.java
    │   ├── Manager.java
    │   ├── Local.java
    │   ├── Produs.java
    │   ├── Comanda.java
    │   ├── Recenzie.java
    │   └── StareComanda.java   (Enum-ul)
    │
    ├── services/               
    │   └── FoodDeliveryService.java
    │
    └── main/                   
        └── Main.java
```

# ENGLISH VERSION:

## Food Delivery Management System 🍔🚚

A backend system for managing a food delivery platform, developed in Java. This project demonstrates the practical application of fundamental Object-Oriented Programming (OOP) principles, such as inheritance, polymorphism, encapsulation, and the use of collections (List, Set) for data flow management.

## 📦 System Architecture (Classes / Objects)

The system is built around 8 main entities, structured hierarchically:

1. **`User`** - Base class (abstract) for all actors in the platform.
2. **`Customer`** - Inherits from `User`; represents the customers who place orders.
3. **`Driver`** - Inherits from `User`; manages availability and the delivery vehicle.
4. **`Manager`** - Inherits from `User`; has administrative rights over a specific restaurant/venue.
5. **`Restaurant`** - Represents the restaurant/vendor that owns a menu.
6. **`Product`** - An item from the menu (implements `Comparable` for automatic sorting).
7. **`Order`** - The central entity linking a `Customer`, a `Restaurant`, the products, a `Driver`, and the current delivery status.
8. **`Review`** - Allows customers to leave a rating and a comment after completing an order.

## ⚙️ Features and Queries (Actions)

The platform supports the following main operations:

1. **Registering a new user** (`Customer`, `Driver`, or `Manager`) with personal data.
2. **Adding a new restaurant** to the platform and associating it with a `Manager`.
3. **Menu management** (adding products), an action permitted with strict validation only for the `Manager` of that specific restaurant.
4. **Querying a restaurant's menu** to display all available products, sorted alphabetically.
5. **Searching for a restaurant** in the platform based on its name.
6. **Placing an order** by a `Customer`, including the automatic calculation of the total cost of the products.
7. **Querying the list of available drivers** (filtering drivers who are not assigned to another delivery).
8. **Assigning an available driver** to a newly placed order.
9. **Updating the status of an order** throughout the flow (Placed -> In Preparation -> Picked Up -> Delivered).
10. **Querying the order history** to display the past activity of a specific `Customer`.
11. **Adding a review** (rating and text) by a `Customer` for a restaurant.
12. **Displaying the reviews** left for a specific restaurant.

## 🛠️ Technologies Used
* **Language:** Java
* **Concepts:** OOP (Inheritance, Polymorphism, Encapsulation), Collections Framework (ArrayList, TreeSet), Enums.

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
    │   ├── Product.java
    │   ├── Order.java
    │   ├── Review.java
    │   └── OrderStatus.java   (The Enum)
    │
    ├── services/               
    │   └── FoodDeliveryService.java
    │
    └── main/                   
        └── Main.java
