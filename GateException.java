import java.io.File;

class GateException extends RuntimeException {
  
  public GateException(String value){
    super(value);
  }
  /* Konstruktor för fel relaterade till en specifik rad i en given fil */
  public GateException(String value, File f, int lineNumber){
    super(value + "\nIn file: " + f.getName() + " at line: " + lineNumber);
  }
}