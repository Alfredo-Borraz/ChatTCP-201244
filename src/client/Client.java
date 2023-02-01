package client;
 import java.awt.*;

 import java.awt.event.KeyAdapter;
 import java.awt.event.KeyEvent;
 import java.io.BufferedReader;
 import java.io.InputStreamReader;
 import java.io.PrintWriter;
 import java.net.Socket;
 import java.util.ArrayList;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;


 import javax.swing.GroupLayout;
 import javax.swing.GroupLayout.Alignment;
 import javax.swing.JOptionPane;
 import javax.swing.JTextArea;
 import javax.swing.LayoutStyle.ComponentPlacement;

public class Client extends javax.swing.JFrame{

    private static long serialVersionUID = 1L;

    String username, address = "localhost";

    ArrayList<String>users = new ArrayList<>();

    int port = 4000;
    boolean isConnected = false;
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    public void ListenThread(){
        Thread IncomingReader = new Thread(new IncomingReader());
        IncomingReader.start();
    }

    public void userAdd(String data){
        users.add(data);
    }

    public void userRemove(String data){
        users.remove(data);
    }

    public void sendDisconnect(){
        String Disconnect = (username + ": :Disconnect");
        try {
            writer.println(Disconnect);
            writer.flush();
        }catch (Exception ex){
            ta_chat.append("Could not send Disconnect message \n");
        }
    }

    public void Disconnect(){
        try {
            ta_chat.append("Disconnected. \n");
            isConnected = false;
            tf_username.setEditable(true);
        }catch (Exception ex){
            ta_chat.append("Failed to disconnect. \n");
        }
    }

    public Client(){
        getContentPane().setForeground(new Color(0, 0, 0));
        initComponents();
        this.getContentPane().setBackground(new Color(211, 211, 211));
    }

    public class IncomingReader implements Runnable{
        @Override
        public void run() {
            String[] data;
            String stream, connect = "Connect", disconnect = "Disconnect", chat = "Chat", privatemsg = "private";

            try {
                while ((stream = reader.readLine()) != null){
                    data = stream.split(":");
                    
                    if (data[2].equals(chat)){
                        ta_chat.append(data[0] + ": "+data[1] + "\n");
                        ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
                    } else if (data[2].equals(connect)) {
                        ta_chat.removeAll();
                        userAdd(data[0]);
                    } else if (data[2].equals(disconnect)) {
                        userRemove(data[0]);
                    } else if (data[2].equals(privatemsg)) {
                        ta_chat.append("Private Message from" + data[0] + " : " + data[1] +"\n");
                        ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
                    } else if (data[2].equals("request")) {
                        ta_chat.append("Server replied" + "\n" + data[1] +"\n");
                        ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
                    }
                }
            }catch(Exception ex){}
        }
    }

