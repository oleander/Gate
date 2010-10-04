public class GateTest3 {

  public static void main(String[] arg) {
    NandGate g1 = new NandGate(), g2 = new NandGate(), g3 = new NandGate();
    g1.setName("g1"); g2.setName("g2"); g3.setName("g3"); 
    NandGate  z  = new NandGate();
    z.setName("z");
    SignalGate a = new SignalGate(), b = new SignalGate(), c = new SignalGate();
    a.setName("a"); b.setName("b"); c.setName("c");
    g1.setInputGate(a); g1.setInputGate(b);
    g2.setInputGate(b); g2.setInputGate(c);
    g3.setInputGate(a); g3.setInputGate(c);
    z.setInputGate(g1); z.setInputGate(g2);  z.setInputGate(g3); 
    g1.init(); g2.init(); g3.init();
    z.init();
    Gate.setDelay(1); 
    System.out.println("a  b  c  z");
    for (int i=0; i<2; i++)
      for (int j=0; j<2; j++)
        for (int k=0; k<2; k++) {
        try {
          a.setValue(i==1);
          Thread.sleep(5);
          b.setValue(j==1);      
          Thread.sleep(5);
          c.setValue(k==1);      
          Thread.sleep(5);
        }
        catch (InterruptedException e) {}
        System.out.println
          (value(a) + "  " + value(b) + "  " + value(c) + "  " + value(z));
      }    
  }

  public static String value(Gate g) {
    return ((g.getOutputValue()) ? "1" : "0");
  }

}
