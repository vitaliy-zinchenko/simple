package io;

import java.io.*;
import java.net.Socket;

/**
 * Created by zinchenko on 01.12.14.
 */
public class C {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Socket socket = new Socket("localhost", 8090);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        System.out.println("What is your name?");
        String name = reader.readLine();
        dataOutputStream.writeUTF(name);
        System.out.println("Connected.");

        MessageReceiver messageReceiver = new MessageReceiver(socket);
        messageReceiver.start();

        String line = null;
        System.out.println("Write message:");
        while ((line = reader.readLine()) != null) {

            dataOutputStream.writeUTF(line);
            dataOutputStream.flush();

            System.out.println("Write message:");
        }
    }

    static class MessageReceiver extends Thread {

        private Socket socket;

        private ObjectInputStream inputStream;

        MessageReceiver(Socket socket) {
            this.socket = socket;
        }

        private ObjectInputStream getInputStream() throws IOException {
            if (inputStream == null)
                inputStream = new ObjectInputStream(socket.getInputStream());

            return inputStream;
        }

        @Override
        public void run() {
            while(true) {
                S.Message answer = null;
                try {
                    answer = (S.Message) getInputStream().readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println(answer.getName() + " << " + answer.getText());
            }
        }
    }

}
