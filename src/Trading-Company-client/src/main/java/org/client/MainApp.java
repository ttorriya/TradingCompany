package org.client;

import classes.ResponseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class MainApp extends Application implements Runnable{
    static public Socket connection;
    static public DataOutputStream output;
    static public DataInputStream input;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/ApplicationIcon.jpg")));
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/authentication/authMenu.fxml"));
        primaryStage.setTitle("Система управления складами");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        new Thread(new MainApp()).start();
        Application.launch(args);
    }

    @Override
    public void run(){
        try{
            connection = new Socket(InetAddress.getByName("localhost"), 4567);
            output = new DataOutputStream(connection.getOutputStream());
            input = new DataInputStream(connection.getInputStream());
            while(true){
                ResponseHandler.handler(input.readUTF());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void sendData(String ob1){
        try{
            output.flush();
            output.writeUTF(ob1);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}