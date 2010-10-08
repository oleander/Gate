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
  
  /* Sätter outputvärdet, meddelar till outputgates att deras input har förändrats */
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
  
  /* Returnernar en map med gatenamn och gatear från en config-fil */
  public static Map<String,Gate> createGates(File f) throws GateException {
    /* Innehåller resultatet parsningen */
    LinkedHashMap<String,Gate> output = new LinkedHashMap<String,Gate>();
    /* Innehåller parsade gatear och listor med namn på deras inputgatear */
    LinkedHashMap<Gate,ArrayList<String>> gateInputs = new LinkedHashMap<Gate,ArrayList<String>>();
    ArrayList<String> parsedStrings = new ArrayList<String>();
    String gateType;
    String gateName;
    String buffer = "";
    BufferedReader r;
    /* Håller reda på vilken rad i configfilen som bearbetas */
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
          lineNumber++;
          continue;
        }
      
        /* Parsar ut och formaterar alla 'ord' i den aktuella raden. */
        parsedStrings = gateParser(buffer);
        gateName = parsedStrings.get(0);
        gateType = parsedStrings.get(1);
      
        /* Skapar en gate med hjälp av gateName, som är gatens klassnamn */
        try {
          g = (Gate) Class.forName(gateType).newInstance();
        } catch(Exception e) {
          throw new GateException("Error: invalid gate type - " + e.getMessage(), f, lineNumber);
        }
        
        /* Ställer in gatens namn */
        g.setName(gateName);
      
        /* Placerar gate och namn i mappen, checkar först efter multipla gatear med samma namn */
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
    
    Gate g;
    lineNumber = 1;
    /* För varje gate i output-mappen går man igenom inputgates-listan för den gaten och lägger till dessa gatear genom att
       slå upp dem i output-mappen.
     */
    for(Map.Entry<String,Gate> entry : output.entrySet()) {
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
    output.set(1, toGateClassName(output.get(1)));
    return output;
  }
  
  /* Tar ett ord och gör om det till ett Gateklassnamn */
  private static String toGateClassName(String s) {
    Pattern p = Pattern.compile("([\\S]+)([Gg][Aa][Tt][Ee])");
    Matcher m = p.matcher(s);
    /* Om gatetypen redan innehåller Gate tar vi bort det */
    if (m.find()) {
      s = m.group(1);
    }
    char[] c = s.toLowerCase().toCharArray();
    c[0] = Character.toUpperCase(c[0]);
    s = new String(c) + "Gate";
    return s;
  }
  
}