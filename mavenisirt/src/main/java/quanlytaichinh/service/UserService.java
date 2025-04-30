/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package quanlytaichinh.service;
import quanlytaichinh.model.Sysuser;
import java.util.List;

/**
 *
 * @author 23520
 */
public interface UserService {
    Sysuser Login(String username,String Password);
    boolean Register(Sysuser user);
    boolean changePassword(int userId, String currentPassword, String newPassword);
    boolean UpdateUserInfor(Sysuser user);
    boolean DeleteUserInfor(Sysuser user);
    List <Sysuser> GetAllUser();
    
    
    
}
