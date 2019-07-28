package de.felixklauke.chiara.bukkit;

import java.io.IOException;
import java.net.Socket;

public class Test {

  public static void main(String[] args) {
    System.out.println("CONNECT");
    try {
      Socket socket = new Socket("ldap.google.com", 636);
      System.out.println(socket.isClosed());
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("NOPE");
  }
}
