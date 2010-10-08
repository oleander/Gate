import java.io.*;
public class Main{
  public static void main(String[] args){
    File file = new File("dlatch.txt");
    Gate.createGates(file);
  }
}