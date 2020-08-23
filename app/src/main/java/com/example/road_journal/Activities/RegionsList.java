package com.example.road_journal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.road_journal.R;import java.util.ArrayList;

public class RegionsList extends AppCompatActivity {
    public static String TAG = "TAG";
    ListView listView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.regions_list);
        listView = (ListView) findViewById(R.id.regionlist);
        ArrayList arrayList = new ArrayList();
        arrayList.add("Baringo");
        arrayList.add("Bomet");
        arrayList.add("Bungoma");
        arrayList.add("Busia");
        arrayList.add("Elgeyo Marakwet");
        arrayList.add("Embu");
        arrayList.add("Garrissa");
        arrayList.add("Homa Bay");
        arrayList.add("Isiolo");
        arrayList.add("Kajiado");
        arrayList.add("Kakamega");
        arrayList.add("Kericho");
        arrayList.add("Kiambu");
        arrayList.add("Kilifi");
        arrayList.add("Kirinyaga");
        arrayList.add("Kisii");
        arrayList.add("Kisumu");
        arrayList.add("Kitui");
        arrayList.add("Kwale");
        arrayList.add("Laikipia");
        arrayList.add("Lamu");
        arrayList.add("Machakos");
        arrayList.add("Makueni");
        arrayList.add("Mandera");
        arrayList.add("Meru");
        arrayList.add("Migori");
        arrayList.add("Marsabit");
        arrayList.add("Mombasa");
        arrayList.add("Muranga");
        arrayList.add("Nairobi");
        arrayList.add("Nakuru");
        arrayList.add("Nandi");
        arrayList.add("Narok");
        arrayList.add("Nyamira");
        arrayList.add("Nyandarua");
        arrayList.add("Nyeri");
        arrayList.add("Samburu");
        arrayList.add("Siaya");
        arrayList.add("Taita Taveta");
        arrayList.add("Tana River");
        arrayList.add("Tharaka Nithi");
        arrayList.add("Trans Nzioa");
        arrayList.add("Turkana");
        arrayList.add("Uasin Gishu");
        arrayList.add("Vihiga");
        arrayList.add("Wajir");
        arrayList.add("West Pokot");
        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList));
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                switch (i) {
                    case 0:
                        Intent intent = new Intent(RegionsList.this, RegionsActivity.class);
                        intent.putExtra("town", "Baringo");
                        RegionsList.this.startActivity(intent);
                        return;
                    case 1:
                        Intent intent2 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent2.putExtra("town", "Bomet");
                        RegionsList.this.startActivity(intent2);
                        return;
                    case 2:
                        Intent intent3 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent3.putExtra("town", "Bungoma");
                        RegionsList.this.startActivity(intent3);
                        return;
                    case 3:
                        Intent intent4 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent4.putExtra("town", "Busia");
                        RegionsList.this.startActivity(intent4);
                        return;
                    case 4:
                        Intent intent5 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent5.putExtra("town", "Elgeyo Marakwet");
                        RegionsList.this.startActivity(intent5);
                        return;
                    case 5:
                        Intent intent6 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent6.putExtra("town", "Embu");
                        RegionsList.this.startActivity(intent6);
                        return;
                    case 6:
                        Intent intent7 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent7.putExtra("town", "Garissa");
                        RegionsList.this.startActivity(intent7);
                        return;
                    case 7:
                        Intent intent8 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent8.putExtra("town", "Homa Bay");
                        RegionsList.this.startActivity(intent8);
                        return;
                    case 8:
                        Intent intent9 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent9.putExtra("town", "Isiolo");
                        RegionsList.this.startActivity(intent9);
                        return;
                    case 9:
                        Intent intent10 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent10.putExtra("town", "Kajiado");
                        RegionsList.this.startActivity(intent10);
                        return;
                    case 10:
                        Intent intent11 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent11.putExtra("town", "Kakamega");
                        RegionsList.this.startActivity(intent11);
                        return;
                    case 11:
                        Intent intent12 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent12.putExtra("town", "Kericho");
                        RegionsList.this.startActivity(intent12);
                        return;
                    case 12:
                        Intent intent13 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent13.putExtra("town", "Kiambu");
                        RegionsList.this.startActivity(intent13);
                        return;
                    case 13:
                        Intent intent14 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent14.putExtra("town", "Kilifi");
                        RegionsList.this.startActivity(intent14);
                        return;
                    case 14:
                        Intent intent15 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent15.putExtra("town", "Kirinyaga");
                        RegionsList.this.startActivity(intent15);
                        return;
                    case 15:
                        Intent intent16 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent16.putExtra("town", "Kisii");
                        RegionsList.this.startActivity(intent16);
                        return;
                    case 16:
                        Intent intent17 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent17.putExtra("town", "Kisumu");
                        RegionsList.this.startActivity(intent17);
                        return;
                    case 17:
                        Intent intent18 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent18.putExtra("town", "Kitui");
                        RegionsList.this.startActivity(intent18);
                        return;
                    case 18:
                        Intent intent19 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent19.putExtra("town", "Kwale");
                        RegionsList.this.startActivity(intent19);
                        return;
                    case 19:
                        Intent intent20 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent20.putExtra("town", "Lakipia");
                        RegionsList.this.startActivity(intent20);
                        return;
                    case 20:
                        Intent intent21 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent21.putExtra("town", "Lamu");
                        RegionsList.this.startActivity(intent21);
                        return;
                    case 21:
                        Intent intent22 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent22.putExtra("town", "Machakos");
                        RegionsList.this.startActivity(intent22);
                        return;
                    case 22:
                        Intent intent23 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent23.putExtra("town", "Makueni");
                        RegionsList.this.startActivity(intent23);
                        return;
                    case 23:
                        Intent intent24 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent24.putExtra("town", "Mandera");
                        RegionsList.this.startActivity(intent24);
                        return;
                    case 24:
                        Intent intent25 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent25.putExtra("town", "Meru");
                        RegionsList.this.startActivity(intent25);
                        return;
                    case 25:
                        Intent intent26 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent26.putExtra("town", "Marsabit");
                        RegionsList.this.startActivity(intent26);
                        return;
                    case 26:
                        Intent intent27 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent27.putExtra("town", "Migori");
                        RegionsList.this.startActivity(intent27);
                        return;
                    case 27:
                        Intent intent28 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent28.putExtra("town", "Mombasa");
                        RegionsList.this.startActivity(intent28);
                        return;
                    case 28:
                        Intent intent29 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent29.putExtra("town", "Muranga");
                        RegionsList.this.startActivity(intent29);
                        return;
                    case 29:
                        Intent intent30 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent30.putExtra("town", "Nairobi");
                        RegionsList.this.startActivity(intent30);
                        return;
                    case 30:
                        Intent intent31 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent31.putExtra("town", "Nakuru");
                        RegionsList.this.startActivity(intent31);
                        return;
                    case 31:
                        Intent intent32 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent32.putExtra("town", "Nandi");
                        RegionsList.this.startActivity(intent32);
                        return;
                    case 32:
                        Intent intent33 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent33.putExtra("town", "Narok");
                        RegionsList.this.startActivity(intent33);
                        return;
                    case 33:
                        Intent intent34 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent34.putExtra("town", "Nyamira");
                        RegionsList.this.startActivity(intent34);
                        return;
                    case 34:
                        Intent intent35 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent35.putExtra("town", "Nyandarua");
                        RegionsList.this.startActivity(intent35);
                        return;
                    case 35:
                        Intent intent36 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent36.putExtra("town", "Nyeri");
                        RegionsList.this.startActivity(intent36);
                        return;
                    case 36:
                        Intent intent37 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent37.putExtra("town", "Samburu");
                        RegionsList.this.startActivity(intent37);
                        return;
                    case 37:
                        Intent intent38 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent38.putExtra("town", "Siaya");
                        RegionsList.this.startActivity(intent38);
                        return;
                    case 38:
                        Intent intent39 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent39.putExtra("town", "Taita Taveta");
                        RegionsList.this.startActivity(intent39);
                        return;
                    case 39:
                        Intent intent40 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent40.putExtra("town", "Tana River");
                        RegionsList.this.startActivity(intent40);
                        return;
                    case 40:
                        Intent intent41 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent41.putExtra("town", "Tharaka Nithi");
                        RegionsList.this.startActivity(intent41);
                        return;
                    case 41:
                        Intent intent42 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent42.putExtra("town", "Trans Nzoia");
                        RegionsList.this.startActivity(intent42);
                        return;
                    case 42:
                        Intent intent43 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent43.putExtra("town", "Turkana");
                        RegionsList.this.startActivity(intent43);
                        return;
                    case 43:
                        Intent intent44 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent44.putExtra("town", "Uasin Gishu");
                        RegionsList.this.startActivity(intent44);
                        return;
                    case 44:
                        Intent intent45 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent45.putExtra("town", "Vihiga");
                        RegionsList.this.startActivity(intent45);
                        return;
                    case 45:
                        Intent intent46 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent46.putExtra("town", "Wajir");
                        RegionsList.this.startActivity(intent46);
                        return;
                    case 46:
                        Intent intent47 = new Intent(RegionsList.this, RegionsActivity.class);
                        intent47.putExtra("town", "West Pokot");
                        RegionsList.this.startActivity(intent47);
                        return;
                    default:
                        return;
                }
            }
        });
    }
}
