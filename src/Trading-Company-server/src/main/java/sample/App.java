package sample;

import databaseWork.DatabaseHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class App implements Runnable
{
    public static int clients = 1;

    public static void main( String[] args )
    {
        new Thread(new App()).start();
    }

    @Override
    public void run() {
        ServerSocket s = null;
        try {
            DatabaseHandler.getInstance();
            System.out.println("Сервер запущен");
            s = new ServerSocket(4567);
            while (true) {
                Socket socket = s.accept();
                if (socket!=null)
                {
                    System.out.println("Подключился " + clients + " пользователь. " + socket.getInetAddress());
                    new ClientSocket(socket, clients);
                    clients++;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка сервера...");
        }finally {
            try {
                DatabaseHandler.getInstance().closeConnection();
                System.out.println("Сервер отключён.");
                assert s != null;
                s.close();
            }catch (IOException | SQLException e) {
                System.out.println("Ошибка завершения работы сервера...");
            }
        }
    }


}
