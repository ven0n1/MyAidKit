package com.example.myaidkit.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myaidkit.Constants;
import com.example.myaidkit.Description;
import com.example.myaidkit.entity.Medicine;
import com.example.myaidkit.adapter.MedicineAdapter;
import com.example.myaidkit.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    Medicine[] medicines = {};
    MedicineAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ListView lv = root.findViewById(R.id.HomeList);
        Button button = root.findViewById(R.id.HomeButton);
        EditText search = root.findViewById(R.id.HomeSearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    medicines = new MyTask().execute(search.getText().toString()).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                adapter = new MedicineAdapter(requireContext(), medicines);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener((parent, root, position, id) -> {
                    Intent intent = new Intent(root.getContext(), Description.class);
                    intent.putExtra(Constants.NAME, adapter.getItem(position).getName());
                    intent.putExtra(Constants.LINK, adapter.getItem(position).getLink());
                    intent.putExtra(Constants.FORM, adapter.getItem(position).getForm());
                    intent.putExtra(Constants.TYPE, Constants.INTERNET);
                    startActivity(intent);
                });
            }
        });


        return root;
    }

}

class MyTask extends AsyncTask<String, Void, Medicine[]> {
    Document doc;
    @Override
    protected Medicine[] doInBackground(String... urls) {
        String url = urls[0];
        try {
            doc = Jsoup.connect("https://www.vidal.ru/search?t=product&q=" + url + "&bad=on").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] names = doc.select(".products-table-name .no-underline").eachText().toArray(new String[0]);
        String[] links = doc.select(".products-table-name .no-underline").eachAttr("href").toArray(new String[0]);
        String[] forms = doc.select(".products-table-zip .hyphenate").eachText().toArray(new String[0]);
        int min = Math.min(names.length, Math.min(links.length, forms.length));
        Medicine[] medicines = new Medicine[min];
        Medicine medicine;
        for(int i = 0; i < medicines.length; i++){
            medicine = new Medicine(names[i], links[i], forms[i]);
            medicines[i] = medicine;
        }
        return medicines;
    }

    @Override
    protected void onPostExecute(Medicine[] medicines) {
        super.onPostExecute(medicines);
    }
}