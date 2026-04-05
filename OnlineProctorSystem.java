/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package onlineproctorsystem;
// OnlineProctorSystem.java
// OnlineProctorSystem.java

// OnlineProctorSystem.java


import java.util.List;
import java.util.Arrays;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class OnlineProctorSystem {

    public static void main(String[] args) {
        new LoginFrame();
    }

    // ---------- Login Frame ----------
    static class LoginFrame extends JFrame {
        private JTextField usernameField;
        private JPasswordField passwordField;
        private JComboBox<String> roleCombo;

        private final Map<String, String> studentAccounts = Map.of(
            "student1", "pass123",
            "student2", "pass234",
            "student3", "pass345"
        );

        private final Map<String, String> proctorAccounts = Map.of(
            "proctor1", "admin123"
        );

        public LoginFrame() {
            setTitle("Login - Online Proctor System");
            setSize(400, 250);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            JPanel panel = new JPanel(new GridLayout(4, 2, 15, 15));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            panel.setBackground(new Color(230, 240, 255));

            JLabel title = new JLabel("Login Portal", SwingConstants.CENTER);
            title.setFont(new Font("Verdana", Font.BOLD, 20));
            title.setForeground(new Color(60, 90, 170));
            add(title, BorderLayout.NORTH);

            panel.add(new JLabel("Username:"));
            usernameField = new JTextField();
            panel.add(usernameField);

            panel.add(new JLabel("Password:"));
            passwordField = new JPasswordField();
            panel.add(passwordField);

            panel.add(new JLabel("Role:"));
            roleCombo = new JComboBox<>(new String[]{"Student", "Proctor"});
            panel.add(roleCombo);

            JButton loginBtn = new JButton("Login");
            loginBtn.setBackground(new Color(60, 90, 170));
            loginBtn.setForeground(Color.WHITE);
            loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
            loginBtn.addActionListener(e -> login());
            panel.add(new JLabel());
            panel.add(loginBtn);

            add(panel, BorderLayout.CENTER);
            setVisible(true);
        }

        private void login() {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();

            if (role.equals("Student") && studentAccounts.containsKey(user) && studentAccounts.get(user).equals(pass)) {
                dispose();
                new StudentDashboard(user);
            } else if (role.equals("Proctor") && proctorAccounts.containsKey(user) && proctorAccounts.get(user).equals(pass)) {
                dispose();
                new ProctorDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        }
    }

    // ---------- Student Dashboard ----------
    static class StudentDashboard extends JFrame {
        public StudentDashboard(String studentName) {
            setTitle("Student Dashboard");
            setSize(400, 200);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            JButton startExamBtn = new JButton("Start Exam");
            startExamBtn.setFont(new Font("Arial", Font.BOLD, 18));
            startExamBtn.setBackground(new Color(100, 180, 100));
            startExamBtn.setForeground(Color.WHITE);
            startExamBtn.addActionListener(e -> {
                dispose();
                new ExamFrame(studentName);
            });

            JPanel panel = new JPanel();
            panel.setBackground(new Color(240, 255, 240));
            panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
            panel.add(startExamBtn);
            add(panel);
            setVisible(true);
        }
    }

    // ---------- Question Model ----------
    static class Question {
        String text;
        String[] choices;
        int correctIndex;

        Question(String text, String[] choices, int correctIndex) {
            this.text = text;
            this.choices = choices;
            this.correctIndex = correctIndex;
        }
    }

    // ---------- Webcam Utility ----------
    static class WebcamUtility {
        static void captureImage(String studentName, int captureIndex) {
            try {
                File file = new File("snapshots/" + studentName + "_snap_" + captureIndex + ".jpg");
                file.getParentFile().mkdirs();
                file.createNewFile();
                try (FileWriter fw = new FileWriter(file)) {
                    fw.write("[Mock Image Capture for " + studentName + " - Snapshot " + captureIndex + "]\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ---------- Result Frame ----------
    static class ResultFrame extends JFrame {
        public ResultFrame(int score) {
            setTitle("Exam Result");
            setSize(300, 150);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            JLabel label = new JLabel("Your Score: " + score, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 22));
            add(label);
            setVisible(true);
        }
    }

    // ---------- Proctor Dashboard ----------
    static class ProctorDashboard extends JFrame {
        public ProctorDashboard() {
            setTitle("Proctor Dashboard");
            setSize(500, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            JTextArea area = new JTextArea();
            area.setFont(new Font("Monospaced", Font.PLAIN, 16));
            JScrollPane scroll = new JScrollPane(area);
            add(scroll);

            StringBuilder log = new StringBuilder("Student Results (✅ Legit / ❌ Cheated)\n\n");
            try (BufferedReader reader = new BufferedReader(new FileReader("results.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String name = parts[0];
                        String score = parts[1];
                        String status = parts[2];
                        String emoji = status.equals("Legit") ? "✅" : "❌";
                        log.append(name).append(" - Score: ").append(score).append(" ").append(emoji).append(" (Status: ").append(status).append(")\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            area.setText(log.toString());
            area.setEditable(false);
            setVisible(true);
        }
    }

    // ---------- Save Results ----------
    static void saveResult(String name, int score, String status) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt", true))) {
            writer.write(name + "," + score + "," + status);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

