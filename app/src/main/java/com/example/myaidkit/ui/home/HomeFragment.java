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

import com.example.myaidkit.Description;
import com.example.myaidkit.Medicine;
import com.example.myaidkit.MedicineAdapter;
import com.example.myaidkit.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {
    final String NAME = "name";
    final String LINK = "link";
    final String FORM = "form";
    final String TYPE = "type";
    final int INTERNET = 1;
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
                    intent.putExtra(NAME, adapter.getItem(position).getName());
                    intent.putExtra(LINK, adapter.getItem(position).getLink());
                    intent.putExtra(FORM, adapter.getItem(position).getForm());
                    intent.putExtra(TYPE, INTERNET);
                    startActivity(intent);
                });
            }
        });


        return root;
    }

//    Medicine[] makeArray(){
//        String[] money = {"$86 B", "$75.6 B", "$72.8 B", "$71.3 B", "$56 B", "$54.5 B", "$52.2 B", "$48.3 B", "$48.3 B", "$47.5 B", "$41.5 B", "$40.7 B", "$39.8 B", "$39.5 B", "$34.1 B", "$34 B", "$33.8 B", "$31.3 B", "$31.2 B", "$30.4 B", "$30 B", "$29.2 B", "$28.3 B", "$27.2 B", "$27.2 B", "$27 B", "$27 B", "$26.2 B", "$25.2 B", "$25.2 B", "$24.9 B", "$24.4 B", "$23.2 B", "$21.2 B", "$21.1 B", "$20.7 B", "$20.5 B", "$20.4 B", "$20.4 B", "$20 B", "$20 B", "$19.9 B", "$19.6 B", "$18.8 B", "$18.7 B", "$18.4 B", "$18.3 B", "$18.3 B", "$18 B", "$17.9 B", "$17.5 B", "$17.3 B", "$17 B", "$16.8 B", "$16.6 B", "$16.4 B", "$16.1 B", "$16.1 B", "$16 B", "$15.9 B", "$15.9 B", "$15.8 B", "$15.7 B", "$15.4 B", "$15.3 B", "$15.2 B", "$15.2 B", "$15.1 B", "$15 B", "$15 B", "$15 B", "$14.9 B", "$14.8 B", "$14.5 B", "$14.4 B", "$14.4 B", "$14.3 B", "$14.3 B", "$14 B", "$13.9 B", "$13.9 B", "$13.8 B", "$13.8 B", "$13.7 B", "$13.7 B", "$13.4 B", "$13.3 B", "$13.3 B", "$13.2 B", "$13.1 B", "$13.1 B", "$13 B", "$13 B", "$12.7 B", "$12.6 B", "$12.5 B", "$12.5 B", "$12.5 B", "$12.5 B", "$12.4 B"};
//        String[] names = {"Bill Gates", "Warren Buffett", "Jeff Bezos", "Amancio Ortega", "Mark Zuckerberg", "Carlos Slim Helu", "Larry Ellison", "Charles Koch", "David Koch", "Michael Bloomberg", "Bernard Arnault", "Larry Page", "Sergey Brin", "Liliane Bettencourt", "S. Robson Walton", "Jim Walton", "Alice Walton", "Wang Jianlin", "Li Ka-shing", "Sheldon Adelson", "Steve Ballmer", "Jorge Paulo Lemann", "Jack Ma", "Beate Heister & Karl Albrecht Jr.", "David Thomson", "Jacqueline Mars", "John Mars", "Phil Knight", "Maria Franca Fissolo", "George Soros", "Ma Huateng", "Lee Shau Kee", "Mukesh Ambani", "Masayoshi Son", "Kjeld Kirk Kristiansen", "Georg Schaeffler", "Joseph Safra", "Michael Dell", "Susanne Klatten", "Len Blavatnik", "Laurene Powell Jobs", "Paul Allen", "Stefan Persson", "Theo Albrecht, Jr.", "Prince Alwaleed Bin Talal Alsaud", "Leonid Mikhelson", "Charles Ergen", "Stefan Quandt", "James Simons", "Leonardo Del Vecchio", "Alexey Mordashov", "William Ding", "Dieter Schwarz", "Ray Dalio", "Carl Icahn", "Lakshmi Mittal", "Serge Dassault", "Vladimir Lisin", "Gennady Timchenko", "Wang Wei", "Tadashi Yanai", "Charoen Sirivadhanabhakdi", "Francois Pinault", "Hinduja family", "David & Simon Reuben", "Donald Bren", "Alisher Usmanov", "Lee Kun-Hee", "Thomas & Raymond Kwok", "Joseph Lau", "Gina Rinehart", "Azim Premji", "Marcel Herrmann Telles", "Vagit Alekperov", "Mikhail Fridman", "Abigail Johnson", "Pallonji Mistry", "Vladimir Potanin", "Wang Wenyin", "Elon Musk", "Stefano Pessina", "German Larrea Mota Velasco", "Thomas Peterffy", "Iris Fontbona", "Dilip Shanghvi", "Dietrich Mateschitz", "Harold Hamm", "Robin Li", "Andrey Melnichenko", "Rupert Murdoch", "Heinz Hermann Thiele", "Steve Cohen", "Patrick Drahi", "Henry Sy", "Charlene de Carvalho-Heineken", "Philip Anschutz", "Ronald Perelman", "Hans Rausing", "Carlos Alberto Sicupira", "Klaus-Michael Kuehne"};
//        Medicine[] persons = new Medicine[100];
//        Medicine person;
//        for(int i = 0; i < persons.length; i++){
//            person = new Medicine(names[i],names[i], money[i]);
//            persons[i] = person;
//        }
//        return persons;
//    }
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
//        List<String> names = doc.select(".products-table-name .no-underline").eachText();
//        List<String> links = doc.select(".products-table-name .no-underline").eachAttr("href");
//        List<String> forms = doc.select(".products-table-zip .hyphenate").eachText();
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