import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class PhotoAlbumTool {

    private static String URL = "https://jsonplaceholder.typicode.com/photos";

    public static void main ( String[] args ) {
        System.out.println("Starting album tool...\n");

        PhotoAlbumTool albumTool = new PhotoAlbumTool();
        try{
            if(args.length > 0 && args[0].equals("interactive")){
                try (Scanner scanner =new Scanner(System.in)) {
                    while (true) {
                        System.out.println("Enter your album Number");
                        String input = scanner.next();
                        if (input.equals("exit")) {
                            break;
                        }
                        System.out.println("Fetching your photos...\n");
                        albumTool.printAlbum(new String[]{input});
                    }
                }
            }else{
                albumTool.printAlbum(args);
            }
        }catch(IOException e){
            System.out.println("An error occurred: " + e);
        }

        System.out.println("Goodbye");
    }

    public void printAlbum(String[] args) throws IOException {
        String url = URL;

        if(args.length != 0) {
            url = url + "?albumId=" + args[0];
            for (int i = 1; i < args.length; i++) {
                url = url + "&albumId=" + args[i];
            }
        }

        JSONArray jsonArray = this.getJSONResponse(url);
        this.printToScreen(jsonArray);
    }

    public JSONArray getJSONResponse(String url) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet getRequest = new HttpGet(url);
        HttpEntity httpEntity =  httpClient.execute(getRequest).getEntity();
        return new JSONArray(EntityUtils.toString(httpEntity));
    }

    public JSONArray getJSON( HttpResponse response ) throws IOException {
        HttpEntity httpEntity = response.getEntity();
        return new JSONArray(EntityUtils.toString(httpEntity));
    }

    private void printSingleAlbum(JSONArray jsonArray, String albumNumber) throws IllegalStateException {
        System.out.println("> photo-album " + albumNumber);
        jsonArray.forEach(item -> {
            JSONObject obj = (JSONObject) item;
            if ( obj.has("albumId") && obj.has("id") && obj.has("title")) {
                System.out.println("[" + obj.get("id") + "] " + obj.get("title"));
            }else{
                throw new IllegalStateException("Unexpected return format. Expects JSONArray" +
                        "of JSONObjects with values for albumId, id and title.");
            }
        });
    }

    public void printToScreen(JSONArray jsonArray) throws IllegalStateException {
        SortedMap<Integer, String> albums = new TreeMap<>();
        jsonArray.forEach(item -> {
            JSONObject obj = (JSONObject) item;
            if ( obj.has("albumId") && obj.has("id") && obj.has("title")){
                Integer albumId = Integer.parseInt(obj.get("albumId").toString());
                String photoDescription = "[" + obj.get("id") + "] " + obj.get("title") + "\n";
                photoDescription = albums.containsKey(albumId) ? albums.get(albumId) + photoDescription : photoDescription;
                albums.put(albumId, photoDescription);
            }
            else {
                throw new IllegalStateException("Unexpected return format. Expects JSONArray " +
                        "of JSONObjects with values for albumId, id and title.");
            }
        });

        if(albums.size() == 0){
            System.out.println("There are no photos that match your request.\n");
        }else{
            for(Map.Entry<Integer,String> entry : albums.entrySet()){
                System.out.println("> photo-album " + entry.getKey());
                System.out.println(entry.getValue());
            }
        }

        System.out.println("Printing complete!\n");

    }
}
