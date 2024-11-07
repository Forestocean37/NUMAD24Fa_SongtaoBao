package com.example.numad24fa_songtaobao;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.numad24fa_songtaobao.model.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ContactsCollectorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_collector);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactList = new ArrayList<>();
        adapter = new ContactAdapter(contactList, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> openAddContactDialog());

        // Initialize swipe to delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private final ColorDrawable background = new ColorDrawable(Color.RED);
            private final Drawable deleteIcon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete);

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // no move support
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Get the position of the swiped item
                int position = viewHolder.getAdapterPosition();
                Contact removedContact = contactList.get(position);

                // Remove the item from the list
                contactList.remove(position);
                adapter.notifyItemRemoved(position);

                // Show Snackbar with Undo option
                Snackbar.make(recyclerView, "Contact deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            contactList.add(position, removedContact);
                            adapter.notifyItemInserted(position);
                        }).show();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20; // Slight offset for background corners

                if (dX < 0) { // Swiping to the left
                    background.setBounds(itemView.getRight() + (int) dX - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    background.draw(c);

                    // Set position and draw delete icon
                    int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                    int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                    int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();
                    int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;
                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    deleteIcon.draw(c);
                } else {
                    background.setBounds(0, 0, 0, 0); // No swipe in the opposite direction
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void openAddContactDialog() {
        // Create an AlertDialog Builder for the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Contact");

        // Create a LinearLayout to hold the input fields
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        // Input field for the contact's name
        final EditText nameInput = new EditText(this);
        nameInput.setHint("Enter Name");
        layout.addView(nameInput);

        // Input field for the contact's phone number
        final EditText phoneInput = new EditText(this);
        phoneInput.setHint("Enter Phone Number");
        phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);  // Set input type to phone
        layout.addView(phoneInput);

        // Attach the layout to the AlertDialog
        builder.setView(layout);

        // Configure the "Add" button to add a new contact
        builder.setPositiveButton("Add", (dialog, which) -> {
            // Retrieve the entered name and phone number
            String name = nameInput.getText().toString().trim();
            String phoneNumber = phoneInput.getText().toString().trim();

            // Check that both fields are filled
            if (!name.isEmpty() && !phoneNumber.isEmpty()) {
                // Create a new Contact and add it to the list
                Contact newContact = new Contact(name, phoneNumber);
                contactList.add(newContact);
                adapter.notifyItemInserted(contactList.size() - 1); // Notify the adapter

                // Show a Snackbar to confirm the addition
                Snackbar.make(recyclerView, "Contact added", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            // Undo the addition by removing the last added contact
                            contactList.remove(contactList.size() - 1);
                            adapter.notifyItemRemoved(contactList.size());
                        }).show();
            } else {
                // Display a Toast if any field is empty
                Toast.makeText(this, "Please enter both name and phone number", Toast.LENGTH_SHORT).show();
            }
        });

        // Configure the "Cancel" button to close the dialog
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the AlertDialog
        builder.show();
    }

    public void openEditContactDialog(Contact contact, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Contact");

        // Create a LinearLayout to hold the input fields
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        // Input field for Name (pre-filled)
        final EditText nameInput = new EditText(this);
        nameInput.setText(contact.getName());
        nameInput.setHint("Enter Name");
        layout.addView(nameInput);

        // Input field for Phone Number (pre-filled)
        final EditText phoneInput = new EditText(this);
        phoneInput.setText(contact.getPhoneNumber());
        phoneInput.setHint("Enter Phone Number");
        phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);
        layout.addView(phoneInput);

        builder.setView(layout);

        // "Save" button to confirm changes
        builder.setPositiveButton("Save", (dialog, which) -> {
            String updatedName = nameInput.getText().toString().trim();
            String updatedPhone = phoneInput.getText().toString().trim();

            if (!updatedName.isEmpty() && !updatedPhone.isEmpty()) {
                // Update the contact details
                contact.setName(updatedName);
                contact.setPhoneNumber(updatedPhone);

                // Notify the adapter of the change
                adapter.notifyItemChanged(position);
            } else {
                Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
            }
        });

        // "Cancel" button to dismiss the dialog
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Show the dialog
        builder.show();
    }

}
