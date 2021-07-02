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

import com.example.myaidkit.R;
import com.example.myaidkit.Reminder;
import com.example.myaidkit.ReminderAdapter;

import java.io.File;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class NotificationsFragment extends Fragment {
    SQLiteDatabase mydatabase;
    final String TYPE = "type";
    final String ID = "id";
    final String NAME = "name";
    final String QUANTITY = "quantity";
    final String TIME = "time";
    final int ADD = 1;
    final int DELETE = 2;
    final String TAG = "delete";

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
        File dbpath = requireContext().getDatabasePath("medicines");
        if (!Objects.requireNonNull(dbpath.getParentFile()).exists()) {
            dbpath.getParentFile().mkdirs();
        }
        mydatabase = SQLiteDatabase.openOrCreateDatabase(dbpath,null);

        Reminder[] reminders = makeArray();
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
//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Toast.makeText(getContext(), Integer.toString(adapter.getItem(position).getId()), Toast.LENGTH_LONG).show();
//                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//                dialog.setTitle("Удаление напоминания");
//                dialog.setMessage("Вы хотите удалить напоминание?");
//                dialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                dialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        mydatabase.delete("Notifications", "ID = " + adapter.getItem(position).getId(), null);
//                    }
//                });
//                dialog.show();
//                return true;
//            }
//        });

        return root;
    }

    Reminder[] makeArray(){
        Cursor cursor = mydatabase.rawQuery("SELECT ID, Name, Quantity, Time FROM Notifications;", null);
        int count = cursor.getCount();
        int[] ids = new int[count];
        String[] names = new String[count];
        Float[] qantities = new Float[count];
        String[] times = new String[count];
        cursor.moveToFirst();
        for(int i = 0; i < count; i++){
            ids[i] = cursor.getInt(0);
            names[i] = cursor.getString(1);
            qantities[i] = cursor.getFloat(2);
            times[i] = cursor.getString(3);
            cursor.moveToNext();
        }
        cursor.close();
        Reminder[] reminders = new Reminder[count];
        Reminder reminder;
        for(int i = 0; i < reminders.length; i++){
            reminder = new Reminder(ids[i], names[i], qantities[i], times[i]);
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