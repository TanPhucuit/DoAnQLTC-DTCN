/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.service;
import java.util.List;
import quanlytaichinh.model.Sysuser;
import quanlytaichinh.service.UserService;
import quanlytaichinh.dao.SysuserDaott;
import java.util.ArrayList;
import java.sql.*;
import java.sql.SQLException;
import quanlytaichinh.dao.SysuserDao;


public class UserServicett implements UserService {
    private SysuserDao dao;

    public UserServicett() {
        this.dao = new SysuserDaott();
    }

    @Override
    public Sysuser Login(String name, String password) {
        System.out.println("Đang đăng nhập cho user: " + name);
        try {
            Sysuser usercall = dao.FindUser(name);
            if (usercall == null) {
                System.out.println("Không tìm thấy User");
                return null;
            }

            if (password != null && password.equals(usercall.getPassword())) {
                System.out.println("Đăng nhập thành công");
                return usercall;
            } else {
                System.out.println("Sai mật khẩu hoặc user không tìm thấy");
                return null;
            }

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra trong quá trình đăng nhập: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean Register(Sysuser user) {
        System.out.println("Đăng ký mới cho người dùng " + user.getUsername());

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            System.out.println("Đăng ký không thành công vì chưa có tên tài khoản");
            return false;
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty() || user.getPassword().length() > 12) {
            System.out.println("Mật khẩu không thể để trống hoặc vượt quá 12 ký tự");
            return false;
        }

        try {
            Sysuser userdb = dao.FindUser(user.getUsername());
            if (userdb != null) {
                System.out.println("Tên tài khoản đã tồn tại");
                return false;
            }

            System.out.println("Tên tài khoản " + user.getUsername() + " hợp lệ");
            return dao.AddUser(user);

        } catch (Exception e) {
            System.err.println("Lỗi không thể đăng ký: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean UpdateUserInfor(Sysuser user) {
        System.out.println("Cập nhật thông tin cho UserID: " + user.getUserid());

        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
             if (user.getPassword().length() > 12) {
                 System.out.println("Mật khẩu mới quá dài.");
                 return false;
             }
             System.out.println("Đang cập nhật mật khẩu");
        } else {
             try {
                Sysuser currentUser = dao.findIdUser(user.getUserid());
                if (currentUser != null) {
                    user.setPassword(currentUser.getPassword());
                } else {
                     System.out.println("Không tìm thấy user để cập nhật.");
                     return false;
                }
             } catch(Exception e) {
                 System.err.println("Lỗi khi lấy thông tin user hiện tại để cập nhật: " + e.getMessage());
                 e.printStackTrace();
                 return false;
             }
        }

        try {
            return dao.UpdateUser(user);
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật thông tin người dùng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean DeleteUserInfor(Sysuser user) {
         if (user == null) return false;
         int userId = user.getUserid();
         System.out.println("Xóa người dùng với UserID: " + userId);
         try {
             return dao.DeleteUser(userId);
         } catch (Exception e) {
             System.err.println("Lỗi khi xóa người dùng: " + e.getMessage());
             e.printStackTrace();
             return false;
         }
    }

    @Override
    public List<Sysuser> GetAllUser() {
        System.out.println("Lấy danh sách tất cả người dùng");
        try {
            return dao.findallUser();
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách người dùng: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

     @Override
     public boolean changePassword(int userId, String currentPassword, String newPassword) {
         System.out.println("Thay đổi mật khẩu cho " + userId);
         if (newPassword == null || newPassword.trim().isEmpty() || newPassword.length() > 12) {
             System.out.println("Mật khẩu mới không hợp lệ.");
             return false;
         }

         try {
             Sysuser user = dao.findIdUser(userId);
             if (user == null) {
                 System.out.println("Không tìm thấy người dùng.");
                 return false;
             }

             boolean currentPasswordMatches = currentPassword != null && currentPassword.equals(user.getPassword());

             if (currentPasswordMatches) {
                 user.setPassword(newPassword);

                 System.out.println("Mật khẩu đúng");
                 return dao.UpdateUser(user);
             } else {
                 System.out.println("Mật khẩu hiện tại không đúng.");
                 return false;
             }

         } catch (Exception e) {
             System.err.println("Lỗi khi thay đổi mật khẩu: " + e.getMessage());
             e.printStackTrace();
             return false;
         }
     }
}
    

 

