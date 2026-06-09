package MazeConstraction;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;

public class GetInstructions {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static CompletableFuture<String> generateMazeUrlAsync(int width, int height) {
        return CompletableFuture.supplyAsync(() -> {
            String baseApiUrl = "https://backend-qcf9.onrender.com/fm1/get-maze-image";

            int finalWidth = width;
            int finalHeight = height;

            if (finalWidth > 100 || finalWidth < 5) {
                finalWidth = 30;
            }

            if (finalHeight > 100 || finalHeight < 5) {
                finalHeight = 30;
            }

            return baseApiUrl + "?width=" + finalWidth + "&height=" + finalHeight;
        });
    }

    public static CompletableFuture<String[]> getApiSettingsAsync() {
        return CompletableFuture.supplyAsync(() -> {
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
        });
    }

    public static CompletableFuture<int[][]> convertImageToBinaryGridAsync() {// הגדרת מידות ברירת מחדל ישירות בתוך הפונקציה (למשל 40x40)
        int defaultWidth = 40;
        int defaultHeight = 40;

        return generateMazeUrlAsync(defaultWidth, defaultHeight).thenCompose(imageUrl -> {
            return CompletableFuture.supplyAsync(() -> {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(imageUrl))
                        .GET()
                        .build();

                try {
                    HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
                    BufferedImage image = ImageIO.read(response.body());

                    if (image == null) return null;

                    int imgWidth = image.getWidth();
                    int imgHeight = image.getHeight();
                    int[][] grid = new int[imgHeight][imgWidth];

                    int whiteRgb = Color.WHITE.getRGB();

                    for (int y = 0; y < imgHeight; y++) {
                        for (int x = 0; x < imgWidth; x++) {
                            int pixelRgb = image.getRGB(x, y);
                            grid[y][x] = (pixelRgb != whiteRgb) ? 0 : 1;
                        }
                    }
                    return grid;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            });
        });
    }
}