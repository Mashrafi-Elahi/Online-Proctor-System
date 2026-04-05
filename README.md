# Online Proctor System

## Overview

A Java Swing desktop application that simulates an online exam with basic proctoring features. Students log in, take a timed multiple-choice exam while the system periodically captures mock webcam snapshots. Proctors log in to a separate dashboard to review all submitted results.

---

## Features

- **Login portal** – role-based login (Student / Proctor)
- **Student dashboard** – one-click exam launch
- **Proctored exam** – multiple-choice questions with a countdown timer and periodic mock snapshot captures
- **Proctor dashboard** – displays all student results with a Legit ✅ / Cheated ❌ indicator
- **Persistence** – results are saved to `results.txt`; snapshots are stored in the `snapshots/` directory

---

## Demo Credentials

| Role    | Username  | Password   |
|---------|-----------|------------|
| Student | student1  | pass123    |
| Student | student2  | pass234    |
| Student | student3  | pass345    |
| Proctor | proctor1  | admin123   |

---

## How to Run

**Prerequisites:** Java 11 or later.

```bash
# Compile
javac -d out OnlineProctorSystem.java ExamFrame.java

# Run
java -cp out onlineproctorsystem.OnlineProctorSystem
```

> If you are using an IDE such as NetBeans or IntelliJ IDEA, add both `.java` files to the same project under the `onlineproctorsystem` package and run `OnlineProctorSystem`.

---

## Files

| File | Description |
|------|-------------|
| `OnlineProctorSystem.java` | Main application – entry point, all UI frames, models, utilities, and persistence |
| `ExamFrame.java` | Exam window with questions, timer, and snapshot logic (separate dependency) |
| `results.txt` | Auto-created on first exam submission; stores `name,score,status` per line |
| `snapshots/` | Auto-created directory that holds mock snapshot files per student |

---

## Notes

- **ExamFrame dependency:** `StudentDashboard` launches `new ExamFrame(studentName)`. This class is defined in a separate `ExamFrame.java` file (not included in this repository). The application will not compile without it.
- Webcam capture is **simulated** – `WebcamUtility` writes placeholder text files instead of real images.
- Credentials are hard-coded for demonstration purposes and should not be used in production.

---

## Future Improvements

- Replace hard-coded credentials with a proper user database or file-based store
- Implement real webcam capture using a library such as [Webcam Capture](https://github.com/sarxos/webcam-capture)
- Add question randomisation and a configurable question bank
- Use a database (e.g., SQLite) instead of a plain-text `results.txt`
- Add a registration/enrolment flow so new students can be added without code changes
- Improve proctoring heuristics (tab-switch detection, multiple-face detection, etc.)
