package com.personal.finance.testproject.service;

import com.personal.finance.testproject.dao.InvestStorageDetailDAO;
import com.personal.finance.testproject.model.InvestStorageDetail;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.time.Instant;
import java.util.List;

public class PriceUpdateService {
    private final InvestStorageDetailDAO investStorageDetailDAO;
    private final HttpClient httpClient;
    private static final BigDecimal USD_TO_VND = new BigDecimal("26000");
    private static final Map<String, String> SPOT_SYMBOL_MAPPING = new HashMap<>();
    private static final Map<String, String> FUTURE_SYMBOL_MAPPING = new HashMap<>();
    private static final String API_KEY = "YZTA1XKWpmW4hWoXH8xC0QhfFF78Y3IXU8a7YQAcpzvVFnlV1NesLEcRyoyBb9UNgmSdhU0zgbgvfQKAKKQaA";
    private static final String SECRET_KEY = "zLSlDDppBJCKM2YXKOJsd0CXik1skq6gsZjoLPdM3GWq8376vd1LZVtXfdjDZObJaZQudhbmMbxDBSlUg";
    
    static {
        SPOT_SYMBOL_MAPPING.put("IN_BTC", "BTC-USDT");
        SPOT_SYMBOL_MAPPING.put("Short_BTC", "BTC-USDT");
        SPOT_SYMBOL_MAPPING.put("IN_ETH", "ETH-USDT");
        SPOT_SYMBOL_MAPPING.put("Short_ETH", "ETH-USDT");
        FUTURE_SYMBOL_MAPPING.put("IN_GOLD", "XAUUSDT");
        FUTURE_SYMBOL_MAPPING.put("ST_GOLD", "XAUUSDT");
        FUTURE_SYMBOL_MAPPING.put("IN_CrudeOilBrent", "BRENTUSDT");
    }

    private final BingXApiService bingXApiService = new BingXApiService();

    public PriceUpdateService(InvestStorageDetailDAO investStorageDetailDAO) {
        this.investStorageDetailDAO = investStorageDetailDAO;
        this.httpClient = HttpClient.newHttpClient();
    }

