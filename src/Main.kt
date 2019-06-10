import java.net.URL
import java.net.HttpURLConnection
import com.google.gson.*

/**
 *Ejercicio de Kodemia
 *
 *@author Miguel Angel Osorio Cruz <ing.mosorio19@gmail.com>
 *
 *
 */
fun main(args: Array<String>) {
	try {
		val latitud = ""//ingrese Latitud
		val longitud = ""//ingrese longitud
		
		val response = makeHttpConnection(latitud, longitud)//hacemos el peticion y guardamos la respuesta
		
		val lista: List<Scooters> = if (response != null) {
			parseJSON(response)
		} else {
			 listOf<Scooters>()
		}
		
		val sortedByRange = lista.sortedWith(compareBy( {it.properties?.range })).reversed()//ordena por rango, de mayor a menor
		
		val sortedByDistance = lista.sortedWith(compareBy({it.properties?.distance }))//ordena de menor a mayor por distancia
		
		//mostramos los scooters ordenados por rango
		println("Listado por Rango")
		
		for(item: Scooters in sortedByRange) {
			println("Rendimiento: ${item.properties?.range} Tipo: ${item.type} Nivel de bateria: ${item.properties?.power}")
		}
		
		//Se recorren los scooters ordenados por distancia
		println("Listado por Distancia")
		
		for(item: Scooters in sortedByDistance) {
			println("Distancia: ${item.properties?.distance} Tipo: ${item.type} Nivel de bateria: ${item.properties?.power}")
		}
		
	} catch(e: Exception) {
		e.printStackTrace()
	}
}

/**
 *
 *Función que se encarga de hacer la conexion con el API
 *
 *@params latitud
 *@params longitud
 *
 *@return String Retorna la respuesta del servidor en el caso de algún fallo retorna un nulo
 */
fun makeHttpConnection(latitud: String, longitud: String) : String? {
	
	val URL_CONNECTION: String = "https://beta.econduce.mx/api/estaciones/get_nearest_map_features.json?latitude=$latitud&longitude=$longitud"
	val CONTENT_TYPE: String = "application/json"
	val API_KEY = ""//ingrese API Key
	
	val connection = URL(URL_CONNECTION).openConnection() as HttpURLConnection
	connection.requestMethod = "GET"
	connection.setRequestProperty("Content-Type", CONTENT_TYPE)
	connection.setRequestProperty("apiKey", API_KEY)
	
	try {
		
		val text: String = connection.inputStream.bufferedReader().readText()
		
		return text
		
	} catch(e: Exception) {
		e.printStackTrace()
		return null
	} finally {
		connection.disconnect()
	}
}

/**
 *Función que se encarga de hacer el parseo
 *
 *@params texto	obtiene el String de la respuesta dle servidor
 *
 *@return List<Scooters>	retorna la lista de Scooters disponibles
 */
fun parseJSON(texto: String) : List<Scooters> {
	try {
		return  Gson().fromJson(texto, Array<Scooters>::class.java).toList()
	} catch(e: Exception) {
		return listOf<Scooters>()
	}
}