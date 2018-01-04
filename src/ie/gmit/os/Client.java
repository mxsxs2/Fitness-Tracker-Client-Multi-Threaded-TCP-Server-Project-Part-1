package ie.gmit.os;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	// The server port to send to
	private int port = 2004;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message = "";
	Scanner stdin;

	/**
	 * Opens a new socket connection to a specified server and keeps it open until
	 * the user says bye
	 */
	public void run() {
		// Create a new scanner to read from console
		this.stdin = new Scanner(System.in);
		System.out.println("Please Enter your IP Address");
		// Get the ip address to connect to
		String ipaddress = stdin.next();
		// Try to open the socket
		try (Socket requestSocket = new Socket(ipaddress, this.port);) {

			System.out.println("Connected to " + ipaddress + " in port 2004");
			// Create new output stream to socket
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			// Flush the output stream
			out.flush();
			// Create new input strem from socket
			in = new ObjectInputStream(requestSocket.getInputStream());

			// Run until the client says bye
			do {
				try {
					// Read a message from the server
					message = (String) in.readObject();
					// Write out server message
					System.out.println(message);
					// Read a message from the client
					message = stdin.next();
					// Send the message to the server
					sendMessage(message);

					if (message.compareToIgnoreCase("1") == 0) {
						//PPS number
						this.doSingleCommunication();
						//Password
						this.doSingleCommunication();
						//Result
						message = (String) in.readObject();
						System.out.println(message);

					} else if (message.compareToIgnoreCase("2") == 0) {
						//PPS Number
						this.doSingleCommunication();
						//Password
						this.doSingleCommunication();
						//Name
						this.doSingleCommunication();
						//Address
						this.doSingleCommunication();
						//Age
						this.doSingleCommunication();
						//Weight
						this.doSingleCommunication();
						//Height
						this.doSingleCommunication();
						//Registration result
						message = (String) in.readObject();
						System.out.println(message);
					}

				} catch (ClassNotFoundException classNot) {
					System.err.println("data received in unknown format");
				}
				// Exit if the client says bye
			} while (!message.equals("bye"));
		} catch (UnknownHostException unknownHost) {
			// Notify the user if the host is not found
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			// Close the streams and the scanner
			try {
				in.close();
				out.close();
				stdin.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	/**
	 * Send a new message to the client
	 * 
	 * @param Message
	 */
	private void sendMessage(String msg) {
		try {
			// Send the message
			out.writeObject(msg);
			// Flush the stream
			out.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	/**
	 * Reads a message from the server, shows to the user and sends the users
	 * response to back to the server.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void doSingleCommunication() throws ClassNotFoundException, IOException {
		// Read message from server
		message = (String) in.readObject();
		// Write to console
		System.out.println(message);
		// Read message from console
		message = stdin.next();
		// Send to server
		sendMessage(message);
	}
}