    public void updatePrices() {
        try {
            System.out.println("[LOG] Bắt đầu cập nhật giá...");
            var details = investStorageDetailDAO.findAll();
            System.out.println("[LOG] Tìm thấy " + details.size() + " tài sản cần cập nhật giá");

            // Lấy giá spot từ BingX
            List<BingXSpotTicker> spotTickers = bingXApiService.fetchSpotTickers();
            System.out.println("[LOG] Đã lấy được " + spotTickers.size() + " giá spot từ BingX");
            java.util.Map<String, BigDecimal> spotPriceMap = new java.util.HashMap<>();
            for (BingXSpotTicker ticker : spotTickers) {
                spotPriceMap.put(ticker.getSymbol(), ticker.getLastPrice());
            }

            for (var detail : details) {
                String inStId = detail.getInStId();
                String spotSymbol = SPOT_SYMBOL_MAPPING.get(inStId);
                String futureSymbol = FUTURE_SYMBOL_MAPPING.get(inStId);
                BigDecimal price = null;
                boolean isGold = "ST_GOLD".equals(inStId);
                boolean isBrent = "IN_CrudeOilBrent".equals(inStId);
                // Đặc biệt cho ST_GOLD và IN_CrudeOilBrent: lấy giá future XAUUSDT và BRENTUSDT
                if ("ST_GOLD".equals(inStId)) {
                    try {
                        if (futureSymbol != null) {
                            price = bingXApiService.fetchFutureLastTradePrice(futureSymbol);
                            System.out.println("[LOG][GOLD] Giá future last trade cho ST_GOLD (" + futureSymbol + "): " + price);
                            if (price == null) {
                                System.err.println("[ERROR][GOLD] Không lấy được giá future cho ST_GOLD với symbol " + futureSymbol);
                            }
                        } else {
                            System.err.println("[ERROR][GOLD] futureSymbol null cho ST_GOLD");
                        }
                    } catch (Exception ex) {
                        System.err.println("[ERROR][GOLD] Exception khi lấy giá future cho ST_GOLD: " + ex.getMessage());
                    }
                } else if ("IN_CrudeOilBrent".equals(inStId)) {
                    try {
                        if (futureSymbol != null) {
                            price = bingXApiService.fetchFutureLastTradePrice(futureSymbol);
                            System.out.println("[LOG][BRENT] Giá future last trade cho IN_CrudeOilBrent (" + futureSymbol + "): " + price);
                            if (price == null) {
                                System.err.println("[ERROR][BRENT] Không lấy được giá future cho IN_CrudeOilBrent với symbol " + futureSymbol);
                            }
                        } else {
                            System.err.println("[ERROR][BRENT] futureSymbol null cho IN_CrudeOilBrent");
                        }
                    } catch (Exception ex) {
                        System.err.println("[ERROR][BRENT] Exception khi lấy giá future cho IN_CrudeOilBrent: " + ex.getMessage());
                    }
                } else if (futureSymbol != null) {
                    try {
                        BingXFutureTicker futureTicker = bingXApiService.fetchFutureTicker(futureSymbol);
                        if (futureTicker != null) {
                            price = futureTicker.getPrice();
                            System.out.println("[LOG] Lấy giá future cho " + inStId + " (" + futureSymbol + "): " + price);
                        }
                    } catch (Exception ex) {
                        System.err.println("[WARN] Không lấy được giá future cho " + inStId + ": " + ex.getMessage());
                    }
                }
                // Nếu không phải hàng hóa thì lấy spot
                if (price == null && spotSymbol != null && spotPriceMap.containsKey(spotSymbol)) {
                    price = spotPriceMap.get(spotSymbol);
                    System.out.println("[LOG] Lấy giá spot cho " + inStId + " (" + spotSymbol + "): " + price);
                }
                if (price != null) {
                    BigDecimal priceInVND = price.multiply(USD_TO_VND).setScale(2, RoundingMode.HALF_UP);
                    System.out.println("[LOG] Chuyển đổi giá " + inStId + " sang VND: " + price + " -> " + priceInVND);
                    boolean updated = false;
                    int retry = 0;
                    while (retry < 3) {
                        try {
                            investStorageDetailDAO.updateCurPrice(inStId, priceInVND);
                            InvestStorageDetail updatedDetail = investStorageDetailDAO.findById(inStId);
                            if (updatedDetail != null && priceInVND.compareTo(updatedDetail.getCurPrice()) == 0) {
                                System.out.println("[LOG] Đã cập nhật giá cho " + inStId + " thành công: " + priceInVND + " (lần thử: " + (retry+1) + ")");
                                updated = true;
                                break;
                            } else {
                                System.err.println("[WARN] Giá DB chưa khớp giá API cho " + inStId + ". Giá API*26000: " + priceInVND + ", Giá DB: " + (updatedDetail != null ? updatedDetail.getCurPrice() : "null") + ". Thử lại...");
                            }
                        } catch (Exception ex) {
                            System.err.println("[ERROR] Lỗi khi cập nhật giá cho " + inStId + ": " + ex.getMessage());
                        }
                        retry++;
                    }
                    if (!updated) {
                        System.err.println("[ERROR] Không thể cập nhật giá cho " + inStId + " sau 3 lần thử. Giá API*26000: " + priceInVND);
                    }
                } else {
                    System.err.println("[ERROR] Không lấy được giá cho " + inStId);
                }
            }
            System.out.println("[LOG] Hoàn thành cập nhật giá");
        } catch (Exception e) {
            System.err.println("[ERROR] Lỗi khi cập nhật giá: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật giá: " + e.getMessage(), e);
        }
    }

    private String generateSignature(String timestamp, String method, String path, String queryString) {
        try {
            String data = timestamp + method + path + queryString;
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error generating signature: " + e.getMessage(), e);
        }
    }
} 