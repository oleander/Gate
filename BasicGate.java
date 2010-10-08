import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;

public abstract class BasicGate extends Gate implements ActionListener {
  private Timer timer = new Timer(delay,this);
  private boolean currentCalc;
  
  public BasicGate(){
    
  }
  
  public void inputChanged(){
    if(!this.timer.isRunning()){
      this.currentCalc = this.calculateValue();
      
      /* Är det nya värdet skilt från det nuvarande ? */
      if(this.getOutputValue() != this.currentCalc){
        this.timer.start();
      }
      
    }
  }
  
  public void actionPerformed(ActionEvent e){
    /* Är det verkligen ett timer-objekt som har kickat igång detta callback ? */
    if(!(e.getSource() instanceof Timer)){
      return;
    }
    /* Sparar undan värdet 
       Gör dock inte beräkningen igen, använder {currentCalc} */
    this.outputChanged(this.currentCalc);
    
    /* Stoppar timern, så att denna metod inte körs igenom om {delay} ms */
    ((Timer) e.getSource()).stop();
  }
}