package com.example.myaidkit.ui.notifications;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.myaidkit.Description;
import com.example.myaidkit.Medicine;
import com.example.myaidkit.R;
import com.example.myaidkit.Reminder;
import com.example.myaidkit.ReminderAdapter;

import java.io.File;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class NotificationsFragment extends Fragment {
    SQLiteDatabase mydatabase;

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        Button button = root.findViewById(R.id.NotifyAdd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), Notification.class);
                startActivityForResult(intent, 1);
            }
        });

        Reminder[] reminders = makeArray();
        ReminderAdapter adapter = new ReminderAdapter(this.requireActivity(), reminders);
        ListView lv = root.findViewById(R.id.NotifyList);
        lv.setAdapter(adapter);


        return root;
    }

    Reminder[] makeArray(){
        File dbpath = requireContext().getDatabasePath("medicines");
        if (!Objects.requireNonNull(dbpath.getParentFile()).exists()) {
            dbpath.getParentFile().mkdirs();
        }
        mydatabase = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        Cursor cursor = mydatabase.rawQuery("SELECT Name, Quantity, Time FROM Notifications;", null);
        int count = cursor.getCount();
        String[] names = new String[count];
        Float[] qantities = new Float[count];
        String[] times = new String[count];
        cursor.moveToFirst();
        for(int i = 0; i < count; i++){
            names[i] = cursor.getString(0);
            qantities[i] = cursor.getFloat(1);
            times[i] = cursor.getString(2);
            cursor.moveToNext();
        }
        cursor.close();
        Reminder[] reminders = new Reminder[count];
        Reminder reminder;
        for(int i = 0; i < reminders.length; i++){
            reminder = new Reminder(names[i], qantities[i], times[i]);
            reminders[i] = reminder;
        }
        return reminders;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (resultCode == RESULT_OK){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false);
            }
            ft.detach(this).attach(this).commit();
        }
    }
}