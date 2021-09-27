
package chata;
import javax.swing.*;
import javax.swing.border.*;  //for chat bubble
import java.awt.*;
import java.awt.event.*;  //key listener
import java.net.*; //for web socket
import java.io.*;  //has data input/output stream.Also file related queries will be done

import java.util.Calendar;  //for the timing
import java.text.SimpleDateFormat;  //for date format
public class Chata implements ActionListener{
    JPanel p1;
    JTextField t1;
    JButton b1;
    static JPanel a1;
    static JFrame f1 = new JFrame();
    
    static Box vertical = Box.createVerticalBox();   //to align Messages vertically
    
    static ServerSocket skt; //why static?
    static Socket s;  //The Socket class is used for communicate b/w client and server.
    static DataInputStream din;
    static DataOutputStream dout;
    
    Boolean typing;
    
    Chata(){
        f1.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        p1 = new JPanel();
        p1.setLayout(null);
        p1.setBackground(new Color(7, 94, 104));
        p1.setBounds(0, 0, 450, 70);     //set bounds
        f1.add(p1);
        
       ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("chata/icons/3.png"));
       Image i2 = i1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
       ImageIcon i3 = new ImageIcon(i2);
       JLabel l1 = new JLabel(i3);
       l1.setBounds(5, 17, 30, 30);
       p1.add(l1);
       
       l1.addMouseListener(new MouseAdapter(){
           public void mouseClicked(MouseEvent ae){
               System.exit(0);
           }
       });
       
