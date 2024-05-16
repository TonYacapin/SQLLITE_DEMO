package com.example.cc10624demosqllite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class studentlist extends AppCompatActivity {

    ListView lsvStudent;
    Button btnEdit, btnBack, btnSave, btnDelete;
    EditText editTextName, editTextLocation, editTextCourse;

    private DBHandler dbhandler;

    int selectedStudentId = -1; // To store the ID of the selected student

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlist);

        dbhandler = new DBHandler(this);

        lsvStudent = findViewById(R.id.lsvstudents);
        btnEdit = findViewById(R.id.btnEdit);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        editTextName = findViewById(R.id.editTextName);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextCourse = findViewById(R.id.editTextCourse);
        btnDelete = findViewById(R.id.btnDelete);

        // Load student list
        loadStudentList();

        // Set item click listener for the ListView
        lsvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the ID of the selected student
                selectedStudentId = position + 1; // Assuming position corresponds to the ID
                // Show edit fields
                showEditFields(selectedStudentId);
            }
        });

        // Set click listener for the Edit button
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStudentId != -1) {
                    // Show edit fields
                    showEditFields(selectedStudentId);
                } else {
                    Toast.makeText(studentlist.this, "Please select a student to edit.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set click listener for the Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if edit fields are visible
                if (editTextName.getVisibility() == View.VISIBLE ||
                        editTextLocation.getVisibility() == View.VISIBLE ||
                        editTextCourse.getVisibility() == View.VISIBLE) {
                    // Hide edit fields
                    hideEditFields();
                } else {
                    // Navigate back to main activity
                    finish();
                }
            }
        });

        // Set click listener for the Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        // Set click listener for the Delete button
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForDelete("Warning", "Are you sure you want to delete?", selectedStudentId);
            }
        });
    }

    // Method to load the student list
    private void loadStudentList() {
        ArrayList<String> studList = dbhandler.readStudent();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(studentlist.this, android.R.layout.simple_list_item_1, studList);
        lsvStudent.setAdapter(adapter);
    }

    // Method to show edit fields and populate with student details
    private void showEditFields(int studentId) {
        ArrayList<String> studentDetails = dbhandler.getStudentDetails(studentId);
        if (studentDetails != null && studentDetails.size() == 3) {
            editTextName.setText(studentDetails.get(0));
            editTextLocation.setText(studentDetails.get(1));
            editTextCourse.setText(studentDetails.get(2));
            // Show edit fields
            editTextName.setVisibility(View.VISIBLE);
            editTextLocation.setVisibility(View.VISIBLE);
            editTextCourse.setVisibility(View.VISIBLE);
            // Hide ListView, Edit button, and show Save button
            lsvStudent.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        }
    }

    // Method to hide edit fields
    private void hideEditFields() {
        // Hide edit fields
        editTextName.setVisibility(View.GONE);
        editTextLocation.setVisibility(View.GONE);
        editTextCourse.setVisibility(View.GONE);
        // Show ListView and Edit button, hide Save button
        lsvStudent.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
    }

    private void showAlertForDelete(String title, String message, final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbhandler.deleteStudent(id);
                    // Refresh the student list
                    hideEditFields();
                    loadStudentList();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Method to save changes
    private void saveChanges() {
        String newName = editTextName.getText().toString().trim();
        String newLocation = editTextLocation.getText().toString().trim();
        String newCourse = editTextCourse.getText().toString().trim();

        if (newName.isEmpty() || newLocation.isEmpty() || newCourse.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isSuccess = dbhandler.updateStudentInfo(selectedStudentId, newName, newLocation, newCourse);
        if (isSuccess) {
            Toast.makeText(this, "Changes saved successfully.", Toast.LENGTH_SHORT).show();
            // Hide edit fields and show ListView and Edit button
            hideEditFields();
            // Refresh the student list
            loadStudentList();
        } else {
            Toast.makeText(this, "Failed to save changes.", Toast.LENGTH_SHORT).show();
        }
    }
}
