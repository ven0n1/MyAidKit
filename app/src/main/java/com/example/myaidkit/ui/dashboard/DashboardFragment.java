package com.example.myaidkit.ui.dashboard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import com.example.myaidkit.Constants;
import com.example.myaidkit.Description;
import com.example.myaidkit.entity.Medicine;
import com.example.myaidkit.adapter.MedicineAdapter;
import com.example.myaidkit.dao.MedicineDao;
import com.example.myaidkit.AppDatabase;
import com.example.myaidkit.R;

import static android.app.Activity.RESULT_OK;

public class DashboardFragment extends Fragment {
    AppDatabase appDatabase;
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
                appDatabase = Room.databaseBuilder(requireContext(), AppDatabase.class, "medicine").build();
                medicineDao = appDatabase.medicineDao();
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
            intent.putExtra(Constants.NAME, adapter.getItem(position).getName());
            intent.putExtra(Constants.LINK, adapter.getItem(position).getLink());
            intent.putExtra(Constants.FORM, adapter.getItem(position).getForm());
            intent.putExtra(Constants.TYPE, Constants.HOME);
            startActivityForResult(intent, 1);
        });

        return root;
    }

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
