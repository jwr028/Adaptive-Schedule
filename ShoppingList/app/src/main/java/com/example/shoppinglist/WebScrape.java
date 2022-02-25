package com.example.shoppinglist;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Array;

public class WebScrape extends AppCompatActivity {

    TextView webText; // Declaration of text box (if text box is changed/renamed change here)
    ImageView webImage;
    String item = "milk";
    int page = 1;
    private Button nextPage;
    private Button previousPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_scrape_specify);

        webText = findViewById(R.id.webText);
        webImage = findViewById(R.id.webImage);
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
    String theDescription;

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
                theDescription = elementsText.eq(0).text();
                thePicture = elementsImage.eq(0).attr("src_set");
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            webText.setText(theDescription);
            webImage.setImageURI(Uri.parse(thePicture));
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

