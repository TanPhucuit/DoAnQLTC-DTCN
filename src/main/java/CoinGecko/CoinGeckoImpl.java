
package CoinGecko;
import com.personal.finance.testproject.util.DatabaseConnection; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoinGeckoImpl implements CoinGeckoDao {
    
    @Override
    public List<String> getAllCoingeckoIds() {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT DISTINCT COINGECKO_ID FROM INVEST_STORAGE_DETAIL WHERE COINGECKO_ID IS NOT NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getString("COINGECKO_ID"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách Coingecko IDs: " + e.getMessage());
            e.printStackTrace();
        }
        return ids;
    }

    @Override
    public boolean updateCurrentPriceByCoingeckoId(String coingeckoId, double newPrice) {
        String sql = "UPDATE INVEST_STORAGE_DETAIL SET CUR_PRICE = ?, UP_DATE = SYSDATE WHERE COINGECKO_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newPrice);
            ps.setString(2, coingeckoId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật giá cho Coingecko ID " + coingeckoId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    }
