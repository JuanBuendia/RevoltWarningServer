package comunications;

import java.net.Socket;
import java.io.IOException;
import java.time.LocalTime;
import java.util.logging.Level;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.format.DateTimeFormatter;

public class ThreadSocket extends Thread {

	private boolean stop;
	private Socket connection;
	private DataInputStream input;
	private DataOutputStream output;

	public ThreadSocket(Socket socket) throws IOException {
		this.connection = socket;
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
		start();
	}

	@Override
	public void run() {
		while (!stop) {
			String request;
			try {
				request = input.readUTF();
				if (request != null) {
					manageRequest(request);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void manageRequest(String request) throws IOException {
		Server.LOGGER.log(Level.INFO, "Request: " + connection.getInetAddress().getHostAddress() + " - " + request);
		switch (Request.valueOf(request)) {
		case ALERT:
			responseAlertService();
			break;
		case ALERT_WITH_TEXT:
			responseAlertWithTextService();
			break;
		default:
			break;
		}
		Server.LOGGER.log(Level.INFO, "Conexion con: " + connection.getInetAddress().getHostAddress() + " cerrada.");
	}

	private void responseAlertWithTextService() throws IOException {
		output.writeUTF(Response.ALERT_WITH_TEXT.toString());
		output.writeUTF(input.readUTF());
		output.writeUTF(LocalTime.now().format(DateTimeFormatter.ISO_TIME));
	}

	private void responseAlertService() throws IOException {
		output.writeUTF(Response.ALERT.toString());
		output.writeUTF(LocalTime.now().format(DateTimeFormatter.ISO_TIME));
	}
}