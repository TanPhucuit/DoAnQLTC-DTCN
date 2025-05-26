
package CoinGecko;
import java.util.List;


public interface CoinGeckoDao {
    List<String> getAllCoingeckoIds();
    boolean updateCurrentPriceByCoingeckoId(String coingeckoid, double newPrice);           
}
