package network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.EventListener;

public class TCPConnection {


    private final TCPConnectionListener eventListener;
    private final Socket socket;
    private final Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;


    public TCPConnection(TCPConnectionListener eventListener, String ipAddress, int port) throws IOException {
        this(new Socket(ipAddress,port), eventListener);
    }


    public TCPConnection(Socket socket, TCPConnectionListener eventListener) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));

        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    eventListener.OnConnectionReady(TCPConnection.this);
                    while(!rxThread.isInterrupted()){
                        eventListener.OnReceiveString(TCPConnection.this,in.readLine());
                    }
                }
                catch (IOException e){
                    eventListener.OnException(TCPConnection.this,e);
                }
                finally {
                    eventListener.OnDisconnect(TCPConnection.this);
                }

            }
        });
        rxThread.start();
    }

    public synchronized void SendString(String value){
        try {
            out.write(value + "\r\n");
            out.flush();
        }
        catch (IOException e) {
            eventListener.OnException(this,e);
            Disconnect();
        }
    }

    public synchronized void Disconnect(){
        rxThread.interrupt();
        try {
            socket.close();
        }
        catch (IOException e) {
            eventListener.OnException(this,e);
        }
    }

    @Override
    public String toString() {
        return socket.getInetAddress() + ":" + socket.getPort();
    }
}
