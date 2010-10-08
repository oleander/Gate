import java.util.*;
import java.io.*;
import java.util.regex.*;
import java.lang.Class;

public abstract class Gate {

  protected String name;
  protected boolean outputValue;
  protected ArrayList<Gate> outputGates;
  protected ArrayList<Gate> inputGates;
  protected static int delay;
  
  public Gate(){
    this.inputGates = new ArrayList<Gate>();
    this.outputGates = new ArrayList<Gate>();
  }
  
  public Gate(String theName) {
    this();
    this.setName(theName);
  }
  
  public void init(){
    this.inputChanged();
  }
  
  public String getName(){
    return this.name;
  }
  
  public void setName(String name){
    this.name = name;
  }
  
  public boolean getOutputValue(){
    return this.outputValue;
  }
  
  public void setOutputValue(boolean value){
    this.outputValue = value;
  }
  
  /* Lägger till en gate i this inputlista, men även this i argumentgatens outputlista. */
  public void setInputGate(Gate gate){
    this.inputGates.add(gate);
    gate.setOutputGate(this);
  }
  
  public List<Gate> getInputGates(){
    return (List<Gate>) this.inputGates;
  }
  
  public abstract boolean calculateValue();
  
  public static void setDelay(int d){
    delay = d;
  }
  
  public static int getDelay(){
    return delay;
  }
  
  protected void outputChanged(boolean value) {
    this.outputValue = value;
    for (Gate g : this.outputGates) {
      g.inputChanged();
    }
  }
  
  protected void setOutputGate(Gate g) {
    this.outputGates.add(g);
  }
  
  
  public abstract void inputChanged();
  
  public static Map<String,Gate> createGates(File f) throws GateException {
    LinkedHashMap<String,Gate> output = new LinkedHashMap<String,Gate>();
    LinkedHashMap<Gate,ArrayList<String>> gateInputs = new LinkedHashMap<Gate,ArrayList<String>>();
    ArrayList<String> parsedStrings = new ArrayList<String>();
    String gateType;
    String gateName;
    String buffer = "";
    BufferedReader r;
    int lineNumber = 1;
    
    try {
      r = new BufferedReader (new FileReader(f));
    } catch (Exception e) {
      throw new GateException("Error: Could not read file: " + e.getMessage());
    }
    
    /* Går igenom filen rad för rad. */
    try {
      while ((buffer = r.readLine()) != null) {
        Gate g;
      
        /* Om raden börjar med * eller / hoppar vi över den. */
        if (buffer.startsWith("*") || buffer.startsWith("/")) {
          continue;
        }
      
        /* Parsar ut och formaterar alla 'ord' i den aktuella raden. */
        parsedStrings = gateParser(buffer);
        gateName = parsedStrings.get(0);
        gateType = parsedStrings.get(1);
      
        /* Skapar en gate med hjälp av ord 2, som är gatens klassnamn */
        try {
          g = (Gate) Class.forName(gateType).newInstance();
        } catch(Exception e) {
          throw new GateException("Error: invalid gate type - " + e.getMessage(), f, lineNumber);
        }
        
        /* Ställer in gatens namn */
        g.setName(gateName);
      
        /* Placerar gate och namn i mappen */
        if (output.containsKey(gateName)) {
          throw new GateException("Error: several gates with matching names.", f, lineNumber);
        } else {
         output.put(gateName, g); 
        }
      
        /* Tar bort gatens namn och klass */
        parsedStrings.remove(0); parsedStrings.remove(0);
      
        /* Resterande ord i listan är gatens inputgates */
        ArrayList<String> inputGates = new ArrayList<String>();
        if (!parsedStrings.isEmpty()) {
          for (String s : parsedStrings) {
            inputGates.add(s);
          }
        }
        gateInputs.put(g, inputGates);
      
        /* Tömmer listan med ord inför nästa iterering */
        parsedStrings.clear();
        
        lineNumber++;
      }
    } catch (Exception e){
      throw new GateException(e.getMessage());
    }
    
    /* Ställer in inputgates för alla gatear */
    Gate g;
    lineNumber = 1;
    for(Map.Entry<String,Gate> entry : output.entrySet()) {
      /* Objekten som vi itererar över är av typen Map.Entry (måste castas) som innehåller både nyckel och värde */
      g = entry.getValue();
      for (String s : gateInputs.get(g)) {
        try { 
          g.setInputGate(output.get(s));
          lineNumber++;
        } catch (Exception e) {
          throw new GateException (e.getMessage(), f, lineNumber);
        }
      }
    }
    return output;
  }
  
  /* Parsar ut ord ur en textrad. Ord 2 formateras om så att det matchar klassnamnet på en gate. */
  private static ArrayList<String> gateParser (String s) {
    Pattern p = Pattern.compile("([\\S]+)");
    Matcher m = p.matcher(s);
    ArrayList<String> output = new ArrayList<String>();
    
    /* Matcharen hittar ett ord åt gången */
    while (m.find()) {
      output.add(m.group(0));
    }
    
    /* Ord två bör vara gatetypen. Vi använder capitalize och lägger till Gate för att typen ska matcha classnamnet. */
    output.set(1, capitalize(output.get(1).toLowerCase()) + "Gate");
    return output;
  }
  
  private static String capitalize(String s) {
    char[] c = s.toCharArray();
    c[0] = Character.toUpperCase(c[0]);
    return new String(c);
  }
  
  // public static void main(String[] args) {
  //   File f = new File("xor.txt");
  //   createGates(f);
  // }
  
}