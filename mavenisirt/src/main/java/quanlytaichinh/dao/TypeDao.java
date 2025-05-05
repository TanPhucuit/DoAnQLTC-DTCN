
package quanlytaichinh.dao;
import java.util.List;
import quanlytaichinh.model.Type;
public interface TypeDao {
 List<Type> getTypesByUserId(int userId);
}
