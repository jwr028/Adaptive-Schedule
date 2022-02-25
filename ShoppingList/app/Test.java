import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebScrape {

    public static void main (String[] args){
        final String url = "https://shares.telegraph.co.uk/indices/summary/index/MCX";

        try{
            final Document document = Jsoup.connect(url).get();

            System.out.println(document.outerHtml());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


}
