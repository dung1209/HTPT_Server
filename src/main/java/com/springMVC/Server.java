package com.springMVC;

//import com.springMVC.dao.impl.UserImpl;
//import com.springMVC.entity.User;

import com.springMVC.dao.impl.UserImpl;
import com.springMVC.entity.User;
import com.springMVC.service.UserService;
import com.springMVC.view.ServerView;
import com.springMVC.view.ServerView1;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
  private static UserService userService;
  String requestMessage;
  String responseMessage;
  static ServerView1 serverView1;

  public static void main(String[] args) {
    userService = new UserService(new UserImpl());
    serverView1 = new ServerView1();

    try (ServerSocket server = new ServerSocket(9091)) {
      System.out.println("Server is running on port 9091");
      while (true) {
        Socket socket = server.accept();
        System.out.println("Coordinator connected");
        System.out.println("Coordinator IP: " + socket.getInetAddress().getHostName());

        Server temp = new Server();
        Thread t = new Thread(temp.new ClientHandler(socket));
        t.start();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private class ClientHandler implements Runnable {

    private Socket socket;

    //        private UserImpl userDao;
    public ClientHandler(Socket socket) {
      super();
      this.socket = socket;
//            userDao = new UserImpl();
    }

    @Override
    public void run() {
      try {
        List<User> users = new ArrayList<>();
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        // Đọc yêu cầu từ coordinator
        System.out.println("Waiting..........");
        String request = in.readLine();
        System.out.println("Title: " + request);
        String clientID = in.readUTF();
        System.out.println("Client's ID: " + clientID);
        serverView1.setClientID(clientID);

        // Xử lý yêu cầu
        String processedRequest = processRequest(request);
        System.out.println("yêu cầu: " + processedRequest);
        serverView1.setClientRequest(processedRequest);

        switch (processedRequest) {
          case "GET_LIST_CS1": {
            requestMessage = "Get list users from branch 1!";

            users = userService.getListUser();
            // Gửi danh sách người dùng cho coordinator
            out.writeObject(users);
            out.flush();
            break;
          }
          case "ADD_CS1": {
            requestMessage = "Add new user to branch 1!";

            User receivedUser = (User) in.readObject();
            boolean isAdded = userService.addUser(receivedUser);

            if (isAdded) {
              responseMessage = "User added successfully!";
              System.out.println(responseMessage);
              out.writeObject(responseMessage);
              out.flush();
            } else {
              responseMessage = "Failed to add user!";
              System.out.println(responseMessage);
              out.writeObject(responseMessage);
              out.flush();
            }
            break;
          }
          case "SAVE_CS1": {
            User receivedUser = (User) in.readObject();
            System.out.println(receivedUser.toString());

            requestMessage = "Update user has ID: " + receivedUser.getId();

            boolean isUpdated = userService.updateUser(receivedUser);


            if (isUpdated) {
              responseMessage = "User is updated successfully!";
              System.out.println(responseMessage);
              out.writeObject(responseMessage);
              out.flush();
            } else {
              responseMessage = "Failed to update user!";
              System.out.println(responseMessage);
              out.writeObject(responseMessage);
              out.flush();
            }
            break;
          }
          case "DELETE_CS1": {
            User receivedUser = (User) in.readObject();
            requestMessage = "Delete user has ID: " + receivedUser.getId();

            boolean isDeleted = userService.deleteUser(receivedUser);

            if (isDeleted) {
              responseMessage = "User is deleted successfully!";
              System.out.println(responseMessage);
              out.writeObject(responseMessage);
              out.flush();
            } else {
              responseMessage = "Failed to delete user!";
              System.out.println(responseMessage);
              out.writeObject(responseMessage);  // Gửi phản hồi trong mọi trường hợp
              out.flush();
            }
            break;
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private String processRequest(String request) {
    return request;
  }

}