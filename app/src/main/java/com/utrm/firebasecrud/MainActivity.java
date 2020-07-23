package com.utrm.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utrm.firebasecrud.model.Persona;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private List<Persona> listPerson = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;
    EditText nombreP, appP, correoP, passwordP;
    ListView lisV_personas;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Persona personaSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreP = findViewById(R.id.txt_nombrePersona);
        appP = findViewById(R.id.txt_appPersona);
        correoP = findViewById(R.id.txt_correoPersona);
        passwordP = findViewById(R.id.txt_passwordPersona);
        lisV_personas = findViewById(R.id.lv_datosPersonas);
        inicializarFirebase();
        listarDatos();
        lisV_personas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personaSelected = (Persona) parent.getItemAtPosition(position);
                nombreP.setText(personaSelected.getNombre());
                appP.setText(personaSelected.getApellidos());
                correoP.setText(personaSelected.getCorreo());
                passwordP.setText(personaSelected.getPassword());
            }
        });
    }

    private void listarDatos() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            listPerson.clear();
            for (DataSnapshot objSnapshot : snapshot.getChildren()){
                Persona p = objSnapshot.getValue(Persona.class);
                listPerson.add(p);

                arrayAdapterPersona= new ArrayAdapter<Persona>(MainActivity.this,android.R.layout.simple_list_item_1,listPerson);
                lisV_personas.setAdapter(arrayAdapterPersona);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String nombre = nombreP.getText().toString();
        String apellidos =appP.getText().toString();
        String correo= correoP.getText().toString();
        String password = passwordP.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:
                if (nombre.equals("") || apellidos.equals("")||correo.equals("")||password.equals("")){
                    validacion();
                }
                else {
                    Persona p = new Persona();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setApellidos(apellidos);
                    p.setCorreo(correo);
                    p.setPassword(password);
                    databaseReference.child("Persona").child(p.getUid()).setValue(p);
                    Toast.makeText(this, "Agregar", Toast.LENGTH_LONG).show();
                    CleanInput();

                }
                break;
            case R.id.icon_save:{
                Persona p = new Persona();
                p.setUid(personaSelected.getUid());
                p.setNombre(nombreP.getText().toString().trim());
                p.setApellidos(appP.getText().toString().trim());
                p.setCorreo(correoP.getText().toString().trim());
                p.setPassword(passwordP.getText().toString().trim());
                databaseReference.child("Persona").child(p.getUid()).setValue(p);
                Toast.makeText(this,"Guardar",Toast.LENGTH_LONG).show();
                CleanInput();
                break;
            }
            case R.id.icon_delete:
                Persona p = new Persona();
                p.setUid(personaSelected.getUid());
                databaseReference.child("Persona").child(p.getUid()).removeValue();
                Toast.makeText(this,"Eliminar",Toast.LENGTH_LONG).show();
                CleanInput();
                break;
            default:break;




        }
        return true;
    }

    private void CleanInput() {
        nombreP.setText("");
        appP.setText("");
        correoP.setText("");
        passwordP.setText("");

    }

    private void validacion() {
       String nombre = nombreP.getText().toString();
       String apellidos =appP.getText().toString();
       String correo= correoP.getText().toString();
       String password = passwordP.getText().toString();

       if (nombre.equals("")){
           nombreP.setError("Required");
       }
       else if(apellidos.equals("")){
           appP.setError("Required");
        }
       else if(correo.equals("")){
           correoP.setError("Required");
       }
       else if(password.equals("")){
           passwordP.setError("Required");
       }

    }
}