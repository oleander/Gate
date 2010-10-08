import java.io.File;

class GateException extends RuntimeException {
  
  public GateException(String value){
    super(value);
  }
  
  public GateException(String value, File f, int lineNumber){
    super(value + "\nIn file: " + f.getName() + " at line: " + lineNumber);
  }
}