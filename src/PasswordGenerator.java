/*
 * Secure Password Generator
 * Author: Sunny Patel
 * Email: sunnypatel124555@gmail.com
 * Date: October 3, 2023
 * 
 * This code is the property of Sunny Patel and may not be copied or distributed
 * without permission.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.regex.*;

public class PasswordGenerator extends JFrame {
    private JButton generateButton;
    private JButton saveButton;
    private JButton copyButton;
    private JTextField passwordField;
    private JLabel strengthLabel;
    private JSlider lengthSlider;
    private JCheckBox useUppercase;
    private JCheckBox useLowercase;
    private JCheckBox useDigits;
    private JCheckBox useSpecialChars;

    public PasswordGenerator() {
        setTitle("Secure Password Generator");
        setSize(600, 450); // Increased height for a more balanced appearance
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Define Discord-inspired colors
        Color discordGray = new Color(32, 34, 37);
        Color discordGreen = new Color(67, 181, 129);
        Color discordTextGray = new Color(153, 170, 181);

        generateButton = new JButton("Generate Password");
        generateButton.setBackground(discordGreen);
        generateButton.setForeground(Color.WHITE);
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generatePassword();
            }
        });

        saveButton = new JButton("Save Password as .txt");
        saveButton.setBackground(discordGreen);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                savePasswordAsTxt();
            }
        });

        copyButton = new JButton("Copy to Clipboard");
        copyButton.setBackground(discordGreen);
        copyButton.setForeground(Color.WHITE);
        copyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copyToClipboard();
            }
        });

        passwordField = new JTextField();
        passwordField.setEditable(false);
        passwordField.setBackground(discordGray);
        passwordField.setForeground(Color.WHITE);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18)); // Increased font size

        strengthLabel = new JLabel("Password Strength: ");
        strengthLabel.setForeground(discordTextGray);
        strengthLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Increased font size

        lengthSlider = new JSlider(6, 20, 12);
        lengthSlider.setMajorTickSpacing(2);
        lengthSlider.setMinorTickSpacing(1);
        lengthSlider.setPaintTicks(true);
        lengthSlider.setSnapToTicks(true);
        lengthSlider.setBackground(discordGray);

        useUppercase = new JCheckBox("Include Uppercase Letters");
        useLowercase = new JCheckBox("Include Lowercase Letters");
        useDigits = new JCheckBox("Include Digits");
        useSpecialChars = new JCheckBox("Include Special Characters");

        // Set background color for option checkboxes
        useUppercase.setBackground(discordGray);
        useLowercase.setBackground(discordGray);
        useDigits.setBackground(discordGray);
        useSpecialChars.setBackground(discordGray);

        // Set text color for checkbox labels
        useUppercase.setForeground(Color.WHITE);
        useLowercase.setForeground(Color.WHITE);
        useDigits.setForeground(Color.WHITE);
        useSpecialChars.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(generateButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(copyButton);

        JPanel optionsPanel = new JPanel(new GridLayout(5, 1));
        optionsPanel.add(useUppercase);
        optionsPanel.add(useLowercase);
        optionsPanel.add(useDigits);
        optionsPanel.add(useSpecialChars);

        // Add a label below the checkboxes
        JLabel separatorLabel = new JLabel("Generated Password Below");
        separatorLabel.setForeground(discordTextGray);
        separatorLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(discordGray);
        mainPanel.add(passwordField, BorderLayout.CENTER);
        mainPanel.add(strengthLabel, BorderLayout.SOUTH);

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(discordGray);
        sidePanel.add(lengthSlider, BorderLayout.CENTER);
        sidePanel.add(optionsPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(discordGray);
        topPanel.add(buttonPanel, BorderLayout.NORTH);
        topPanel.add(sidePanel, BorderLayout.CENTER);

        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(discordGray);
        backgroundPanel.add(topPanel, BorderLayout.NORTH);
        backgroundPanel.add(separatorLabel, BorderLayout.CENTER); // Add separator label
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        getContentPane().add(backgroundPanel);

        // About the Developer section
        JPanel developerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        developerPanel.setBackground(discordGray);
        JLabel developerLabel = new JLabel("About the Developer: ");
        developerLabel.setForeground(discordTextGray);
        developerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel linkedInLabel = new JLabel(
                "<html><a href=\"https://www.linkedin.com/in/sunny-patel-30b460204/\">LinkedIn</a></html>");
        linkedInLabel.setForeground(Color.BLUE);
        linkedInLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkedInLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openLinkedInProfile();
            }
        });
        developerPanel.add(developerLabel);
        developerPanel.add(linkedInLabel);
        backgroundPanel.add(developerPanel, BorderLayout.SOUTH);
    }

    private void generatePassword() {
        String generatedPassword = generateSecurePassword(lengthSlider.getValue(),
                useUppercase.isSelected(),
                useLowercase.isSelected(),
                useDigits.isSelected(),
                useSpecialChars.isSelected());

        passwordField.setText(generatedPassword);
        updatePasswordStrength(generatedPassword);
    }

    private String generateSecurePassword(int length, boolean useUppercase, boolean useLowercase,
            boolean useDigits, boolean useSpecialChars) {
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_+=<>?";

        String allChars = "";
        if (useUppercase) {
            allChars += uppercaseChars;
        }
        if (useLowercase) {
            allChars += lowercaseChars;
        }
        if (useDigits) {
            allChars += digits;
        }
        if (useSpecialChars) {
            allChars += specialChars;
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allChars.length());
            password.append(allChars.charAt(randomIndex));
        }

        return password.toString();
    }

    private void updatePasswordStrength(String password) {
        int strength = calculatePasswordStrength(password);
        if (strength < 30) {
            strengthLabel.setText("Password Strength: Weak");
            strengthLabel.setForeground(Color.RED);
        } else if (strength < 60) {
            strengthLabel.setText("Password Strength: Moderate");
            strengthLabel.setForeground(Color.ORANGE);
        } else if (strength < 80) {
            strengthLabel.setText("Password Strength: Strong");
            strengthLabel.setForeground(new Color(0, 128, 0)); // Dark green for strong
        } else {
            strengthLabel.setText("Password Strength: Very Strong");
            strengthLabel.setForeground(new Color(0, 255, 0)); // Greener for very strong
        }
    }

    private int calculatePasswordStrength(String password) {
        int length = password.length();
        int uppercaseCount = password.replaceAll("[^A-Z]", "").length();
        int lowercaseCount = password.replaceAll("[^a-z]", "").length();
        int digitCount = password.replaceAll("[^0-9]", "").length();
        int specialCharCount = length - (uppercaseCount + lowercaseCount + digitCount);

        int strength = length * 4;
        strength += (length - uppercaseCount) * 2;
        strength += (length - lowercaseCount) * 2;
        strength += digitCount * 4;
        strength += specialCharCount * 6;

        return strength;
    }

    private void savePasswordAsTxt() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".txt")) {
                file = new File(file.getParentFile(), file.getName() + ".txt");
            }
            String password = passwordField.getText();

            try {
                FileWriter writer = new FileWriter(file);
                writer.write(password);
                writer.close();
                JOptionPane.showMessageDialog(this, "Password saved as .txt successfully.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving password.");
            }
        }
    }

    private void copyToClipboard() {
        StringSelection selection = new StringSelection(passwordField.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        JOptionPane.showMessageDialog(this, "Password copied to clipboard.");
    }

    private void openLinkedInProfile() {
        try {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                URI uri = new URI("https://www.linkedin.com/in/sunny-patel-30b460204/");
                desktop.browse(uri);
            } else {
                JOptionPane.showMessageDialog(this, "Unable to open the LinkedIn profile.");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PasswordGenerator passwordGenerator = new PasswordGenerator();
            passwordGenerator.setVisible(true);
        });
    }
}
