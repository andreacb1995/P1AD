import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class MyEchoServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Properties config = new Properties();
		config.setProperty("puerto", "2222");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date fecha = new Date();

		try {
			InetAddress address = InetAddress.getLocalHost();
			String rutafichero = ".\\trafic.log";
			File fichero = new File(rutafichero);
			BufferedWriter bw = null;
			bw = new BufferedWriter(new FileWriter(fichero));
			System.out.println("El fichero" + fichero + " se ha creado correctamente.");

			config.store(new FileOutputStream(".\\server.props"), "Fichero de configuracion server.props");
			// cargamos el fichero de propiedades
			config.load(new FileInputStream(".\\server.props"));

			ServerSocket server = new ServerSocket(Integer.parseInt(config.getProperty("puerto")));
			System.out.println("Inicio Servidor");
			Socket client = server.accept();
			// conexion establecida
			PrintWriter salida = new PrintWriter(client.getOutputStream(), true);
			BufferedReader entrada = new BufferedReader(new InputStreamReader(client.getInputStream()));
			System.out.println("Cliente conectado");

			String aux = null;

			while (client.isConnected()) {
				aux = entrada.readLine();
				if (aux.equals("exit")) {
					entrada.close();
					salida.close();
					client.close();
					server.close();
				}
				salida.println("Servidor: " + aux);

				bw = new BufferedWriter(new FileWriter(fichero, true));
				bw.write(simpleDateFormat.format(fecha) + " [" + address.getHostAddress() + "] " + "-" + aux + "\n");
				salida.flush();
				bw.close();
			}

			entrada.close();
			salida.close();
			client.close();
			server.close();
			System.out.println("Cliente desconectado");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			System.out.println("Cliente desconectado");
		}
	}
}