import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.io.*;

 public class GateProg extends JFrame implements ActionListener {
   Map<String, Gate> gateMap;
   Set<Map.Entry<String, Gate>> gateEntries;
   Map<JRadioButton, SignalGate> inputMap = new LinkedHashMap<JRadioButton, SignalGate>();
   Map<JRadioButton, Gate> outputMap = new LinkedHashMap<JRadioButton, Gate>();
   Set<Map.Entry<JRadioButton, Gate>> outputEntries;
   JPanel p0 = new JPanel();
   JScrollPane sp = new JScrollPane(p0);
   JPanel p1 = new JPanel(), p2 = new JPanel();
   static final int DELAY = 100;
   javax.swing.Timer tim = new javax.swing.Timer(DELAY, this);

   public GateProg() {
     addWindowListener(wl);
     add(sp);
     p0.setLayout(new BorderLayout());
     p0.add(p1, BorderLayout.WEST);
     p0.add(p2, BorderLayout.EAST);
     p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS)); 
     p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS)); 
     p1.setBorder(new EtchedBorder());
     p2.setBorder(new EtchedBorder());
     String dir = System.getProperty("user.dir");
     JFileChooser fc = new JFileChooser(dir);
     int res = fc.showOpenDialog(null);
     if (res == JFileChooser.APPROVE_OPTION)
     try { 
       gateMap = Gate.createGates(fc.getSelectedFile());
       JLabel l = new JLabel(fc.getSelectedFile().getName(), JLabel.CENTER);
       l.setFont(new Font("SansSerif", Font.BOLD, 13));
       add(l, BorderLayout.NORTH);
       Font fo = new Font("Monospaced", Font.BOLD, 13);
       gateEntries = gateMap.entrySet(); 
       p1.add(Box.createVerticalGlue()); 
       for (Map.Entry<String, Gate> e : gateEntries) {
         String txt = e.getKey();
         Gate g = e.getValue();
         JRadioButton rb = new JRadioButton();
         rb.setFont(fo);
         if (g instanceof SignalGate) {
           rb.setText(txt);
           p1.add(rb);
           inputMap.put(rb, (SignalGate) g);
           ((SignalGate) g).setValue(false);
         }
         else {
           while(txt.length() < 6)
             txt += ' ';
           String type = g.getClass().getSimpleName();
           txt += type.substring(0, type.indexOf("Gate")).toUpperCase();
           for (Gate ga : g.getInputGates())
             txt += " " + ga.getName();
           rb.setText(txt);
           rb.setEnabled(false);
           p2.add(rb);
           outputMap.put(rb, g);
         }
         rb.addActionListener(this);
       }
       p1.add(Box.createVerticalGlue()); 
       outputEntries = outputMap.entrySet();
       for (Map.Entry<JRadioButton, Gate> e : outputEntries)
         e.getValue().init();
       Gate.setDelay(100);
       tim.start();          
       pack();
       centrera();
       setVisible(true); 
       setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
     }
     catch(GateException ie) {
       JOptionPane.showMessageDialog(null, ie.getMessage());
       dispose();
     }
     else        
       finish();
   }

   private void centrera() {
     Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
     setLocation((int)(d.getWidth()-getSize().getWidth())/2,
                (int)(d.getHeight()-getSize().getHeight())/2);
   }

   private void finish() {
     if (!(JOptionPane.showConfirmDialog(null, "Vill du öppna en annan fil?") == 0))
       System.exit(0);
     else
       new GateProg(); 
   }
             
   // lyssnarmetoder
   public void actionPerformed(ActionEvent ae) {
     if (ae.getSource() instanceof JRadioButton) {
       JRadioButton rb = (JRadioButton) ae.getSource();
       inputMap.get(rb).setValue(rb.isSelected());
     }
     else if (ae.getSource() == tim)
       for (Map.Entry<JRadioButton, Gate> e : outputEntries)
         e.getKey().setSelected(e.getValue().getOutputValue());
   } 

   private WindowListener wl = new WindowAdapter() {
     public void windowClosed(WindowEvent e) {
       finish();
     }
   };     

   public static void main (String[] arg) {
     new GateProg();
   }
 }

