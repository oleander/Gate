import java.util.*;
import java.io.*;

public abstract class Gate {

  protected String name;
  protected boolean outputValue;
  protected ArrayList<Gate> outputGates;
  protected ArrayList<Gate> inputGates;
  protected static int delay;
  
  /* Innehåller en lista på ingående parametrar till griden i fråga 
     Listan gå inte att komma åt utifrån, då alla värden i listan är skräpvärden */
  private ArrayList<String> slug = null;
  
  public Gate(){
    this.inputGates = new ArrayList<Gate>();
    this.outputGates = new ArrayList<Gate>();
  }
  
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
    return this.slug;
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
  
  public void setInputGate(Gate gate){
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
           Listan måste innehålla minst två värden
           Det får heller inte finnas en gate med samma namn i listan */
        if(strLine.matches("^[/|\\*].+") || tmp.length < 2 || gates.containsValue(tmp[1])){
          continue;
        }
        
        /* Gör om strängen {tmp[1]} från slump-tecken till CamelCasing */
        tmp[1] = Gate.createClass(tmp[1]);
        
        /* Skapar en instans av den angivna klassen */
        gate = (Gate) Class.forName(tmp[1]).newInstance();
        
        /* Sätter namnet på griden */
        gate.setName(tmp[0]);
        
        /* Sparar undran klassen */
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
    } catch (Exception e){
      Gate.customErrorMessage(line, e, file, "Okänt fel har uppstått");
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
  
  public abstract boolean calculateValue();
  public abstract void inputChanged();
  
}