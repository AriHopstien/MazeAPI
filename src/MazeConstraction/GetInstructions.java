package MazeConstraction;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.imageio.ImageIO;

public class GetInstructions {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static String generateMazeUrl(int width, int height) {
        String baseApiUrl = "https://backend-qcf9.onrender.com/fm1/get-maze-image";

        if (width > 100 || width < 5) {
            width = 30;
        }

        if (height > 100 || height < 5) {
            height = 30;
        }
        return baseApiUrl + "?width=" + width + "&height=" + height;
    }

    public static String[] getApiSettings() {
        String settingsUrl = "https://backend-qcf9.onrender.com/fm1/get-render-config";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(settingsUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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
            e.printStackTrace();
            return null;
        }
    }

    public static int[][] convertImageToBinaryGrid(String imageUrl) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(imageUrl))
                .GET()
                .build();

        try {
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            BufferedImage image = ImageIO.read(response.body());

            if (image == null) return null;

            int width = image.getWidth();
            int height = image.getHeight();
            int[][] grid = new int[height][width];

            int whiteRgb = Color.WHITE.getRGB();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixelRgb = image.getRGB(x, y);

                    if (pixelRgb != whiteRgb) {
                        grid[y][x] = 0;
                    } else {
                        grid[y][x] = 1;
                    }
                }
            }
            return grid;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}