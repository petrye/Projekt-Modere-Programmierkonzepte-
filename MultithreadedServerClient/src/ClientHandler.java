import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClientHandler extends Thread {
	
	// Variablen f�r den Client/Socket
	DataInputStream dis;
	DataOutputStream dos;
	Socket s;
	
	// eigenes Format f�r Datum
	SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy - hh:mm:ss");
	
	// Konstruktor
	public ClientHandler (Socket s, DataInputStream dis, DataOutputStream dos){
		this.s = s;
		this.dis = dis;
		this.dos = dos;
	}
	
	@Override
	public void run() {
		
		// Variablen zum Speichern der Nachrichten
		String received;
		String toReturn;
		
		// Endlosschleife, Abbruchbedingung: Empfangen von "Exit"
		while(true) {
			
			try {
				
				// Nachricht dem Outputstream �bergeben
				// Nachfrage beim Client
				dos.writeUTF("Hallo, wie kann ich Ihnen helfen? \"Date\" gibt das aktuelle Datum zur�ck, mit \"Exit\" wird die Verbindung getrennt.");
				
				// Inputstream lesen und in Variable speichern
				// Antwort vom Client lesen
				received = dis.readUTF();
				
				// �berpr�fung der Abbruchbedingung
				// Socket schlie�en und Endlosschleife beenden
				if (received.equals("Exit")) {
					System.out.println("Trenne Verbindung zu " + s);
					s.close();
					System.out.println("Verbindung getrennt.");
					break;
				}
				
				Date date = new Date();
			
				// Empfangene Nachricht pr�fen und entsprechend antworten
				// Antwort dem Outputstream �bergeben
				switch (received) {
				
					case "Date" :
						toReturn = dateFormatter.format(date);
						dos.writeUTF(toReturn);
						break;
					
					case "Hallo" :
						toReturn = "Welt!";
						dos.writeUTF(toReturn);
						break;
					
					default:
						// Echo :P
						dos.writeUTF(received);
						break;
				}
				
			}catch (IOException e) {
				// Bei geworfenen Fehlern verliert der Socket die Verbindung, Endlosschleife wird verlassen
				e.printStackTrace();
				System.out.println(s + " hat die Verbindung verloren.");
				break;
			}
		}
	
		try {
			// Input- und Outputstream beenden
			dis.close();
			dos.close();
		}catch(IOException e) {
			e.printStackTrace();
		}	
	}
}