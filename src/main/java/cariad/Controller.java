package cariad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minidev.json.JSONObject;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class Controller {

    @GetMapping("/strings")
    public JSONObject queryParameter(@RequestParam List<String> u) {
        //URL Validator to check if string is url
        String[] schemes = {"http"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        HashSet<String> set = new HashSet<String>();
        for (int i = 0; i < u.size(); i++) {
            if (urlValidator.isValid(u.get(i))) {
                //Request configured to timeout after 500ms
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(500)
                    .setConnectionRequestTimeout(500)
                    .setSocketTimeout(500).build();

            // Client
            CloseableHttpClient client =
                    HttpClientBuilder.create().setDefaultRequestConfig(config).build();


            // Building the Request to be sent to server
            HttpGet getMethod = new HttpGet(u.get(i));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(u.get(i))).build();
            HttpUriRequest r = getMethod;
            CloseableHttpResponse response;
            String result = null;
            try {
                response = client.execute(getMethod);
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);


                //Splits the response into strings using REGEX
                List<String> listMatches = getAllMatchesAsList(result);
                String x = listMatches.get(0);
                String[] q = x.split(",");
                // Adding the strings to set, so that duplicates are removed
                for (String s : q) {
                    if (!set.contains(s)) {
                        set.add(s);
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

        // Creation of JSONObject for return statement
        JSONObject object = new JSONObject();
        String [] unique = set.toArray(String[]::new);
        for(String x : unique){
            x = x.replace("\\", "");
        }

        // Sorting the Strings lexicographically Time complexity: O(n log(n))
        Arrays.sort((unique));
        Gson Gson=new GsonBuilder().create();
        Gson g = new Gson();
        String ja =Gson.toJson(Arrays.toString(unique));
        String jsonFormattedString = ja.replaceAll("\\\\", "");
        String jsonFormattedString1 = jsonFormattedString.replaceAll("\\\\", "");

        //Adding strings to the beginning of the object, followed by its values
        System.out.println(jsonFormattedString);
        object.put("strings", jsonFormattedString1);



        return object;
    }

    private static List<String> getAllMatchesAsList(String str) {

        List<String> listMatches = new ArrayList<String>();

        //create a pattern to extract numbers
        Pattern pattern = Pattern.compile("(?<=\\[).*?(?=\\])");

        //create a matcher for input string
        Matcher matcher = pattern.matcher(str);

        //iterate over all matches
        while (matcher.find()) {

            //add match to the list
            listMatches.add(matcher.group());
        }
        return  listMatches;

    }
}


