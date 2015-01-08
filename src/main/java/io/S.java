package io;

import redis.task.Serializer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zinchenko on 01.12.14.
 */
public class S {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8090);

        MessageManager messageManager = new MessageManager();
        messageManager.start();

        while (true) {
            System.out.println("waiting for the client");
            Socket socket = serverSocket.accept();
            System.out.println("accepted");

            new Thread(() -> {
                try {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    String name = dataInputStream.readUTF();

                    messageManager.register(socket, name);

                    while(true) {
                        System.out.println("waiting message from " + name);
                        String newClientMessage = dataInputStream.readUTF();
                        System.out.println("New message from " + name + ": " + newClientMessage);

                        messageManager.publish(name, newClientMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }


    }

    static class Subscriber {
        private ObjectOutputStream outputStream;
        private String name;

        Subscriber(ObjectOutputStream outputStream, String name) {
            this.outputStream = outputStream;
            this.name = name;
        }

        public ObjectOutputStream getOutputStream() {
            return outputStream;
        }

        public void setOutputStream(ObjectOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class Message implements Serializable {
        private String name;
        private String text;

        Message(String name, String text) {
            this.name = name;
            this.text = text;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    static class MessageManager extends Thread{
        public List<Subscriber> subscribers = Collections
                .synchronizedList(new LinkedList<Subscriber>());

        public Queue<Message> messages = new ConcurrentLinkedQueue<>();

        @Override
        public void run() {
            while (true) {
                if(messages.isEmpty()) continue;

                Message m = messages.remove();
                for(Subscriber subscriber: subscribers) {
                    try {
                        if(subscriber.getName().equals(m.getName())) {
                            continue;
                        }
                        subscriber.getOutputStream().writeObject(m);
                        subscriber.getOutputStream().flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void publish(String name, String text) {
            messages.add(new Message(name, text));
        }

        public void register(Socket socket, String name) throws IOException {
            ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            subscribers.add(new Subscriber(outputStream, name));
        }
    }

}
