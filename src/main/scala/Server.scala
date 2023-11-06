import java.net._
import java.util.Scanner
import java.net.ServerSocket
import scala.collection.Map
import scala.jdk.CollectionConverters._
import java.util.concurrent.CopyOnWriteArrayList
import scala.collection.mutable.ListBuffer
import Array._
import scala.io.Source._
import sttp.client4.quick.*
import sttp.client4.Response
import org.json4s._
import org.json4s.native.JsonMethods._
import netscape.javascript.JSObject

object Server extends App {
    def GetWeather(location : String) = 
        {
            val response: Response[String] = quickRequest.get(uri"http://api.weatherapi.com/v1/current.json?key=2f2a9d8cb8d646feabf151702230211 &q=$location&aqi=no").send()
            val json = parse(response.body)
            val last_updated = for { JString(x) <- (json\"current"\"last_updated") } yield x
            val temp_c = for { JDouble(x) <- (json\"current"\"temp_c") } yield x
            val wind_dir = for { JString(x) <- (json\"current"\"wind_dir") } yield x
            val wind_kph = for { JDouble(x) <- (json\"current"\"wind_kph") } yield x
            val humidity = for { JInt(x) <- (json\"current"\"humidity") } yield x
            "location: " + location + "; last_updated: " + last_updated(0).toString + "; temp_c: " + temp_c(0).toString + "; wind_dir: " + 
                wind_dir(0).toString + "; wind_kph: " + wind_kph(0).toString + "; humidity: " + humidity(0).toString
        }

    println("Server is running")
    try 
        {
            val address = InetAddress.getByName("233.0.0.1")
            var packet: DatagramPacket = null
            val socket = new DatagramSocket
            while (true) 
                {
                    Thread.sleep(10000)
                    val source = scala.io.Source.fromFile("location.txt")
                    val location = try source.mkString finally source.close()
                    val weather = GetWeather(location)
                    val conversion_array: Array[Byte] = weather.getBytes("UTF-8")
                    println("Transmitting Weather:\n"+ weather)
                    packet = new DatagramPacket(conversion_array, conversion_array.length, address, 1502)
                    socket.send(packet)
                }
        } 
    catch 
        {
            case e: Exception => 
        }
}