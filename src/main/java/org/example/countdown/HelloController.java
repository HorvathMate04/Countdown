package org.example.countdown;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController {
    @FXML
    private TextField dateTimeField;

    @FXML
    private Label countdownLabel;

    private Timer timer;

    @FXML
    public void startCountdown() {
        String input = dateTimeField.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        try {
            LocalDateTime targetDateTime = LocalDateTime.parse(input, formatter);
            LocalDateTime now = LocalDateTime.now();
            if (targetDateTime.isBefore(now)) {
                showError("A megadott időpont a múltban van!");
                return;
            }

            timer = new Timer(true);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> updateCountdown(targetDateTime));
                }
            }, 0, 1000);

        } catch (DateTimeParseException e) {
            showError("Érvénytelen dátumformátum! Ez a helyes formátum: yyyy.MM.dd HH:mm:ss");
        }
    }

    private void updateCountdown(LocalDateTime targetDateTime) {
        LocalDateTime now = LocalDateTime.now();
        if (!now.isBefore(targetDateTime)) {
            timer.cancel();
            Platform.runLater(() -> {
                countdownLabel.setText("Lejárt az idő!");
                showAlert("Idő lejárt", "A visszaszámlálás véget ért.");
            });
            return;
        }

        Duration duration = Duration.between(now, targetDateTime);

        long years = duration.toDays() / 365;
        long months = (duration.toDays() % 365) / 30;
        long days = (duration.toDays() % 365) % 30;
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        countdownLabel.setText(
                String.format("%d év %d hó %d nap %02d:%02d:%02d", years, months, days, hours, minutes, seconds)
        );
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hiba");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

