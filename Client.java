import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;

public class Client extends JFrame {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //declare components
    private JLabel heading=new JLabel("Client");
    private JTextArea msgArea=new JTextArea();
    private JTextField msgInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    public Client()
    {
       
        try {
            System.out.println("Sending request to Server");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("connection done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            
            createGUI();
            handleEvents();
             startReading();
            // startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    private void handleEvents() {

        msgInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
               //System.out.println("key released "+e.getKeyCode());
               if(e.getKeyCode() == 10)
               {
                //System.out.println("you have pressed enter button");
                String contentToSend = msgInput.getText();
                msgArea.append("Me : "+contentToSend+"\n");
                out.println(contentToSend);
                out.flush();
                msgInput.setText("");
                msgInput.requestFocus();
               } 
            }

        });
    }

    private void createGUI()
    {
        this.setTitle("Client Messenger[END]");
        this.setSize(500,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //coding for component
        heading.setFont(font);
        msgArea.setFont(font);
        msgInput.setFont(font); 
        
        heading.setIcon(new ImageIcon("micon.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        msgArea.setEditable(false);
        msgInput.setHorizontalAlignment(SwingConstants.CENTER);

        //setting layout of the frame
        this.setLayout(new BorderLayout());
        
        //adding components to the frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(msgArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(msgInput,BorderLayout.SOUTH);

        this.setVisible(true);

    }


    public void startReading()
    {
        Runnable r1=()->{
            
            System.out.println("reader started..");
            
            
            try {
                while(true)
            {
               
                String msg = br.readLine();
               if(msg.equals("exit"))
               {
                   System.out.println("Server terminated the chat");
                   JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                   msgInput.setEnabled(false);
                   socket.close();
                   break;
               } 

               //System.out.println("Server : "+msg);
               msgArea.append("Server : "+msg+"\n");

            }
            } catch (Exception e) {
                System.out.println("connection closed");
            }

        };

        new Thread(r1).start();
    }

    public void startWriting()
    {
        Runnable r2=()->{
            System.out.println("Writer started.");
             try {
                while(!socket.isClosed())
                {
                   
   
                       BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                       String content =br1.readLine();
                       out.println(content);
                       out.flush();

                       if(content.equals("exit")){
                        socket.close();
                        break;
                     }
                       
                   
                }
             } catch (Exception e) {
                   System.out.println("connection closed");
             }


        };

        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("this is Client");
        new Client();
    }
}
