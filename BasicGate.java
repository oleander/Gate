import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;

public abstract class BasicGate extends Gate implements ActionListener {
  
  /* Skapar ett timerobjekt, skulle lika gärna kunna ligga i en konstruktor
     Tiden sätts i storheten ms i respektive grind */
  private Timer timer = new Timer(delay,this);
  
  /* En mellanlagring för det beräknade värdet */
  private boolean currentCalc;
  
  public void inputChanged(){
    /* Om INTE timern körs så görs beräkningen */
    if(!this.timer.isRunning()){
      this.currentCalc = this.calculateValue();
      
      /* Är det nya värdet skilt från det nuvarande ? */
      if(this.getOutputValue() != this.currentCalc){
        this.timer.start();
      }
    }
  }
  
  /**
  * Anropas efter {delay} ms
  * Publicerar beräknat värde {currentCalc}
  * @param e, Bör vara av klassen Timer, stämmer inte detta så ignoreras callbacket.
  * @return none
  */
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