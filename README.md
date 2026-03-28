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

src/
└── ro/unibuc/fooddelivery/ (sau orice alt nume de pachet de bază alegi)
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
