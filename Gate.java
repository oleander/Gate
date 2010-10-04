import java.util.ArrayList;

public class Gate {
  private String name;
  private boolean outputValue;
  private ArrayList<Gate> outputGates;
  private ArrayList<Gate> inputGates;
  private static int delay;
  
  public Gate(){
    
  }
  
  public void init(){
    
  }
  
  public String getName(){
    return "";
  }
  
  public void setName(String name){
    
  }
  
  public boolean getOutputValue(){
    return true;
  }
  
  public void setOutputValue(boolean value){
    this.outputValue = value;
  }
  
  public void setInputGate(Gate gate){
   this.inputGates.add(gate);
  }
  
  public ArrayList<Gate> getInputGates(){
    return this.inputGates;
  }
  
  public boolean calculateValue(){
    return true;
  }
  
  public void setDelay(int delay){
    this.delay = delay;
  }
  
  public int getDelay(){
    return this.delay;
  }
}