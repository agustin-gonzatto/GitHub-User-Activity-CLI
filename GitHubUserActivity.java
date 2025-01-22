import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubUserActivity {
    public static void main(String[] args) {
        String url = "https://api.github.com/users/" + args[0] + "/events";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                parseAndDisplayEvents(response.toString());
            } else {
                System.out.println("Error code: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parseAndDisplayEvents(String jsonResponse) {
        String[] events = jsonResponse.split("\\{");
        for(int i = 0; i < events.length; i++){
            if (events[i].contains("\"type\"") && events[i].contains("\"id\"")) {
               String type = events[i].split("\"type\":\"")[1].split("\"")[0];
                String repo = events[i+2].split("\"name\":\"")[1].split("\"")[0];
              switch (type) {
                  case "PushEvent":
                      System.out.println("Pushed commits to " + repo);
                      break;
                  case "IssuesEvent":
                      System.out.println("Opened a new issue in " + repo);
                      break;
                  case "WatchEvent":
                      System.out.println("Starred " + repo);
                      break;
                  default:
                      System.out.println("Performed " + type + " on " + repo);
                      break;
              }
            }
        }
    }
}
