package utilidades;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class BasesDatos {
	//devuelve un array de cinco cadenas con los datos de configuraciÛn validados
	public static List<String> leerConfig(String ruta) throws IOException, ParseException  {
		List<String> lineas = null;
		try {
			lineas = Files.readAllLines(Paths.get(ruta), Charset.forName("UTF-8"));
			if(lineas.size() !=5) {
				
				throw new ParseException("Fichero incorrecto, tiene que tener cinco l√≠neas", 0);
			}
			//si falla levanta excepciÛn NumberFormat
			Integer.parseInt(lineas.get(1));
			
		} catch (IOException|NumberFormatException e) {
			if(e instanceof IOException)
				throw new IOException("El fichero config.text no existe");
			if(e instanceof NumberFormatException)
				throw new NumberFormatException("la segunda l√≠nea debe ser el n√∫mero de puerto");
		}
		//validar la configuraciÛn

		return lineas;
	}
	//llama a leerConfig y si no hay excepciones crea la conexi√≥n con los datos
	public static Connection initBD(String ruta) throws ClassNotFoundException, IOException, SQLException, ParseException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		String driver = "jdbc:oracle:thin:";
		Connection conn = null;
		String  ip, puerto, sid, usuario, clave, cadena;
		List<String> config;
		/* leer datos*/
		
		config = leerConfig(ruta);
		/* separar los campos*/
		ip = config.get(0);
		puerto = config.get(1);
		sid = config.get(2);
		usuario = config.get(3);
		clave = config.get(4);
		/* montar la cadena*/
		cadena = driver + "@" + ip + ":" + puerto + ":" + sid;
		/* conectar con la base de datos*/

		conn = DriverManager.getConnection(cadena, usuario, clave);
		return conn;
	}
}
