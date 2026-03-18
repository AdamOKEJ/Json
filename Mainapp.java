import com.fasterxml.jackson.databind.ObjectMapper;
import javax.swing.*;
import java.awt.*;
import java.io.File;

class UserProfile {
    private String imie;
    private String nazwisko;
    private String email;

    public UserProfile() {}

    public UserProfile(String imie, String nazwisko, String email) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
    }

    public String getImie() { return imie; }
    public void setImie(String imie) { this.imie = imie; }
    public String getNazwisko() { return nazwisko; }
    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

class StorageManager {
    public static void save(UserProfile p) {
        try {
            new ObjectMapper().writeValue(new File("profile.json"), p);
        } catch (Exception e) {}
    }

    public static UserProfile load() {
        try {
            return new ObjectMapper().readValue(new File("profile.json"), UserProfile.class);
        } catch (Exception e) {
            return new UserProfile("brak", "brak", "brak");
        }
    }
}

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("profil uzytkownika");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);

            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);

            JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
            JTextField imieField = new JTextField();
            JTextField nazwiskoField = new JTextField();
            JTextField emailField = new JTextField();
            JButton saveButton = new JButton("zapisz i dalej");

            formPanel.add(new JLabel("imie:"));
            formPanel.add(imieField);
            formPanel.add(new JLabel("nazwisko:"));
            formPanel.add(nazwiskoField);
            formPanel.add(new JLabel("email:"));
            formPanel.add(emailField);
            formPanel.add(new JLabel(""));
            formPanel.add(saveButton);

            JPanel viewPanel = new JPanel(new GridLayout(4, 1, 5, 5));
            JLabel imieLabel = new JLabel();
            JLabel nazwiskoLabel = new JLabel();
            JLabel emailLabel = new JLabel();
            JButton backButton = new JButton("powrot do edycji");

            viewPanel.add(imieLabel);
            viewPanel.add(nazwiskoLabel);
            viewPanel.add(emailLabel);
            viewPanel.add(backButton);

            mainPanel.add(formPanel, "form");
            mainPanel.add(viewPanel, "view");

            saveButton.addActionListener(e -> {
                UserProfile profile = new UserProfile(imieField.getText(), nazwiskoField.getText(), emailField.getText());
                StorageManager.save(profile);

                UserProfile loaded = StorageManager.load();
                imieLabel.setText("wczytane imie: " + loaded.getImie());
                nazwiskoLabel.setText("wczytane nazwisko: " + loaded.getNazwisko());
                emailLabel.setText("wczytany email: " + loaded.getEmail());

                cardLayout.show(mainPanel, "view");
            });

            backButton.addActionListener(e -> {
                cardLayout.show(mainPanel, "form");
            });

            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }
}
