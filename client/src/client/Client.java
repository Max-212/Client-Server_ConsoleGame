package client;

import network.TCPConnection;
import network.TCPConnectionListener;

import java.io.IOException;
import java.util.Scanner;

public class Client implements TCPConnectionListener {

    public static void main(String[] args) throws IOException {
        new Client();
    }

    private final TCPConnection connection;

    public Client() throws IOException {
        connection = new TCPConnection(this,"localhost", 8189);
        while (true) {
            Scanner in = new Scanner(System.in);
            String msg = in.nextLine();
            if(msg.matches("[0-9]{1,3}"))
                connection.SendString(msg);
            else System.out.println("Input number 0-999");

        }
    }

    @Override
    public void OnConnectionReady(TCPConnection tcpConnection) {
        System.out.println("Вы зашли на палубу. Капитан не любит чтобы кто-то находился тут.");
        System.out.println("Поэтому он раз в минуту проводит игру, победитель отправляется за палубу, прямо к морскому дну.");
        System.out.println("Правила: матросы загадывают числа, капитан тоже,\n кто загадает самое близкое число к числу капитана - победит");
    }

    @Override
    public void OnReceiveString(TCPConnection tcpConnection, String value) {
        System.out.println(value);
    }

    @Override
    public void OnDisconnect(TCPConnection tcpConnection) {

    }

    @Override
    public void OnException(TCPConnection tcpConnection, Exception e) {

    }
}
