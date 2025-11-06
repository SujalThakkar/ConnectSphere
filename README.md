# ConnectSphere
A Java-based contact recommendation system built using Discrete Mathematics concepts such as Graph Theory, Set Theory, and Logic. Features include login, registration, friend requests, and mutual connection recommendations with an interactive Swing-based GUI and SQLite database.


# 🌐 ConnectSphere - A Contact Recommendation System

**Developed by:** Sujal Hitesh Thakkar  
**Roll No:** 16015024079  
**Department:** Electronics and Computer Engineering  
**Institution:** K. J. Somaiya School of Engineering  
**Status:** ✅ Completed Project  
**Tech Stack:** Java (Swing, AWT, SQLite)  
**Mathematical Foundation:** Graph Theory, Set Theory, Relations, Logic  

---

## 📖 Overview

**ConnectSphere** is a Java-based contact recommendation system that helps users discover and connect with people through intelligent mutual recommendations.  
It applies **Discrete Mathematics concepts** to model user connections, identify mutual friends, and recommend new contacts efficiently.

This project bridges the gap between abstract mathematical principles and real-world social networking by combining **Graph Theory**, **Set Theory**, and **Logical reasoning** into a functional, GUI-based system.

---

## 🧠 Discrete Mathematics Concepts Used

| Concept | Application in ConnectSphere |
|----------|------------------------------|
| **Graph Theory** | Models users as vertices and friendships as edges in an undirected graph. |
| **Set Theory** | Computes mutual friends using set intersection (`retainAll()` method). |
| **Relations** | Represents friendships and friend requests as binary relations between users. |
| **Logic** | Enforces constraints using logical predicates (e.g., prevents duplicate or self-requests). |
| **Functions / Mapping** | Maps user IDs to mutual friend counts using HashMaps. |
| **Combinatorics** | Counts and ranks mutual connections for recommendation accuracy. |

---

## ⚙️ Features

- 👤 **User Authentication** – Secure registration and login system  
- 💌 **Friend Requests** – Send, accept, or reject requests  
- 🤝 **Recommendations** – Suggests new connections using mutual friends  
- 🧩 **Mutual Connections View** – Double-click recommendations to view shared friends  
- 🖥️ **Modern UI** – Clean, interactive Swing and AWT-based interface  
- 🗄️ **Local Database** – SQLite-based storage for all user data  
- 🧮 **Mathematical Logic** – Discrete Math algorithms drive backend intelligence  

---

## 🏗️ Project Structure

com.discretemath.connectsphere/
│
├── config/ # Database configuration
├── database/ # DatabaseManager (SQLite schema setup)
├── model/ # Core models (User, FriendRequest, Recommendation)
├── service/ # Logic for users, friends, and recommendations
└── ui/ # Swing UI panels (Login, Register, Dashboard, Requests)

yaml
Copy code

---

## 🚀 How to Run

### 🔧 Requirements
- Java JDK 17 or above  
- VS Code / IntelliJ IDEA  
- SQLite JDBC driver (already included in Maven dependencies)

### ▶️ Steps
1. Clone this repository  
   ```bash
   git clone https://github.com/your-username/ConnectSphere
   cd ConnectSphere
Open the project in VS Code or IntelliJ

Run the file:

swift
Copy code
src/main/java/com/discretemath/connectsphere/App.java
The ConnectSphere GUI will launch with login and registration options.

📸 GUI Overview
Login Page: User authentication interface

Register Page: Create a new account

Dashboard: Displays My Friends, Recommended Connections, and All Users

Requests Panel: Accept or reject pending friend requests

🧮 Academic Context
This project was developed as part of Tutorial 9 – Discrete Mathematics (Semester III)
under the guidance of faculty at K. J. Somaiya School of Engineering,
Department of Electronics and Computer Engineering.

It demonstrates how mathematical modeling enhances real-world problem-solving and intelligent system design.

🏆 Achievements and Highlights
Built a fully functional contact recommendation system powered by Discrete Math.

Implemented Graph Theory and Set Theory in practical friend suggestion algorithms.

Designed a modern Swing-based UI that ensures smooth interaction and clean visual flow.

Showcased how logical reasoning ensures data validity and smart recommendations.

🧭 Future Enhancements
🔔 Notification System: Real-time updates for new requests

💬 Messaging Module: Direct chat between connected users

🧠 AI-Powered Recommendations: Machine learning–based friend suggestions

🌐 Web Version: Deployment using Spring Boot or React.js

🧾 License
This project is developed for academic and learning purposes.
Feel free to explore, fork, or enhance it for educational or research use.
