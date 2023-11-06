import java.net._
import java.util.Scanner
import java.util.concurrent.CopyOnWriteArrayList
import scala.jdk.CollectionConverters._

object Client extends App {
  lazy val weather_buffer = new CopyOnWriteArrayList[String]().asScala
  new Thread(() => 
    {
      try 
        {
        val serverSocket = new ServerSocket(9999)
        while (true) 
          {
            val accept = serverSocket.accept
            val ip = accept.getInetAddress.getHostAddress
            val port = accept.getPort
            val user = ip + ":" + port
            System.out.println("User connected:" + user)
            val os = accept.getOutputStream
            if (weather_buffer.nonEmpty)
              {
                var i = 0;
                for (w <- weather_buffer.reverse; if i < 5) 
                  {
                    os.write((w + "\n").getBytes)
                    i += 1
                  }
              }
            os.close()
            accept.close()
          }
        } 
      catch 
        {
        case e: Exception => println(e)
        }
    }).start()

  try 
    {
      var buffer: Array[Byte] = null
      var packet: DatagramPacket = null
      var str: String = null
      var udp_socket = new MulticastSocket(1502)
      var address = InetAddress.getByName("233.0.0.1")
      udp_socket.joinGroup(address)

      while (true) 
        {
          System.out.println("\n\nListening for weather update\n")
          buffer = new Array[Byte](256)
          packet = new DatagramPacket(buffer, buffer.length)
          udp_socket.receive(packet)
          str = new String(packet.getData).trim
          if (!weather_buffer.contains(str)) weather_buffer.addOne(str)
          for (w <- weather_buffer) println(w)
        }
    } 
  catch 
    {
    case e: Exception => println("Had an IOException trying to read that file")
    }
}