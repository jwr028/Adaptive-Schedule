package com.example.shoppinglist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.Adapter.WebScrapeAdapter;
import com.example.shoppinglist.Model.WebScrapeItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class WebScrape extends AppCompatActivity {

    public RecyclerView recyclerViewWeb;
    private RecyclerView.Adapter adapter;
    private String itemName;

    private Button nextPage;
    private Button previousPage;
    private int page = 1;
    ArrayList<WebScrapeItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_scrape_specify);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            itemName = extras.getString("ItemName");
        } else{
            itemName = "milk";
        }

        description_webscrape dw = new description_webscrape();
        dw.execute();

        ArrayList<WebScrapeItem> recyclerViewWeb;
        recyclerViewWeb = list;

        this.recyclerViewWeb = (RecyclerView) findViewById(R.id.recyclerViewWeb);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.recyclerViewWeb.setLayoutManager(mLayoutManager);

        adapter = new WebScrapeAdapter(recyclerViewWeb);
        this.recyclerViewWeb.setAdapter(adapter);


        nextPage = findViewById(R.id.nextPage);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                list.clear();
                description_webscrape aw = new description_webscrape();
                aw.execute();

            }
        });

        previousPage = findViewById(R.id.previousPage);
        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page != 1) {
                    page--;
                    list.clear();
                    description_webscrape aw = new description_webscrape();
                    aw.execute();

                }
            }
        });
    }

    /*private ArrayList<WebScrapeItem> initCities() {
        ArrayList<WebScrapeItem> list = new ArrayList<>();

        list.add(new WebScrapeItem("Cinque Terre", "https://bit.ly/CBImageCinque"));
        list.add(new WebScrapeItem("Paris", "https://bit.ly/CBImageParis"));
        list.add(new WebScrapeItem("Rio de Janeiro", "https://bit.ly/CBImageRio"));
        list.add(new WebScrapeItem("Sydney", "https://bit.ly/CBImageRio"));
        list.add(new WebScrapeItem("Cinque Terre", "https://bit.ly/CBImageCinque"));
        list.add(new WebScrapeItem("Paris", "https://bit.ly/CBImageParis"));
        list.add(new WebScrapeItem("Rio de Janeiro", "https://bit.ly/CBImageRio"));
        list.add(new WebScrapeItem("Sydney", "https://bit.ly/CBImageRio"));
        list.add(new WebScrapeItem("Cinque Terre", "https://bit.ly/CBImageCinque"));
        list.add(new WebScrapeItem("Paris", "https://bit.ly/CBImageParis"));
        list.add(new WebScrapeItem("Rio de Janeiro", "https://bit.ly/CBImageRio"));
        list.add(new WebScrapeItem("Sydney", "https://bit.ly/CBImageRio"));

        return list;
    }*/




    private class description_webscrape extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            org.jsoup.nodes.Document document = null;
            org.jsoup.select.Elements elementsText;
            org.jsoup.select.Elements elementsImage;

            String url = null;
            if (page == 1){
                url = String.format("https://www.walmart.com/search?q=%s&affinityOverride=store_led",itemName);
            } else {
                url = String.format("https://www.walmart.com/search?q=%s&affinityOverride=store_led&page=%d",itemName, page);
            }

            try {
                document = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            elementsText = document.getElementsByClass("f6 f5-l normal dark-gray mb0 mt1 lh-title");
            elementsImage = document.getElementsByTag("img");

            /*
            String src;
            for (Element el : elementsImage){
                src = el.absUrl("src");
                Log.d("Image", src);
            }
            */

            int i = 0;
            while (i < elementsText.size()) {
                list.add(new WebScrapeItem(elementsText.eq(i).text(), elementsImage.eq(i).attr("src")));
                Log.d("Text", elementsText.eq(i).text());
                Log.d("Image", elementsImage.eq(i).attr("src"));
                Log.d("Placeholder", " ");
                i++;
            }
            /*for (int i = 0; i<10; i++) {
                list.add(elementsText.eq(i).text());
                //thePicture = elementsImage.eq(0).attr("src_set");
            }*/



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Log.d("Text", String.valueOf(list.size()));
            //Log.d("Text", list.toString());

        }


    }

    /*public String Scrape(){
        org.jsoup.nodes.Document document = null;
        String url = null;
        if (page == 1){
            url = String.format("https://www.walmart.com/search?q=%s&affinityOverride=store_led",item);
        } else {
            url = String.format("https://www.walmart.com/search?q=%s&affinityOverride=store_led&page=%d",item, page);
        }

        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        org.jsoup.select.Elements elementsText = document.getElementsByClass("f6 f5-l normal dark-gray mb0 mt1 lh-title");
        org.jsoup.select.Elements elementsImage = document.getElementsByClass("absolute top-0 left-0");
        return elementsText.text();
    }*/
}

