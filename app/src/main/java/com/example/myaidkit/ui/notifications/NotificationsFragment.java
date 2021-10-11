package com.example.myaidkit.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.myaidkit.AppDatabase;
import com.example.myaidkit.Medicine;
import com.example.myaidkit.R;
import com.example.myaidkit.Reminder;
import com.example.myaidkit.ReminderAdapter;
import com.example.myaidkit.ReminderDao;

import java.io.File;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class NotificationsFragment extends Fragment {
    SQLiteDatabase mydatabase;
    com.allyants.notifyme.Notification.NotificationDBHelper notifyDB;
    final String TYPE = "type";
    final String ID = "id";
    final String NAME = "name";
    final String QUANTITY = "quantity";
    final String TIME = "time";
    final int ADD = 1;
    final int DELETE = 2;
    final String TAG = "delete";
    AppDatabase appDatabase;
    ReminderDao reminderDao;
    Reminder reminder;
    Reminder[] reminders = null;

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
                intent.putExtra(TYPE, ADD);
                startActivityForResult(intent, 1);
            }
        });
        notifyDB = new com.allyants.notifyme.Notification.NotificationDBHelper(getContext());
        mydatabase = notifyDB.getReadableDatabase();

//        Reminder[] reminders = makeArray();
        Thread thread = new Thread(){
            @Override
            public void run() {
                appDatabase = Room.databaseBuilder(requireContext(), AppDatabase.class, "medicine").build();
                reminderDao = appDatabase.reminderDao();
                reminders = reminderDao.getAll().toArray(new Reminder[0]);
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ReminderAdapter adapter = new ReminderAdapter(this.requireActivity(), reminders);
        ListView lv = root.findViewById(R.id.NotifyList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(root.getContext(), Notification.class);
            intent.putExtra(TYPE, DELETE);
            intent.putExtra(ID, adapter.getItem(position).getId());
            intent.putExtra(NAME, adapter.getItem(position).getName());
            intent.putExtra(QUANTITY, adapter.getItem(position).getQuantity());
            intent.putExtra(TIME, adapter.getItem(position).getTime());
            startActivityForResult(intent, 2);
        });

        return root;
    }

    Reminder[] makeArray(){
        Cursor cursor = mydatabase.rawQuery("SELECT _id, time, content FROM notification;", null);
        int count = cursor.getCount();
        int[] ids = new int[count];
        String[] names = new String[count];
        Float[] quantities = new Float[count];
        Long[] times = new Long[count];
        cursor.moveToFirst();
        for(int i = 0; i < count; i++){
            ids[i] = cursor.getInt(0);
            times[i] = cursor.getLong(1);
            String[] ss = cursor.getString(2).substring(9).split(" в объеме: ");
            names[i] = ss[0];
            quantities[i] = ss.length == 2 ? Float.valueOf(ss[1]) : Float.valueOf(0);
            cursor.moveToNext();
        }
        cursor.close();
        Reminder[] reminders = new Reminder[count];
        Reminder reminder;
        for(int i = 0; i < reminders.length; i++){
            reminder = new Reminder(ids[i], names[i], quantities[i], times[i]);
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