package MazeConstruction;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import javax.imageio.ImageIO;

public class GetInstructions {

    private static final HttpClient client = HttpClient.newHttpClient();

    // בדיקת אינטרנט פשוטה פנימית
    private static boolean isOnline() {
        try (java.net.Socket socket = new java.net.Socket()) {
            socket.connect(new java.net.InetSocketAddress("8.8.8.8", 53), 1500);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String[] getApiSettings() {

        String[] result = new String[]{"#000000", "#FFFFFF", "true", "#CCCCCC", "50"};

        if (!isOnline()) {
            return new String[]{"error","error","error","error","error"};
        }

        String settingsUrl = "https://backend-qcf9.onrender.com/fm1/get-render-config";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(settingsUrl))
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();

        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();
            if (json == null || json.isBlank()) return result;

            json = json.replaceAll("[{}\"\\s]", "");
            String[] pairs = json.split(",");

            for (String pair : pairs) {
                String[] kv = pair.split(":");
                if (kv.length < 2) continue;

                String key = kv[0].trim();
                String value = kv[1].trim();

                switch (key.toLowerCase()) {
                    case "wallcellcolor":     result[0] = value; break;
                    case "pathcolor":         result[1] = value; break;
                    case "drawgrid":          result[2] = value; break;
                    case "gridcolor":         result[3] = value; break;
                    case "animationdelayms":  result[4] = value; break;
                }
            }
            return result;

        } catch (Exception e) {

            return result;
        }
    }


    public static int[][] convertImageToBinaryGrid(int width, int height) {

        if (!isOnline()) return null;

        int finalWidth = width;
        int finalHeight = height;

        if (finalWidth > 100 || finalWidth < 5) finalWidth = 30;
        if (finalHeight > 100 || finalHeight < 5) finalHeight = 30;

        String imageUrl =
                "https://backend-qcf9.onrender.com/fm1/get-maze-image?width="
                        + finalWidth + "&height=" + finalHeight;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(imageUrl))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

        try {
            HttpResponse<InputStream> response =
                    client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            BufferedImage image = ImageIO.read(response.body());

            if (image == null) return null;

            int imgWidth = image.getWidth();
            int imgHeight = image.getHeight();

            int blockRows = imgHeight / 16;
            int blockCols = imgWidth / 16;

            int[][] grid = new int[blockRows][blockCols];

            int whiteRgb = Color.WHITE.getRGB();

            for (int y = 0; y < imgHeight; y += 16) {
                for (int x = 0; x < imgWidth; x += 16) {

                    int pixelRgb = image.getRGB(x, y);

                    int gridY = y / 16;
                    int gridX = x / 16;

                    grid[gridY][gridX] = (pixelRgb != whiteRgb) ? 0 : 1;
                }
            }

            return grid;

        } catch (Exception e) {
            return null;
        }
    }
}