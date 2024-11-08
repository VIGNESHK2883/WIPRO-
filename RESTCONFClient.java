import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RESTCONFClient {

    private static final String BASE_URL = "http://10.10.10.1/restconf/data";

    public static void getInterfaceStatistics() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/interfaces/interface"))
                .header("Accept", "application/yang-data+json")
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("username:password".getBytes()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Interface Statistics: " + response.body());
        } else {
            System.out.println("Error: Unable to retrieve interface statistics.");
        }
    }

    public static void main(String[] args) throws Exception {
        getInterfaceStatistics();
    }
}
