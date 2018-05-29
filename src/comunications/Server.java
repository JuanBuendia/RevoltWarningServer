package comunications;

import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {

	private int port;
	private boolean stop;
	private ServerSocket server;
	private ArrayList<ThreadSocket> connections;
	public final static Logger LOGGER = Logger.getGlobal();

	public Server(int port) throws IOException {
		LOGGER.log(Level.INFO, "Servidor inicado en puerto: " + this.port);
		connections = new ArrayList<>();
		server = new ServerSocket(port);
		this.port = port;
		start();
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