package com.example.numad24fa_shaojiezhang;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

public class ContactsCollectorActivity extends AppCompatActivity implements ContactsAdapter.OnContactLongClickListener {

    private RecyclerView contactsRecyclerView;
    private ContactsAdapter contactsAdapter;
    private ArrayList<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_collector);

        contactList = new ArrayList<>();
        contactsRecyclerView = findViewById(R.id.contactsRecyclerView);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsAdapter = new ContactsAdapter(contactList, this); // Pass `this` as the listener
        contactsRecyclerView.setAdapter(contactsAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> openAddContactDialog());
    }

    @Override
    public void onContactLongClick(int position) {
        showEditDeleteOptionsDialog(position);
    }

    private void openAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Contact");

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Name");

        final EditText phoneInput = new EditText(this);
        phoneInput.setHint("Phone Number");
        phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(nameInput);
        layout.addView(phoneInput);
        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();

            if (!name.isEmpty() && !phone.isEmpty()) {
                Contact newContact = new Contact(name, phone);
                addContact(newContact);
                showSnackbar("Contact added", "Undo", v -> {
                    contactList.remove(newContact);
                    contactsAdapter.notifyDataSetChanged();
                });
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addContact(Contact contact) {
        contactList.add(contact);
        contactsAdapter.notifyDataSetChanged();
    }

    private void showEditDeleteOptionsDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit or Delete Contact")
                .setMessage("Do you want to edit or delete this contact?")
                .setPositiveButton("Edit", (dialog, which) -> showEditContactDialog(position))
                .setNegativeButton("Delete", (dialog, which) -> confirmDeleteContact(position))
                .show();
    }

    private void showEditContactDialog(int position) {
        Contact contact = contactList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Contact");

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Name");
        nameInput.setText(contact.getName());

        final EditText phoneInput = new EditText(this);
        phoneInput.setHint("Phone Number");
        phoneInput.setText(contact.getPhone());
        phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(nameInput);
        layout.addView(phoneInput);
        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = nameInput.getText().toString().trim();
            String newPhone = phoneInput.getText().toString().trim();

            if (!newName.isEmpty() && !newPhone.isEmpty()) {
                contact.setName(newName);
                contact.setPhone(newPhone);
                contactsAdapter.notifyItemChanged(position);
                showSnackbar("Contact updated", "Undo", v -> {
                    contact.setName(contact.getName()); // reset to previous name if undone
                    contact.setPhone(contact.getPhone());
                    contactsAdapter.notifyItemChanged(position);
                });
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void confirmDeleteContact(int position) {
        Contact removedContact = contactList.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this contact?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    contactList.remove(position);
                    contactsAdapter.notifyItemRemoved(position);
                    showSnackbar("Contact deleted", "Undo", v -> {
                        contactList.add(position, removedContact);
                        contactsAdapter.notifyItemInserted(position);
                    });
                })
                .setNegativeButton("No", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void showSnackbar(String message, String action, View.OnClickListener undoListener) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);
        snackbar.setAction(action, undoListener);
        snackbar.show();
    }
}
