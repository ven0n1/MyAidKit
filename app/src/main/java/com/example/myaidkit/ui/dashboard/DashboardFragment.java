package com.example.myaidkit.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.myaidkit.Description;
import com.example.myaidkit.Medicine;
import com.example.myaidkit.MedicineAdapter;
import com.example.myaidkit.MedicineDao;
import com.example.myaidkit.MedicineDatabase;
import com.example.myaidkit.R;

import java.io.File;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class DashboardFragment extends Fragment {
    final String NAME = "name";
    final String LINK = "link";
    final String FORM = "form";
    final String TYPE = "type";
    final int HOME = 2;
    SQLiteDatabase mydatabase;
    MedicineDatabase medicineDatabase;
    MedicineDao medicineDao;
    Medicine medicine;
    Medicine[] medicines = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ListView lv = root.findViewById(R.id.HomeList);
        Thread thread = new Thread(){
            @Override
            public void run() {
                medicineDatabase = Room.databaseBuilder(requireContext(), MedicineDatabase.class, "medicine").build();
                medicineDao = medicineDatabase.medicineDao();
                medicines = medicineDao.getAll().toArray(new Medicine[0]);
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MedicineAdapter adapter = new MedicineAdapter(requireContext(), medicines);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(root.getContext(), Description.class);
            intent.putExtra(NAME, adapter.getItem(position).getName());
            intent.putExtra(LINK, adapter.getItem(position).getLink());
            intent.putExtra(FORM, adapter.getItem(position).getForm());
            intent.putExtra(TYPE, HOME);
            startActivityForResult(intent, 1);
        });

        return root;
    }

//    Medicine[] makeArray(){
//        File dbpath = requireContext().getDatabasePath("medicines");
//        if (!Objects.requireNonNull(dbpath.getParentFile()).exists()) {
//            dbpath.getParentFile().mkdirs();
//        }
//        mydatabase = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
//        Cursor cursor = mydatabase.rawQuery("SELECT Name, Link, Form FROM Description;", null);
//        int count = cursor.getCount();
//        String[] names = new String[count];
//        String[] links = new String[count];
//        String[] forms = new String[count];
//        cursor.moveToFirst();
//        for(int i = 0; i < count; i++){
//            names[i] = cursor.getString(0);
//            links[i] = cursor.getString(1);
//            forms[i] = cursor.getString(2);
//            cursor.moveToNext();
//        }
//        cursor.close();
//        Medicine[] medicines = new Medicine[count];
//        Medicine medicine;
//        for(int i = 0; i < medicines.length; i++){
//            medicine = new Medicine(names[i], links[i], forms[i]);
//            medicines[i] = medicine;
//        }
//        return medicines;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (resultCode == RESULT_OK){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false);
            }
            ft.detach(this);
            ft.attach(this);
            ft.commit();
        }
    }

}
