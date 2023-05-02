package sample;

import connectionWork.ActionMenu;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

public class ClientSocket extends Thread {

    private int number;
    public Socket socket;
    public DataInputStream in;
    public DataOutputStream out;

    public ClientSocket(Socket s, int number) throws IOException {
        this.number = number;
        socket = s;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        start();
    }

    @Override
    public void run() {
        try {
            ActionMenu actionMenu = new ActionMenu();
            while(true) {
                sendData(actionMenu.dataProcessing(in.readUTF()));
            }
        }
        catch (IOException | SQLException | ClassNotFoundException e) {
            System.out.println("Пользователь " + number + " отключился.");
        } finally {
            try {
                socket.close();
            }
            catch (IOException e) {
                System.err.println("Сокет не закрыт");
            }
        }
    }

    public void sendData(String ob1){
        try{
            out.flush();
            out.writeUTF(ob1);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
