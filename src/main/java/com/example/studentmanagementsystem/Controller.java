package com.example.studentmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class Controller {

    @FXML private TextField txtName;
    @FXML private TextField txtCourse;
    @FXML private ChoiceBox<YearLevel> cbYear;
    @FXML private TableView<Student> table;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colCourse;
    @FXML private TableColumn<Student, String> colYear;

    private final ObservableList<Student> list = FXCollections.observableArrayList();
    private Connection conn;
    private int selectedId = -1;

    @FXML
    public void initialize() {
        conn = dbconnection.connect();

        cbYear.getItems().setAll(YearLevel.values());

        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colCourse.setCellValueFactory(data -> data.getValue().courseProperty());
        colYear.setCellValueFactory(data -> data.getValue().yearLevelProperty());

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedStudent) -> {
            if (selectedStudent != null) {
                selectedId = selectedStudent.getId();
                txtName.setText(selectedStudent.getName());
                txtCourse.setText(selectedStudent.getCourse());
                setChoiceBoxValue(selectedStudent.getYearLevel());
            }
        });

        loadData();
    }

    private void loadData() {
        list.clear();

        if (conn == null) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Cannot connect to PostgreSQL. Check your database settings.");
            table.setItems(list);
            return;
        }

        String query = "SELECT id, name, course, year_level FROM students ORDER BY id";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("year_level")
                ));
            }

            table.setItems(list);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Could not load student records.");
            e.printStackTrace();
        }
    }

    @FXML
    private void addStudent() {
        if (!isInputValid()) {
            return;
        }

        String query = "INSERT INTO students(name, course, year_level) VALUES (?, ?, ?)";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setString(3, cbYear.getValue().toString());
            pst.executeUpdate();

            loadData();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student record added successfully.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Add Error", "Could not add student record.");
            e.printStackTrace();
        }
    }

    @FXML
    private void updateStudent() {
        if (selectedId == -1) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student record to update.");
            return;
        }

        if (!isInputValid()) {
            return;
        }

        String query = "UPDATE students SET name = ?, course = ?, year_level = ? WHERE id = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setString(3, cbYear.getValue().toString());
            pst.setInt(4, selectedId);
            pst.executeUpdate();

            loadData();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student record updated successfully.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Error", "Could not update student record.");
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteStudent() {
        if (selectedId == -1) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student record to delete.");
            return;
        }

        Optional<ButtonType> result = showConfirmation("Delete Record", "Are you sure you want to delete the selected student record?");
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        String query = "DELETE FROM students WHERE id = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, selectedId);
            pst.executeUpdate();

            loadData();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student record deleted successfully.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Delete Error", "Could not delete student record.");
            e.printStackTrace();
        }
    }

    @FXML
    private void clearFields() {
        txtName.clear();
        txtCourse.clear();
        cbYear.setValue(null);
        table.getSelectionModel().clearSelection();
        selectedId = -1;
    }

    private boolean isInputValid() {
        if (conn == null) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "No database connection available.");
            return false;
        }

        if (txtName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Name is required.");
            txtName.requestFocus();
            return false;
        }

        if (txtCourse.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Course is required.");
            txtCourse.requestFocus();
            return false;
        }

        if (cbYear.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Year level is required.");
            cbYear.requestFocus();
            return false;
        }

        return true;
    }

    private void setChoiceBoxValue(String yearLevelText) {
        for (YearLevel yearLevel : YearLevel.values()) {
            if (yearLevel.toString().equals(yearLevelText)) {
                cbYear.setValue(yearLevel);
                return;
            }
        }

        cbYear.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}