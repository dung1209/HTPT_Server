package com.springMVC.dao.impl;

import com.springMVC.dao.UserDao;
import com.springMVC.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class UserImpl implements UserDao {
    private final SessionFactory sessionFactory;

    public UserImpl() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public boolean addUser(User user) {
        boolean isAdded = false;
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction(); // Bắt đầu transaction

            session.save(user); // Lưu user vào cơ sở dữ liệu

            transaction.commit(); // Commit transaction
            isAdded = true; // Đánh dấu thêm mới thành công
            System.out.println("User added: " + user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback nếu có lỗi
            }
            e.printStackTrace(); // Ghi log lỗi
        }

        return isAdded;
    }



    @Override
    public boolean updateUser(User user) {
        boolean isUpdated = false;
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction(); // Bắt đầu transaction
            session.update(user); // Cập nhật user
            transaction.commit(); // Commit transaction
            isUpdated = true; // Đánh dấu cập nhật thành công
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback nếu có lỗi
            }
            e.printStackTrace(); // Ghi log lỗi
        }

        return isUpdated;
    }


    @Override
    public boolean deleteUser(User user) {
        boolean isDeleted = false;
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction(); // Bắt đầu transaction

            session.delete(user); // Xóa user

            transaction.commit(); // Commit transaction
            isDeleted = true; // Đánh dấu xóa thành công
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback nếu xảy ra lỗi
            }
            e.printStackTrace(); // Ghi log lỗi
        }

        return isDeleted;
    }


    @Override
    public List<User> getListUser() {
        List<User> users = null;
        try(Session session = sessionFactory.openSession()){
            users = session.createQuery("FROM User", User.class).list();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }
}
