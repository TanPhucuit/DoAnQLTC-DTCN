/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CoinGecko; // Package của bạn

import java.util.List;
import java.util.Map;

/**
 *
 * @author 23520
 */
public class CoingeckoSerImpl implements CoingeckoSer { // <<=== THÊM "implements CoingeckoSer" VÀO ĐÂY

    private final CoinGeckoApi coinGeckoApi;
    private final CoinGeckoDao coinGeckoDao;

    // Constructor để inject dependencies (hoặc khởi tạo trực tiếp)
    public CoingeckoSerImpl() {
        this.coinGeckoApi = new CoinGeckoApi(); // Khởi tạo API service
        this.coinGeckoDao = new CoinGeckoImpl();  // Khởi tạo DAO implementation
    }

    // Hoặc nếu bạn muốn truyền vào từ bên ngoài (tốt cho testing)
    // public CoingeckoSerImpl(CoinGeckoApi coinGeckoApi, CoinGeckoDao coinGeckoDao) {
    //     this.coinGeckoApi = coinGeckoApi;
    //     this.coinGeckoDao = coinGeckoDao;
    // }

    @Override // Annotation này bây giờ sẽ hợp lệ
    public void updateAllConfiguredAssetPrices(String vsCurrency) {
        if (vsCurrency == null || vsCurrency.trim().isEmpty()) {
            System.err.println("Đơn vị tiền tệ (vsCurrency) không hợp lệ trong Service.");
            return;
        }

        // 1. Lấy danh sách COINGECKO_ID từ CSDL
        List<String> idsToUpdate = coinGeckoDao.getAllCoingeckoIds();
        if (idsToUpdate.isEmpty()) {
            System.out.println("Service: Không có COINGECKO_ID nào trong CSDL để cập nhật.");
            return;
        }

        System.out.println("Service: Bắt đầu lấy giá cho các ID: " + idsToUpdate + " bằng " + vsCurrency.toUpperCase());

        // 2. Lấy giá mới từ CoinGecko API
        Map<String, Double> newPrices = coinGeckoApi.getPricesInSpecificCurrency(idsToUpdate, vsCurrency);

        if (newPrices.isEmpty()) {
            System.out.println("Service: Không nhận được dữ liệu giá từ CoinGecko API.");
            return;
        }

        int successCount = 0;
        int failCount = 0;

        // 3. Cập nhật giá mới vào CSDL
        for (Map.Entry<String, Double> entry : newPrices.entrySet()) {
            String coingeckoId = entry.getKey();
            double price = entry.getValue();

            System.out.println("Service: Đang cập nhật CSDL cho ID " + coingeckoId + ", Giá: " + price);
            boolean updated = coinGeckoDao.updateCurrentPriceByCoingeckoId(coingeckoId, price);
            if (updated) {
                successCount++;
            } else {
                failCount++;
            }
        }

        System.out.println("Service: Hoàn tất cập nhật. Thành công: " + successCount + ", Thất bại: " + failCount);
    }
}