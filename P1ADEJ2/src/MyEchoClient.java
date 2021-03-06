import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

public class MyEchoClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Socket client = null;
		PrintWriter streamSalida = null;
		Properties configuracion = new Properties();
		configuracion.setProperty("direccion", "127.0.0.1");
		configuracion.setProperty("puerto", "2222");
		try {
			configuracion.store(new FileOutputStream(".\\echo.props"), "Fichero de configuracion echo.props");
			// cargamos el fichero de propiedades
			configuracion.load(new FileInputStream(".\\echo.props"));

			// para saber las propiedades que haya dentro del fichero
			System.out.println("direccion=" + configuracion.getProperty("direccion"));

			client = new Socket(configuracion.getProperty("direccion"),
					Integer.parseInt(configuracion.getProperty("puerto")));

			System.out.println("Introduce el mensaje");
			BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
			streamSalida = new PrintWriter(client.getOutputStream(), true);
			BufferedReader streamEntrada = new BufferedReader(new InputStreamReader(client.getInputStream()));

			String linea = null;
			while ((linea = entrada.readLine()) != null) {
				streamSalida.println(linea);
				System.out.println(streamEntrada.readLine());
				streamSalida.flush();
				System.out.println("Introduce un nuevo mensaje");
			}
			streamEntrada.close();
			streamSalida.close();
			client.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				streamSalida.println("");
				client.close();
				System.out.println("Cliente cerrado");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}