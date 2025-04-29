# ZeroDayLite

> 🎯 An educational, interactive Java-based vulnerability scanner with built-in simulations, security tips, learning modules, and mini-games.

---

## 🚀 Overview

**ZeroDayLite** is a light yet powerful tool designed for cybersecurity education. It combines simulated vulnerability scanning, real-time learning, interactive payload building, and fun games to help beginners understand web security concepts safely.

This project includes:
- 🛡️ Vulnerability scanning simulations: SQLi, XSS, Keylogger
- 📡 Real-Time Web Scanner with input inspection
- 📚 Vulnerability Education Panel with expandable sections
- 🧰 Payload Builder with obfuscation, templates & preview
- 💡 Rotating Security Tips with animations
- 🎮 Fun Games (3 included)
- 📊 Statistics & Badge Tracking

---

## 🗂️ Project Structure

ZeroDayLite/
│
├── src/
|   ├── assets/ # Backgrounds, icons, GIFs for GUI 
│   ├── gui/ # All Java Swing GUI panels
│   ├── scanner/ # Core scanner logic 
│   ├── utils/ # Reusable helpers (stats, animations, DB, etc.) 
│   ├── game/ # 3 interactive games for learning 
|   └── Main.java # Entry point 
├── lib/ # External libraries (e.g., Jsoup for HTML parsing, all the jar files)
├── database/ # SQL schema for using MySQL(schema.sql)
├── README.md
└── .gitignore

---

## 🧑‍💻 How to Run

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/ZeroDayLite.git
cd ZeroDayLite

### 2. Open in IDE
Use IntelliJ, NetBeans, or any Java IDE. Open the src/ directory as your source root.

### 3. Build & Run
Ensure you have:
*Java 8+ installed
*External libraries (e.g., Jsoup in lib/) linked to your project

--->Run Main.java to start the application.

### 4. Database (Optional)
If using MySQL:
```sql
--->Create a DB and import from /database/setup.sql
--->Update DB credentials in DBConnection.java

---

## 🧰 Dependencies
>javax.swing — UI framework
>org.jsoup — for HTML parsing (included in lib/)
>mysql-connector-java — for database connection (if used)

---

## 📘 Educational Features
* Security Tips Mode: Rotating cards with animated icons
* Vulnerability Panel: Learn about 10+ vulns with examples and fun facts
* Mini Quiz: At the bottom of the education panel to test what you’ve learned
* Payload Builder: Understand how attacks like XSS/SQLi are crafted

---

## 🎓 Ideal For
> Cybersecurity beginners
> Students learning OWASP Top 10
> Demonstrations in secure code training
> Gamified learning in secure environments

---

## 📝 License
This project is licensed for educational use only. Please do not use this tool for unethical or illegal purposes.

---

## 🙌 Contributions
Pull requests are welcome! Feel free to fork and enhance ZeroDayLite with new scan types, games, or modules.

---

## 🔗 Live GitHub Repo
https://github.com/Dmishra66/ZeroDayLite

Created with 💙 for ethical hacking education.
