package by.adamovich.lw16_db;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ContactInfoActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        nameInfo_et = findViewById(R.id.nameInfoET);
        phoneInfo_et = findViewById(R.id.phoneInfoET);

        Bundle arguments = getIntent().getExtras();
        contact = (ContactEntity) arguments.get("Contact");
        nameInfo_et.setText(contact.Name);
        phoneInfo_et.setText(contact.Phone);
    }
    public EditText nameInfo_et, phoneInfo_et;
    public ContactEntity contact;

    public void DeleteContact_Btn(View view)
    {
        ArrayList ops = new ArrayList();
        ContentResolver cr = getContentResolver();

        ops.add(ContentProviderOperation
                .newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{ contact.ID })
                .build());

        try {
            cr.applyBatch(ContactsContract.AUTHORITY, ops);
            ops.clear();
            this.finish();
        }
        catch (OperationApplicationException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        catch (RemoteException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void UpdateContact_Btn(View view)
    {
        try {
            if(nameInfo_et.getText().toString().length() < 2){
                Toast.makeText(this, "Введите имя длинее 2 символов", Toast.LENGTH_LONG).show();
                return;
            }
            if(phoneInfo_et.getText().toString().length() < 7){
                Toast.makeText(this, "Введите номер длиной больше 6 цифр", Toast.LENGTH_LONG).show();
                return;
            }

            ArrayList ops = new ArrayList();
            ContentResolver cr = getContentResolver();

            ops.add(ContentProviderOperation
                    .newDelete(ContactsContract.RawContacts.CONTENT_URI)
                    .withSelection(ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{ contact.ID })
                    .build());

            try {
                cr.applyBatch(ContactsContract.AUTHORITY, ops);
                ops.clear();
                this.finish();
            }
            catch (OperationApplicationException e) {
                e.printStackTrace();
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }

            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());

            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, nameInfo_et.getText().toString())
                    .build());

            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneInfo_et.getText().toString())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());

            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            this.finish();
            Toast.makeText(this, "Обновлено!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
