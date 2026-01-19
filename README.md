# Memento - Life Journal Application

A JavaFX desktop application for journaling and self-reflection through 108 guided life questions.
User's answers can be shared to their friends and loved ones, so they can understand user much better.
Answering these answers will also help the user in understanding themselves much better.

## Features

- 📝 Answer 108 curated life questions
- 👤 User authentication (signup/login)
- 💾 SQLite database for data persistence
- 📊 Progress tracking
- 📖 View and edit past entries
- 📄 Share your answers with your friends and loved ones so they can understand you better by exporting journal as PDF
- 🪟 Windows installer included

## Technologies Used

- **JavaFX 17** - UI Framework
- **SQLite** - Database
- **OpenPDF** - PDF generation
- **Inno Setup** - Windows installer

## Project Structure
```
dairy/
├── src/
│   ├── App.java
│   ├── controllers/
│   ├── database/
│   ├── fxmlsViews/
│   └── CSS/
├── assets/
│   └── icon.ico
├── manifest.txt
└── installer.iss
```

## Building from Source

### Prerequisites
- JDK 17
- JavaFX SDK 17
- SQLite JDBC driver
- OpenPDF library

### Compile
```bash
javac --module-path javafx/lib --add-modules javafx.controls,javafx.fxml -d bin src/**/*.java
```

### Create JAR
```bash
jar --create --file Memento.jar --manifest manifest.txt -C bin .
```

### Create Installer
1. Install [Inno Setup](https://jrsoftware.org/isdl.php)
2. Open `installer.iss`
3. Build → Compile

## Running the Application

### From Source
```bash
java --module-path javafx/lib --add-modules javafx.controls,javafx.fxml -jar Memento.jar
```

### From Installer
Run `Memento_Setup.exe` and follow the installation wizard.

## Database

User data is stored in:
```
C:\Users\<username>\dairy\life108.db
```
life108, contains three tables only users, questions, and answers. And your data is staying in your device unless you're sharing it.

## Author

Savirar0
