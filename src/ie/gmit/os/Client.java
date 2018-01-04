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
		String ipaddress = this.stdin.next();
		// Try to open the socket
		try (Socket requestSocket = new Socket(ipaddress, this.port);) {

			System.out.println("Connected to " + ipaddress + " in port 2004");
			// Create new output stream to socket
			this.out = new ObjectOutputStream(requestSocket.getOutputStream());
			// Flush the output stream
			this.out.flush();
			// Create new input strem from socket
			this.in = new ObjectInputStream(requestSocket.getInputStream());

			// Run until the client says bye
			do {
				try {
					// Menu
					this.doSingleCommunication();

					if (this.message.compareToIgnoreCase("1") == 0) {
						// PPS number
						this.doSingleCommunication();
						// Password
						this.doSingleCommunication();
						// Result
						this.message = (String) in.readObject();
						System.out.println(this.message);
						// If the login was successful
						if (this.message.contains("Welcome")) {
							// Draw the logged in menu
							do {
								// Logged in menu
								this.doSingleCommunication();

								// If fitness record adding
								if (this.message.compareToIgnoreCase("1") == 0) {
									// Fitness mode
									this.doSingleCommunication();
									// Duration
									this.doSingleCommunication();
									// Result
									this.message = (String) in.readObject();
									System.out.println(this.message);
									// If meal record adding
								} else if (this.message.compareToIgnoreCase("2") == 0) {
									// Meal type
									this.doSingleCommunication();
									// Description
									this.doSingleCommunication();
									// Result
									this.message = (String) in.readObject();
									System.out.println(this.message);
									//If record viewing option was selected	
								}else if (this.message.compareToIgnoreCase("3") == 0) {
									//Record type
									this.doSingleCommunication();
									// Result
									this.message = (String) in.readObject();
									System.out.println(this.message);
								}
								// Log out if -1 is entered
							} while (!this.message.equals("-1"));
						}

					} else if (this.message.compareToIgnoreCase("2") == 0) {
						// PPS Number
						this.doSingleCommunication();
						// Password
						this.doSingleCommunication();
						// Name
						this.doSingleCommunication();
						// Address
						this.doSingleCommunication();
						// Age
						this.doSingleCommunication();
						// Weight
						this.doSingleCommunication();
						// Height
						this.doSingleCommunication();
						// Registration result
						this.message = (String) in.readObject();
						System.out.println(message);
					} else if (this.message.compareToIgnoreCase("3") == 0) {
						this.message = "bye";
					}

				} catch (ClassNotFoundException classNot) {
					System.err.println("data received in unknown format");
				}
				// Exit if the client says bye
			} while (!this.message.equals("bye"));
		} catch (UnknownHostException unknownHost) {
			// Notify the user if the host is not found
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			// Close the streams and the scanner
			try {
				this.in.close();
				this.out.close();
				this.stdin.close();
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
			this.out.writeObject(msg);
			// Flush the stream
			this.out.flush();
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
		this.message = (String) this.in.readObject();
		// Write to console
		System.out.println(this.message);
		//Reset the console input
		this.stdin=new Scanner(System.in);
		// Read message from console
		this.message = this.stdin.nextLine();
		// Send to server
		sendMessage(this.message);
	}
}