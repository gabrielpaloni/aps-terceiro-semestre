# Amazonia University - APS 🏛️

This project is a desktop application developed in **Java** as part of the Supervised Practical Activity (APS). The system was designed to manage portals for different user profiles (Students, Professors, and Administrators), utilizing an organized and scalable architecture.

## 🏗️ Project Architecture
The system follows the **MVC (Model-View-Controller)** pattern, ensuring a clear separation of concerns:

* **Model:** Contains entity classes (e.g., `Aluno.java`) and persistence logic (`Conexao.java`).
* **View:** User interface, composed of multiple login screens and specific portals for each access level.
* **Controller:** Manages the data flow between the View and the Model (e.g., `AlunoController.java`, `ProfessorController.java`).

## 🚀 Key Features
* **Multi-level Authentication:** Customized login screens for Administrators, Students, and Professors.
* **Student/Professor/Admin Portals:** Distinct interfaces with specific functionalities for each user type.
* **Data Management:** Centralized controller for handling academic information.
* **Database Connection:** Dedicated class to manage the connection state and persistence.

## 🛠️ Technologies Used
* **Language:** Java
* **IDE:** IntelliJ IDEA (`.idea`, `.iml` files)
* **Version Control:** Git

## 📂 How to Run the Project
1. Clone this repository.
2. Open the project in your preferred IDE (IntelliJ IDEA recommended).
3. Ensure the Java SDK is correctly configured.
4. Run the `Main.java` class located at the root of the `src` directory.

---
*Project developed by Gabriel Paloni for the Computer Science program.*
