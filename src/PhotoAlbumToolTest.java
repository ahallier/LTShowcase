import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class PhotoAlbumToolTest {

    //would be nice to do some mocking of the rest call
    private static String URL = "https://jsonplaceholder.typicode.com/photos";
    private PrintStream systemOut;
    private final ByteArrayOutputStream testOut = new ByteArrayOutputStream();
    private PhotoAlbumTool albumTool = new PhotoAlbumTool();
    private final String photoPrintPattern = "\\[[0-9]+\\][a-z ]+";
    private final Pattern photoPattern = Pattern.compile(photoPrintPattern);
    private JSONObject obj1 = new JSONObject().put("albumId",3).put("id",100).put("title","This is line one test");
    private JSONObject obj2 = new JSONObject().put("albumId",4).put("id",200).put("title","This is line two test");
    private JSONArray testArray = new JSONArray().put(obj1).put(obj2);

    @Before
    public void setUp() {
        systemOut = System.out;
        System.setOut(new PrintStream(testOut));
    }

    @After
    public void takeDown() {
        System.setOut(systemOut);
    }

    @Test
    public void testPrintAlbum() throws IOException {
        albumTool.printAlbum(new String[]{"3"});
        String output = testOut.toString();
        //if no error, and input found
        if(!output.contains("There are no photos that match your request.")){
            assertThat(output, containsString("> photo-album 3"));
            Matcher matcher = photoPattern.matcher(output);
            assertEquals(true, matcher.find());
            assertThat(output, containsString("Printing complete!"));
        }
    }

    @Test
    public void testGetJSONResponse() throws IOException{
        //Testing http response and conversion to json
        //If we did not care to test response, it would be nice to do some mocking here.
        JSONArray jsonArray = albumTool.getJSONResponse(URL);
        assertNotNull(jsonArray);
        JSONObject firstObj = jsonArray.getJSONObject(0);
        assertTrue(firstObj.has("albumId"));
        assertTrue(firstObj.has("id"));
        assertTrue(firstObj.has("title"));
    }

    @Test
    public void testPrintToScreen(){
        albumTool.printToScreen(testArray);
        String output = testOut.toString();

        assertThat(output, containsString("> photo-album 3"));
        assertThat(output, containsString("> photo-album 4"));
        Matcher matcher = photoPattern.matcher(output);
        assertEquals(true, matcher.find());
        assertThat(output, containsString("Printing complete!"));
    }

    @Test
    public void testPrintAlbumTwoInput() throws IOException {
        albumTool.printAlbum(new String[]{"3", "4"});
        String output = testOut.toString();
        //if no error, and input found
        if(!output.contains("There are no photos that match your request.")){
            assertThat(output, containsString("> photo-album 3"));
            assertThat(output, containsString("> photo-album 4"));
            Matcher matcher = photoPattern.matcher(output);
            assertEquals(true, matcher.find());
            assertThat(output, containsString("Printing complete!"));
        }
    }

    @Test
    public void testPrintAlbumInvalidInput() throws IOException {
        albumTool.printAlbum(new String[]{"Billy"});
        String output = testOut.toString();
        assertThat(output, containsString("There are no photos that match your request"));
        assertThat(output, containsString("Printing complete!"));
    }

}
