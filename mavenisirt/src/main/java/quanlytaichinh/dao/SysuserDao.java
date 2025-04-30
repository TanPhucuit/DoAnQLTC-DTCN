/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package quanlytaichinh.dao;
import quanlytaichinh.model.Sysuser;
import java.util.List;
/**
 *
 * @author 23520
 */
public interface SysuserDao {
    Sysuser FindUser(String username);
    boolean AddUser(Sysuser username);
    List<Sysuser> findallUser();
    boolean UpdateUser(Sysuser username);
    boolean DeleteUser(int userid);
    Sysuser findIdUser(int userId); 
}
