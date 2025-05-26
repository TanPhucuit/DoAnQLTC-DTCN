package CoinGecko; 

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.util.HashMap;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CoinGeckoApi {

    private static final String API_BASE_URL = "https://api.coingecko.com/api/v3";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public CoinGeckoApi() { 
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, Map<String, Double>> getCurrentPrices(List<String> coingeckoIds, String vsCurrency) {
        if (coingeckoIds == null || coingeckoIds.isEmpty() || vsCurrency == null || vsCurrency.trim().isEmpty()) {
            System.err.println("Danh sách ID CoinGecko hoặc đơn vị tiền tệ không hợp lệ.");
            return Collections.emptyMap();
        }

        String idsString = String.join(",", coingeckoIds);
        String url = API_BASE_URL + "/simple/price?ids=" + idsString + "&vs_currencies=" + vsCurrency.toLowerCase();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (!response.isSuccessful() || responseBody == null) {
                System.err.println("Lỗi khi gọi API CoinGecko: " + response.code() + " - " + response.message());
                if (responseBody != null) {
                    System.err.println("Response body (error): " + responseBody.string()); 
                }
                return Collections.emptyMap();
            }

            String jsonData = responseBody.string(); 
            if (jsonData.isEmpty() || jsonData.equals("{}")) {
                System.out.println("Không tìm thấy dữ liệu giá cho các ID: " + idsString + " với tiền tệ " + vsCurrency);
                return Collections.emptyMap();
            }
            
            return objectMapper.readValue(jsonData, new TypeReference<Map<String, Map<String, Double>>>() {});

        } catch (IOException e) {
            System.err.println("Lỗi I/O khi gọi API CoinGecko: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyMap();
        } catch (Exception e) {
            System.err.println("Lỗi không xác định khi xử lý phản hồi từ CoinGecko: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }
    public Map<String, Double> getPricesInSpecificCurrency(List<String> coingeckoIds, String targetVsCurrency) {
        if (targetVsCurrency == null || targetVsCurrency.trim().isEmpty()) {
            System.err.println("Đơn vị tiền tệ mục tiêu không hợp lệ.");
            return Collections.emptyMap();
        }

       
        Map<String, Map<String, Double>> rawPrices = getCurrentPrices(coingeckoIds, targetVsCurrency.toLowerCase());
        Map<String, Double> simplifiedPrices = new java.util.HashMap<>(); 

        if (rawPrices.isEmpty()) {
            return Collections.emptyMap();
        }

        String targetCurrencyKey = targetVsCurrency.toLowerCase();

        for (Map.Entry<String, Map<String, Double>> entry : rawPrices.entrySet()) {
            String coingeckoId = entry.getKey();
            Map<String, Double> currencyPriceMap = entry.getValue();

            if (currencyPriceMap != null && currencyPriceMap.containsKey(targetCurrencyKey)) {
                simplifiedPrices.put(coingeckoId, currencyPriceMap.get(targetCurrencyKey));
            } else {
                 System.out.println("Không tìm thấy giá cho " + coingeckoId + " theo tiền tệ " + targetCurrencyKey);
            }
        }
        return simplifiedPrices;
    }
}