    private void initComponents(){

        lb_address = new javax.swing.JLabel();
        tf_address = new javax.swing.JTextField();
        tf_address.setBackground(SystemColor.info);
        lb_port = new javax.swing.JLabel();
        tf_port = new javax.swing.JTextField();
        tf_port.setEditable(false);
        tf_port.setBackground(SystemColor.info);
        lb_username = new javax.swing.JLabel();
        tf_username = new javax.swing.JTextField();
        tf_username.setBackground(SystemColor.info);
        b_connect = new javax.swing.JButton();
        b_connect.setBackground(Color.pink);
        b_disconnect = new javax.swing.JButton();
        b_disconnect.setBackground(Color.pink);
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        ta_chat.setEditable(false);
        ta_chat.setBackground(SystemColor.info);
        tf_chat = new javax.swing.JTextField();
        tf_chat.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    send_it();
                }
            }
        });
        tf_chat.setBackground(SystemColor.info);
        b_send = new javax.swing.JButton();
        b_send.setBackground(Color.orange);
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Client's");
        setBackground(java.awt.SystemColor.textHighlight);
        setForeground(new java.awt.Color(0, 51, 51));
        setName("client");
        setResizable(false);

        lb_address.setText("Server IP : ");

        tf_address.setText("127.0.0.1");
        tf_address.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                tf_addressActionPerformed(evt);
            }
        });


        lb_port.setText("Port: ");
        tf_port.setText("4000");
        tf_port.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                tf_portActionPerformed(evt);
            }
        });

        lb_username.setText("Client Name: ");

        tf_username.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                tf_usernameActionPerformed(evt);
            }
        });

        b_connect.setText("Connecnt");
        b_connect.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                b_connectActionPerformed(evt);
            }
        });

        b_disconnect.setText("Disconnect");
        b_disconnect.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                b_disconnectActionPerformed(evt);
            }
        });

        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);

        b_send.setText("Send");
        b_send.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                b_sendActionPerformed(evt);
            }
        });

        jLabel2.setText("Chat area");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 473, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(tf_chat, 473, 473, 473))
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                                                        .addComponent(b_send, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                                                        .addComponent(b_disconnect, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(b_connect, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                                                        ))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
                                                                        .addComponent(lb_username, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(lb_address, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addGap(18)
                                                                .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                                                                        .addComponent(tf_address, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                                                        .addComponent(tf_username)))
                                                        .addComponent(jLabel2))
                                                .addPreferredGap(ComponentPlacement.RELATED, 320, Short.MAX_VALUE)
                                                .addComponent(lb_port)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(tf_port, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lb_address)
                                        .addComponent(tf_address, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lb_port)
                                        .addComponent(tf_port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(tf_username, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lb_username))
                                .addGap(8)
                                .addComponent(jLabel2)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(b_connect, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                                .addGap(15)
                                                .addComponent(b_disconnect, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                                .addGap(15)
                                                .addGap(15)
                                                .addGap(15)
                                                )
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(tf_chat, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                                        .addComponent(b_send, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                                .addContainerGap())
        );
        getContentPane().setLayout(layout);
        pack();
    }

    private void tf_addressActionPerformed(java.awt.event.ActionEvent evt){}

    private void tf_portActionPerformed(java.awt.event.ActionEvent evt){}

    private void tf_usernameActionPerformed(java.awt.event.ActionEvent evt){}

    private void b_connectActionPerformed(java.awt.event.ActionEvent evt){
        if (isConnected == false){
            if (tf_username.getText().length()!=0){

                if (!users.contains(tf_username.getText())){
                    username = tf_username.getText();
                    tf_username.setEditable(false);
                    tf_port.setEditable(false);
                    tf_address.setEditable(false);
                    ta_chat.setEditable(false);

                    try {
                        socket = new Socket(address, port);
                        InputStreamReader streamreader = new InputStreamReader(socket.getInputStream());
                        reader = new BufferedReader(streamreader);
                        writer = new PrintWriter(socket.getOutputStream());
                        writer.println(username + ": has connected : Connect");
                        writer.flush();
                        isConnected = true;
                        changetxtareafontsize(ta_chat);
                    }catch(Exception ex){
                        ta_chat.append("Cannot Connect! Try Again. \n");
                        tf_username.setEditable(true);
                    }
                    ListenThread();
                } else {
                    JOptionPane.showMessageDialog(null, "Write another name for your Client, this name is already taken","Duplicate name found !!!", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Write a name for your Client first", "Missing Field !!!", JOptionPane.ERROR_MESSAGE);
            }
        } else if (isConnected == true) {
            ta_chat.append("You are already connected. \n");
        }
    }

    private void b_disconnectActionPerformed(java.awt.event.ActionEvent evt){
        sendDisconnect();
        Disconnect();
    }

    private void b_sendActionPerformed(java.awt.event.ActionEvent evt){
        send_it();
    }

    public void send_it(){
        String nothing = "";
        String mydata = tf_chat.getText();
        Pattern pattern = Pattern.compile("(@).*\\\\1");
        Matcher matcher = pattern.matcher(mydata);

        if ((tf_chat.getText()).equals(nothing)){
            tf_chat.setText("");
            tf_chat.requestFocus();
        } else if (matcher.find()) {

            String[] data = mydata.split("@");

            try {
                writer.println(username + ":" + data[2] + ":" + "private" + ":" + data[1]);
                writer.flush();
                ta_chat.append("You have sent {  "+data[2]+"  } as a private message to : " + "'"+data[1]+ "'" +"\n");
            }catch (Exception ex){
                ta_chat.append("Message was not sent. \n"+"No online users found by that name  ");
            }
        }
        else {
            try {
                writer.println(username + ":" + tf_chat.getText() + ":" + "Chat");
                writer.flush();
            }catch(Exception ex){
                ta_chat.append("Message was not sent. \n");
            }
            tf_chat.setText("");
            tf_chat.requestFocus();
        }

        tf_chat.setText("");
        tf_chat.requestFocus();
    }

    public static void main(String args[]){
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client().setVisible(true);
            }
        });
    }

    public void changetxtareafontsize(JTextArea txtarea){
        Font font1 = new Font("SansSerif", Font.BOLD, 11);
        txtarea.setFont(font1);
    }



    private javax.swing.JButton b_connect;
    private javax.swing.JButton b_disconnect;
    private javax.swing.JButton b_send;

    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_address;
    private javax.swing.JLabel lb_port;
    private javax.swing.JLabel lb_username;
    private javax.swing.JTextArea ta_chat;
    private javax.swing.JTextField tf_address;
    private javax.swing.JTextField tf_chat;
    private javax.swing.JTextField tf_port;
    private javax.swing.JTextField tf_username;





}
