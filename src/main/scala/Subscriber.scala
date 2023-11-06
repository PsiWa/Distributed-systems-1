import java.net._
import java.util.Scanner

object Subscriber extends App {
     try 
        {
            val socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999)
            val is = socket.getInputStream
            val arr = new Array[Byte](1024)
            var len = 0
            while (len != -1) 
                {
                    len = is.read(arr)
                    val msg = new String(arr, 0, len)
                    println(msg)
                } 
        }
    catch 
        {
            case e: Exception =>
        }

}