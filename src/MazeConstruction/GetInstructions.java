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

        if (!isOnline()) {
            return new String[]{"eror","eror","eror","eror","eror"};
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

            json = json.replaceAll("[{}\"\\s]", "");
            String[] pairs = json.split(",");
            String[] result = new String[5];

            for (String pair : pairs) {
                String[] kv = pair.split(":");
                if (kv.length < 2) continue;

                switch (kv[0]) {
                    case "wallCellColor":     result[0] = kv[1]; break;
                    case "pathColor":         result[1] = kv[1]; break;
                    case "drawGrid":          result[2] = kv[1]; break;
                    case "gridColor":         result[3] = kv[1]; break;
                    case "animationDelayMs":  result[4] = kv[1]; break;
                }
            }
            return result;

        } catch (Exception e) {
            return new String[]{"eror","eror","eror","eror","eror"};
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