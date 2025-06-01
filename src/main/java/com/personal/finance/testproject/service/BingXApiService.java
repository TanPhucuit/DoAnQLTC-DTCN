package com.personal.finance.testproject.service;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.math.BigDecimal;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;
import java.net.URL;
import java.time.Instant;

public class BingXApiService {
    private static final String BASE_URL = "https://open-api.bingx.com";
    private static final String API_KEY = "YZTA1XKWpmW4hWoXH8xC0QhfFF78Y3IXU8a7YQAcpzvVFnlV1NesLEcRyoyBb9UNgmSdhU0zgbgvfQKAKKQaA";
    private static final String SECRET_KEY = "zLSlDDppBJCKM2YXKOJsd0CXik1skq6gsZjoLPdM3GWq8376vd1LZVtXfdjDZObJaZQudhbmMbxDBSlUg";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private final HttpClient httpClient;

    public BingXApiService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<BingXSpotTicker> fetchSpotTickers() throws Exception {
        String method = "GET";
        String path = "/openApi/spot/v1/ticker/24hr";
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        TreeMap<String, String> parameters = new TreeMap<>();
        parameters.put("timestamp", timestamp);
        
        String signature = generateSignature(method, path, parameters);
        String url = buildRequestUrl(path, parameters, signature);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("X-BX-APIKEY", API_KEY)
            .header("User-Agent", "Mozilla/5.0")
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("API request failed with status: " + response.statusCode() + ", body: " + response.body());
        }

        JSONObject jsonResponse = new JSONObject(response.body());
        if (jsonResponse.getInt("code") != 0) {
            throw new RuntimeException("API returned error: " + jsonResponse.getString("msg"));
        }

        JSONArray data = jsonResponse.getJSONArray("data");
        List<BingXSpotTicker> tickers = new ArrayList<>();
        
        for (int i = 0; i < data.length(); i++) {
            JSONObject ticker = data.getJSONObject(i);
            String symbol = ticker.getString("symbol");
            Object lastPriceObj = ticker.get("lastPrice");
            BigDecimal lastPrice;
            if (lastPriceObj instanceof Number) {
                lastPrice = new BigDecimal(((Number) lastPriceObj).toString());
            } else {
                lastPrice = new BigDecimal(lastPriceObj.toString());
            }
            tickers.add(new BingXSpotTicker(symbol, lastPrice));
        }
        
        return tickers;
    }

    public BingXFutureTicker fetchFutureTicker(String symbol) throws Exception {
        String method = "GET";
        String path = "/openApi/swap/v2/quote/ticker";
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        TreeMap<String, String> parameters = new TreeMap<>();
        parameters.put("symbol", symbol);
        parameters.put("timestamp", timestamp);
        
        String signature = generateSignature(method, path, parameters);
        String url = buildRequestUrl(path, parameters, signature);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("X-BX-APIKEY", API_KEY)
            .header("User-Agent", "Mozilla/5.0")
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("API request failed with status: " + response.statusCode() + ", body: " + response.body());
        }

        JSONObject jsonResponse = new JSONObject(response.body());
        if (jsonResponse.getInt("code") != 0) {
            throw new RuntimeException("API returned error: " + jsonResponse.getString("msg"));
        }

        JSONObject data = jsonResponse.getJSONObject("data");
        Object lastPriceObj = data.get("lastPrice");
        BigDecimal price;
        if (lastPriceObj instanceof Number) {
            price = new BigDecimal(((Number) lastPriceObj).toString());
        } else {
            price = new BigDecimal(lastPriceObj.toString());
        }
        return new BingXFutureTicker(symbol, price);
    }

    public BigDecimal fetchFutureLastTradePrice(String symbol) {
        String endpoint = "/openApi/spot/v1/ticker/price";
        try {
            // Tạo map tham số
            Map<String, String> params = new HashMap<>();
            params.put("symbol", symbol);
            // Thêm timestamp
            long timestamp = Instant.now().toEpochMilli();
            params.put("timestamp", String.valueOf(timestamp));

            // Build query string
            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (queryString.length() > 0) queryString.append("&");
                queryString.append(entry.getKey()).append("=").append(entry.getValue());
            }
            // Tạo signature
            String signature = generateSignature(queryString.toString(), SECRET_KEY);
            queryString.append("&signature=").append(signature);

            // Tạo URL và connection
            URL url = new URL(BASE_URL + endpoint + "?" + queryString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-BX-APIKEY", API_KEY);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                responseCode == 200 ? connection.getInputStream() : connection.getErrorStream()
            ));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

            // Log toàn bộ response
            System.out.println("[DEBUG][API][" + symbol + "] Response: " + responseBuilder);

            JSONObject jsonResponse = new JSONObject(responseBuilder.toString());
            if (jsonResponse.getInt("code") != 0) {
                System.err.println("[ERROR][API][" + symbol + "] API returned error: " + jsonResponse.optString("msg"));
                return null;
            }
            JSONObject data = jsonResponse.getJSONObject("data");
            Object priceObj = data.get("price");
            BigDecimal price = (priceObj instanceof Number)
                ? new BigDecimal(((Number) priceObj).toString())
                : new BigDecimal(priceObj.toString());
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("[WARN][API][" + symbol + "] Price <= 0: " + price);
                return null;
            }
            return price;
        } catch (Exception e) {
            System.err.println("[ERROR][API][" + symbol + "] Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String generateSignature(String method, String path, TreeMap<String, String> parameters) {
        try {
            String queryString = buildQueryString(parameters);
            String data = method + path + "?" + queryString;
            
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(signedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error generating signature: " + e.getMessage(), e);
        }
    }

    private String buildQueryString(TreeMap<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (var entry : parameters.entrySet()) {
            if (!first) {
                sb.append("&");
            }
            first = false;
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

    private String buildRequestUrl(String path, TreeMap<String, String> parameters, String signature) {
        String queryString = buildQueryString(parameters);
        return BASE_URL + path + "?" + queryString + "&signature=" + signature;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    // Hàm tạo signature HMAC SHA-256 (hex)
    private static String generateSignature(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
} 