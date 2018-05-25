package comunications;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {

	private ServerSocket server;
	private int port;
	private boolean stop;
	public final static Logger LOGGER = Logger.getGlobal();
	private ArrayList<ThreadSocket> connections;

	public Server(int port) throws IOException {
		connections = new ArrayList<>();
		this.port = port;
		server = new ServerSocket(port);
		start();
		LOGGER.log(Level.INFO, "Servidor inicado en puerto: " + this.port);
	}

	@Override
	public void run() {
		while (!stop) {
			Socket connection;
			try {
				connection = server.accept();
				connections.add(new ThreadSocket(connection));
				LOGGER.log(Level.INFO, "Conexion aceptada: " + connection.getInetAddress().getHostAddress());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			new Server(9000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}