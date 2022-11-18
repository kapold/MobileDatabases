package by.adamovich.lw16_db;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactName_et = findViewById(R.id.contactNameET);
        contactPhone_et = findViewById(R.id.contactPhoneET);
        searchText_et = findViewById(R.id.searchET);
        contactsLV = findViewById(R.id.contactsInfoLV);
        contact = new ContactEntity();
        contentResolver = getContentResolver();
        contacts = new ArrayList<ContactEntity>();

        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) // если устройство до API 23, устанавливаем разрешение
            READ_CONTACTS_GRANTED = true;
        else // вызываем диалоговое окно для установки разрешений
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        // если разрешение установлено, загружаем контакты
        if (READ_CONTACTS_GRANTED)
            LoadContacts();

        contactsLV.setOnItemClickListener((parent, v, position, id) -> {
            contact = contacts.get(position);
            ShowContactInfo();
        });

        searchText_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                    LoadContacts();
                else
                    SearchContactInfo(s.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
    }
    private static boolean READ_CONTACTS_GRANTED = false;
    private static final int REQUEST_CODE_READ_CONTACTS = 1;

    public ArrayList<ContactEntity> contacts;
    public ContactEntity contact;
    public ContentResolver contentResolver;

    public ListView contactsLV;
    public EditText contactName_et, contactPhone_et, searchText_et;

    // PERMISSION
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_CONTACTS)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                READ_CONTACTS_GRANTED = true;

        if (READ_CONTACTS_GRANTED)
            LoadContacts();
        else
            Toast.makeText(this, "Требуется установить разрешения", Toast.LENGTH_LONG).show();
    }

    public void AddNewContact_Btn(View view)
    {
        if(contactName_et.getText().toString().length() < 2){
            Toast.makeText(this, "Введите имя длинее 2 символов", Toast.LENGTH_LONG).show();
            return;
        }
        if(contactPhone_et.getText().toString().length() < 7){
            Toast.makeText(this, "Введите номер длиной больше 6 цифр", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList <ContentProviderOperation> ops = new ArrayList<>();

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactName_et.getText().toString())
                .build());

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactPhone_et.getText().toString())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        }
        catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        LoadContacts();
        Toast.makeText(this, "Контакт успешно добавлен!", Toast.LENGTH_SHORT).show();
        contactName_et.setText("");
        contactPhone_et.setText("");
    }

    public void LoadContacts()
    {
        try{
            contacts.clear();
            Cursor contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if (contactsCursor != null){
                while(contactsCursor.moveToNext()){
                    ContactEntity contactEntity = new ContactEntity();
                    String contactId = contactsCursor
                            .getString(contactsCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    contactEntity.ID = contactId;

                    String contactName = contactsCursor
                            .getString(contactsCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    contactEntity.Name = contactName;

                    int hasContactPhone = Integer.parseInt(contactsCursor
                            .getString(contactsCursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                    if (hasContactPhone > 0){
                        Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);

                        while (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(phoneCursor
                                    .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            contactEntity.Phone = phoneNumber;
                        }
                        phoneCursor.close();
                    }
                    contacts.add(contactEntity);
                }
                contactsCursor.close();
            }
            else{
                Toast.makeText(this, "ОШИБКА ЗАГРУЗКИ КОНТАКТОВ", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayAdapter<ContactEntity> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, contacts);
            contactsLV.setAdapter(adapter);
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void SearchContactInfo(String phone)
    {
        ArrayList<ContactEntity> foundContacts = new ArrayList<>();
        for(ContactEntity contact: contacts)
            if(contact.Phone.contains(phone))
                foundContacts.add(contact);

        ArrayAdapter<ContactEntity> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, foundContacts);
        contactsLV.setAdapter(adapter);
    }

    public void ShowContactInfo()
    {
        try{
            Intent infoIntent = new Intent(this, ContactInfoActivity.class);
            infoIntent.putExtra("Contact", contact);
            infoIntent.putExtra("Context", MainActivity.class);
            startActivity(infoIntent);
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}