package server;

import network.TCPConnection;
import network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements TCPConnectionListener {

    public static void main(String[] args) {
        new Server();
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    private final Game game = new Game();
    private final Thread gameThread;

    private Server(){
        System.out.println("Server running...");

        gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    SendToAllConnections("Новый бой начинается через минуту! Всем матросам загадать число!!");
                    int time = 0;
                    try{
                        while (time < 60) {
                            Thread.sleep(10000);
                            time += 10;
                            SendToAllConnections("Бой начется через " + (60 - time) + " секунд!");
                        }
                        SendToAllConnections("Время боя!");
                        SendToAllConnections("Загаданное капитаном число: " + game.GetRandom());
                        SendToAllConnections(game.GetResult());
                    }
                    catch (Exception e){
                        System.out.println(e);
                    }
                }
            }
        });
        gameThread.start();

        try(ServerSocket serverSocket = new ServerSocket(8189)){
            while(true){
                try {
                    new TCPConnection(serverSocket.accept(), this);
                }
                catch (IOException e){
                    System.out.println("TCPConnection exception:" + e);
                }
            }
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void OnConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        SendToAllConnections("Матрос под номером - " + tcpConnection + " зашел на палубу");
    }

    @Override
    public synchronized void OnReceiveString(TCPConnection tcpConnection, String value) {
        System.out.println("Client" + tcpConnection + ": " + value);
        game.AddData(tcpConnection.toString(),Integer.parseInt(value));
    }

    @Override
    public synchronized void OnDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        SendToAllConnections("Client disconnected:" + tcpConnection);
    }

    @Override
    public synchronized void OnException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception:" + e);
    }

    private void SendToAllConnections(String value){
        System.out.println(value);
        final int cnt = connections.size();
        for(int i = 0; i < cnt; i++){
            connections.get(i).SendString(value);
        }
    }
}
