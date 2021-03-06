import java.util.*;
import java.io.*;

public abstract class Gate {

  protected String name;
  protected boolean outputValue;
  protected ArrayList<Gate> outputGates;
  protected ArrayList<Gate> inputGates;
  protected static int delay;
  private File file;
  private int line;
  
  /* Innehåller en lista på ingående parametrar till griden i fråga 
     Listan gå inte att komma åt utifrån, då alla värden i listan är skräpvärden */
  private ArrayList<String> slug = null;
  
  public Gate(){
    this.inputGates = new ArrayList<Gate>();
    this.outputGates = new ArrayList<Gate>();
  }
  
  /**
  * Se spec
  */
  public void init(){
    this.inputChanged();
  }
  
  /**
  * Sparar undran skräpsträngar som är relaterade till Gaten
  * @return none
  * @param var, en lista på värden som är relaterade till Gaten
  */
  private void setSlug(ArrayList<String> slug){
    
    /* Plockar bort de två första värdena i lista då dessa innehåller
       namnet på gaten och vilken typ gaten är av */
    slug.remove(0); slug.remove(0);
    this.slug = slug;
  }
  
  /**
  * Se @return
  * @param none
  * @return en lista med ingående värden till gaten
  * Exempel: 
        Om en rad i en konfig-fil ser ut som följer;
        z  OR a1 a2
        Så sparas a1 och a2 i {slug}
  */
  private ArrayList<String> getSlug(){
    /* Sparar undran instansvariabeln
       Tömmer sedan instansen {slug} 
       Varvid vi säger till java att städa
       
       Optimalt? Ingen aning, men det lär inte bli värre än innan :)
       {slug} ska nämligen inte användas mer, 
       där av verkar det aningen onödigt att ha kvar värdet i instansen. */
    ArrayList<String> tmp = this.slug;
    this.slug = null;
    System.gc();
    
    return tmp;
  }
  
  /**
  * Se @param
  * Används av {equals}
  * @return En sträng-representation av griden
            Vilket i vårt fall är namnet på griden
  */
  public String toString(){
    return this.name;
  }
  
  /**
  * Kontrollerar ifall två objekt (Gate) är like
  * Används av {ArrayList.contains}
  * @param gate, Gaten som vi ska jämföra med
  * @return true om ingående objekt har samma sträng-representation som den nuvarande instansen
  */
  public boolean equals(Object gate){
    if(!(gate instanceof Gate)){
      return false;
    }
    
    return this.toString().equals(((Gate) gate).toString());
  }
  
  /**
  * Gör om {var} från slump-tecken till CamelCasing
  * Vid en tom ingående variable så kastas en IllegalArgument-exception
  * @param var, namnet på griden som ska skapas
  * @return var = signal => SignalGate
  */
  private static String createClass(String var) {
    if(var.length() == 0){
      throw new IllegalArgumentException();
    }
    return var.substring(0,1).toUpperCase() + var.substring(1).toLowerCase() + "Gate";
  }
  