       /*ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("chata/icons/.png"));
       Image i5 = i4.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
       ImageIcon i6 = new ImageIcon(i5);
       JLabel l2 = new JLabel(i6);
       l2.setBounds(40, 5, 60, 60);
       p1.add(l2);*/
       
       ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("chata/icons/video.png"));
       Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
       ImageIcon i9 = new ImageIcon(i8);
       JLabel l5 = new JLabel(i9);
       l5.setBounds(290, 20, 30, 30);
       p1.add(l5);
       
       ImageIcon i11 = new ImageIcon(ClassLoader.getSystemResource("chata/icons/phone.png"));
       Image i12 = i11.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
       ImageIcon i13 = new ImageIcon(i12);
       JLabel l6 = new JLabel(i13);
       l6.setBounds(350, 20, 35, 30);
       p1.add(l6);
       
       ImageIcon i14 = new ImageIcon(ClassLoader.getSystemResource("chata/icons/3icon.png"));
       Image i15 = i14.getImage().getScaledInstance(13, 25, Image.SCALE_DEFAULT);
       ImageIcon i16 = new ImageIcon(i15);
       JLabel l7 = new JLabel(i16);
       l7.setBounds(410, 20, 13, 25);
       p1.add(l7);
       
       
       JLabel l3 = new JLabel("Server");
       l3.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
       l3.setForeground(Color.WHITE);
       l3.setBounds(110, 15, 100, 18);
       p1.add(l3);   
       
       
       JLabel l4 = new JLabel("online");
       l4.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
       l4.setForeground(Color.WHITE);
       l4.setBounds(110, 35, 100, 20);
       p1.add(l4);  
       //--------------------------------------------
       //              online to Typing
       Timer t = new Timer(1, new ActionListener(){
           public void actionPerformed(ActionEvent ae){        //ae is ActionEvent Class ka object
               if(!typing){                                    //in java, by default boolean value is FALSE.
                   l4.setText("online");
               }                                
           }
       });
       
       t.setInitialDelay(2000);                               //2000 milisecond is the delay between active now and typing
       
       //----------------------------------------------
       a1 = new JPanel();
       //a1.setBounds(5, 75, 440, 570);
       a1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
       //f1.add(a1);
       
       JScrollPane sp = new JScrollPane(a1);
       sp.setBounds(5, 75, 440, 570);
       sp.setBorder(BorderFactory.createEmptyBorder());
       f1.add(sp);
       
       t1 = new JTextField();
       t1.setBounds(5, 655, 310, 40);
       t1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
       f1.add(t1);
       
       t1.addKeyListener(new KeyAdapter(){                  //learn
           public void keyPressed(KeyEvent ke){
               l4.setText("typing...");
               
               t.stop();                                    
               
               typing = true;                               //as we are typing it should be set as true.
           }
           
           public void keyReleased(KeyEvent ke){
               typing = false;                              //as we have stopped typing(Released the key, so typing is made as false.
               
               if(!t.isRunning()){
                   t.start();                               //learn
               }
           }
       });
       
       b1 = new JButton("Send");
       b1.setBounds(320, 655, 123, 40);
       b1.setBackground(new Color(7, 94, 84));
       b1.setForeground(Color.WHITE);
       b1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
       b1.addActionListener(this);
       f1.add(b1);
        
       f1.getContentPane().setBackground(Color.WHITE);
       f1.setLayout(null);
       f1.setSize(450, 700);
       f1.setLocation(400, 200); 
       f1.setUndecorated(true);
       f1.setVisible(true);
        
    }
    
    public void actionPerformed(ActionEvent ae){  
        try{
            String out = t1.getText();
            
            //sendTextToFile(out);
            
            JPanel p2 = formatLabel(out);                   // if we do not do this, then the chat bubble by default appends the message in the center of the text area
                                                            //
            a1.setLayout(new BorderLayout());               //
                                                            
            JPanel right = new JPanel(new BorderLayout());  //adds the chat bubble to the right.
            right.add(p2, BorderLayout.LINE_END);           //adds the chat bubble to the right as and when the message ends.
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));      //adds the gap between messages
            
            a1.add(vertical, BorderLayout.PAGE_START);      //goes on appending the message from the start
            
            //a1.add(p2);
            dout.writeUTF(out);  //to deliver the message to the client
            t1.setText("");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
   public void sendTextToFile(String message)throws FileNotFoundException{   //"throws" is used to manually throw the exception.
        
        
        try                                                           //did not understood,revise
            (FileWriter f = new FileWriter("chat.txt",true);
            PrintWriter p = new PrintWriter(new BufferedWriter(f));){ //learn about print Writer
            p.append("Gaitonde:"+message+"\n");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public static JPanel formatLabel(String out){   //to make chat bubbles
        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));   //to add the timing at the bottom of the box of the sent message.
        
        JLabel l1 = new JLabel("<html><p style = \"width : 150px\">"+out+"</p></html>");   //done because as the length message exceedes the limit,then the message will be broken.;\->backspace neglects the portion because style tage should always be written in double quotes. 
        l1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        l1.setBackground(new Color(37, 211, 102));  //for green color chat bubble
        l1.setOpaque(true);                         //if we do not do this then, the green bubble will not be applied.
        l1.setBorder(new EmptyBorder(15,15,15,50)); //to increase the size of chat bubble  from top 15,left 15, bottom 15, right 50.ie(Padding).
        
        Calendar cal = Calendar.getInstance();                  //making an object of calander class
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");   //to mention the format of the timing hh->hour,mm->minutes
        
        JLabel l2 = new JLabel();
        l2.setText(sdf.format(cal.getTime()));     //to get the the time of the message sent.
        
        p3.add(l1);
        p3.add(l2);
        return p3;
    }
    
    public static void main(String[] args){
        new Chata().f1.setVisible(true);
        
        String msginput = "";
        try{
            skt = new ServerSocket(6001);
            s = skt.accept();                                   //accepts the connection and returns the instance at socket side.
            while(true){                                        //till the connection is valid we send and receive messages.
                
                din = new DataInputStream(s.getInputStream());  //as the s.getInputStream() returns the input stream,using data input stream ill be reading the data.
                                                                
                                                                 
                                                                 
                                                                
                dout = new DataOutputStream(s.getOutputStream());  //This method returns an OutputStream where the data can be written
                
                
	        while(true){ //used to append received messages onto the frame.For appending sent messages,go to line no:153.
                        System.out.println(dout);                                        
                        JPanel p2 = formatLabel(msginput);
                        
                        JPanel left = new JPanel(new BorderLayout());
                        left.add(p2, BorderLayout.LINE_START);  //LINE_START because if u mention end,then the messages will keep appending in a straight line.
                        vertical.add(left);
                        f1.validate();   
            	}
                
            }
            
        }catch(Exception e){}
    }   
}

