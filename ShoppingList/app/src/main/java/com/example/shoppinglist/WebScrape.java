package com.example.shoppinglist;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.Adapter.WebScrapeAdapter;
import com.example.shoppinglist.Model.WebScrapeItem;

import java.util.ArrayList;

public class WebScrape extends AppCompatActivity {

    private RecyclerView recyclerViewWeb;
    private RecyclerView.Adapter adapter;
    private String itemName;

    private Button nextPage;
    private Button previousPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_scrape_specify);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            itemName = extras.getString("ItemName");
        }

        ArrayList<WebScrapeItem> recyclerViewWeb = ActualScrape();

        this.recyclerViewWeb = (RecyclerView) findViewById(R.id.recyclerViewWeb);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.recyclerViewWeb.setLayoutManager(mLayoutManager);

        adapter = new WebScrapeAdapter(recyclerViewWeb);
        this.recyclerViewWeb.setAdapter(adapter);
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

    private  ArrayList<WebScrapeItem> ActualScrape(){
        ArrayList<WebScrapeItem> list = new ArrayList<>();
        String item = "milk";
        int page = 1;
    }

    /*



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_scrape_specify);

        //webText = findViewById(R.id.webText);
        //webImage = findViewById(R.id.webImage);
        recyclerViewWeb = findViewById(R.id.recyclerViewWeb);
        nextPage = findViewById(R.id.nextPage);
        previousPage = findViewById(R.id.previousPage);

        description_webscrape dw = new description_webscrape();
        dw.execute();


        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                description_webscrape aw = new description_webscrape();
                aw.execute();
            }
        });

        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page != 1) {
                    page--;
                    description_webscrape aw = new description_webscrape();
                    aw.execute();

                }
            }
        });

    }
    String thePicture;
    List theDescription;

    private class description_webscrape extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

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
            org.jsoup.select.Elements elementsImage = document.select("img[class=absolute top-0 left-0]");

            for (int i = 0; i<10; i++) {
                theDescription.addItem(elementsText.eq(i).text());
                //thePicture = elementsImage.eq(0).attr("src_set");
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {



            //(theDescription);

            //webImage.setImageURI(Uri.parse(thePicture));
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