  /**
  * Se spec
  */
  public static Map<String,Gate> createGates(File file){
    BufferedReader br                   = null;
    String strLine                      = null;
    String[] tmp                        = null;
    Gate gate                           = null;
    Map<String,Gate> gates              = new LinkedHashMap();
    int line = 0;
    
    try {
      br = new BufferedReader(new FileReader(file));
      while ((strLine = br.readLine()) != null) {
        
        /* Håller koll på vilken rad vi befinner oss i filen */
        line++;
        
        /* Delar ingående värden vid 'whitespace', då räknat tab och mellanslag */
        tmp = strLine.split("\\s+");
        
        /* Om strängen börjar med '/' eller '*' så hoppar vi vidare 
           Raden i fråga räknas då som en kommentar 
           Listan måste innehålla minst två värden */
        if(strLine.matches("^[/|\\*].+") || tmp.length < 2) continue;
        
        /* Finns redan griden i listan? */
        if(gates.containsValue(tmp[0])) throw new IllegalArgumentException();
        
        /* Gör om strängen {tmp[1]} från slump-tecken till CamelCasing */
        tmp[1] = Gate.createClass(tmp[1]);
        
        /* Skapar en instans av den angivna klassen */
        gate = (Gate) Class.forName(tmp[1]).newInstance();
        
        /* Sparar undran den nuvarande placeringen och filen */
        gate.line = line; gate.file = file;
        
        /* Sätter namnet på griden */
        gate.setName(tmp[0]);
        
        /* Sparar undan klassen */
        gates.put(tmp[0], gate);
        
        /* Sparar undan den andra temp-listan som en ArrayList i stället för en Array */
        gate.setSlug(new ArrayList(Arrays.asList(tmp)));
        
      }  
    } catch(FileNotFoundException e) {
      Gate.customErrorMessage(line, e, file, "Filen kunde inte hittas");
    } catch(IOException e){
      Gate.customErrorMessage(line, e, file, "Något gick fel vid inläsning");
    } catch(ClassNotFoundException e){
      Gate.customErrorMessage(line, e, file, "En utav grindarna som angavs hittades inte.");
    } catch(InstantiationException e){
      Gate.customErrorMessage(line, e, file, "En utav grindarna som angavs kunde inte tas i bruk");
    } catch(IllegalAccessException e){
      Gate.customErrorMessage(line, e, file, "Du har inte läs- / skriv-rättigheter till en utav grindarna");
    }
    
    Iterator first         = gates.entrySet().iterator();
    Gate superGate         = null;
    Gate incoming          = null;
    
    while (first.hasNext()) {
      
      /* Gate <==> ArrayList<String, Gate> */
      Map.Entry incomingGate = (Map.Entry) first.next();
      
      /* Innehåller nu Gate-objektet */
      superGate = (Gate) incomingGate.getValue();   
      
      for(String value : superGate.getSlug()) {
        
        /* Plockar ut gaten som {superGate} är relaterad till */
        incoming = (Gate) gates.get(value);
        
        /* Om inte ingången finns i gate-listan så 
           finns det helt enkelt inget att lägga till */
        if(incoming == null){
          Gate.customErrorMessage(superGate.line, new IllegalAccessException(), superGate.file, "Griden du angav finns inte");
        }
        
        /* Sätter den funna gaten som ingång */
        superGate.setInputGate(incoming);
      }
    }
    return gates;
  }
  
  /**
  * Printar ut ett felmeddelande på skärmen
  * @return none
  * @param line, raden där felet inträffade
  * @param e, felet som kastades av java
  * @param file, filen som genererade felet
  * @param custom, ett eget felmeddelande
  */
  public static void customErrorMessage(int line, Exception e, File file, String custom){
    throw new GateException(custom + ", filen " + file.getName() + ", rad " + Integer.toString(line) + ", fel: " + e.getMessage());
  }
  
  /* Abstrakta metoder */
  public abstract boolean calculateValue();
  public abstract void inputChanged();
  
  /* Se spec för mer info */
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
  
  public void setInputGate(Gate gate){
    /* Innehåller griden redan {gate} ? */
    if(this.inputGates.contains(gate)){
      Gate.customErrorMessage(this.line, new IllegalArgumentException(), this.file, "Ingången används redan");
    }
    
    /* Om inte så jobbar vi på som vanligt */
    this.inputGates.add(gate);
    gate.setOutputGate(this);
  }
  
  public List<Gate> getInputGates(){
    return (List<Gate>) this.inputGates;
  }
  
  public static void setDelay(int d){
    delay = d;
  }
  
  public static int getDelay(){
    return delay;
  }
  
  public void outputChanged(boolean value) {
    this.outputValue = value;
    for (Gate g : this.outputGates) {
      g.inputChanged();
    }
  }
  
  public void setOutputGate(Gate g) {
    this.outputGates.add(g);
  }
}