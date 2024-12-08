import java.awt.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VolunteerLog {

    private static final String FILE_NAME = "VolunteerLog.csv";
    private static final Map<String, Volunteer> volunteers = new HashMap<>();
    private static final DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Name", "Email", "Phone", "Total Hours"}, 0);

    public static void main(String[] args) {
        loadVolunteersFromFile();
        SwingUtilities.invokeLater(VolunteerLog::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Volunteer Log");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Table to display volunteers
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        // Buttons for actions
        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Register Volunteer");
        JButton logHoursButton = new JButton("Log Hours");
        JButton saveButton = new JButton("Save to File");
        buttonPanel.add(registerButton);
        buttonPanel.add(logHoursButton);
        buttonPanel.add(saveButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Register button action
        registerButton.addActionListener(e -> registerVolunteer());

        // Log hours button action
        logHoursButton.addActionListener(e -> logHours());

        // Save button action
        saveButton.addActionListener(e -> saveVolunteersToFile());

        // Load volunteers into the table
        updateTable();

        frame.setVisible(true);
    }

    private static void registerVolunteer() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();

        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Phone:", phoneField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Register Volunteer", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (!email.isEmpty() && !volunteers.containsKey(email)) {
                volunteers.put(email, new Volunteer(name, email, phone));
                updateTable();
                JOptionPane.showMessageDialog(null, "Volunteer registered successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Email already exists or is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void logHours() {
        String email = JOptionPane.showInputDialog(null, "Enter volunteer's email:", "Log Hours", JOptionPane.PLAIN_MESSAGE);
        if (email == null || email.isEmpty()) {
            return;
        }

        Volunteer volunteer = volunteers.get(email);
        if (volunteer == null) {
            JOptionPane.showMessageDialog(null, "No volunteer found with that email!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField startTimeField = new JTextField();
        JTextField endTimeField = new JTextField();

        Object[] message = {
                "Start Time (HH:mm):", startTimeField,
                "End Time (HH:mm):", endTimeField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Log Hours", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                LocalTime startTime = LocalTime.parse(startTimeField.getText().trim());
                LocalTime endTime = LocalTime.parse(endTimeField.getText().trim());
                long hoursWorked = Duration.between(startTime, endTime).toHours();
                volunteer.addHours(hoursWorked);
                updateTable();
                JOptionPane.showMessageDialog(null, "Thanks for volunteering at Lopez Urban Farm today for " + hoursWorked + " hours, " + volunteer.getName() + "!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid time format! Use HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void updateTable() {
        tableModel.setRowCount(0); // Clear existing data
        for (Volunteer volunteer : volunteers.values()) {
            tableModel.addRow(new Object[]{volunteer.getName(), volunteer.getEmail(), volunteer.getPhone(), volunteer.getTotalHours()});
        }
    }

    private static void loadVolunteersFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No existing log found. Starting fresh.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 4) continue;

                String name = fields[0];
                String email = fields[1];
                String phone = fields[2];
                long totalHours = Long.parseLong(fields[3].trim());

                volunteers.put(email, new Volunteer(name, email, phone, totalHours));
            }
            System.out.println("Volunteer data loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void saveVolunteersToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Volunteer volunteer : volunteers.values()) {
                pw.println(volunteer.toCSV());
            }
            JOptionPane.showMessageDialog(null, "Volunteer log saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Volunteer class
    static class Volunteer {
        private final String name;
        private final String email;
        private final String phone;
        private long totalHours;

        public Volunteer(String name, String email, String phone) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.totalHours = 0;
        }

        public Volunteer(String name, String email, String phone, long hours) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.totalHours = hours;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public long getTotalHours() {
            return totalHours;
        }

        public void addHours(long hours) {
            this.totalHours += hours;
        }

        public String toCSV() {
            return name + ", " + email + ", " + phone + ", " + totalHours;
        }
    }
}
