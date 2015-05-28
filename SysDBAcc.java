
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nazmulchowdhury
 */
public class SysDBAcc extends javax.swing.JFrame implements java.awt.event.ActionListener {

    /**
     * Creates new form SysDBAcc
     */
    public SysDBAcc() {
        this.loginClic = new javax.swing.AbstractAction("Login")
        {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                loginActionPerformed(e);
            }
        };
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title = new javax.swing.JLabel();
        usrnam = new javax.swing.JLabel();
        passwd = new javax.swing.JLabel();
        usrnamf = new javax.swing.JTextField();
        passwdf = new javax.swing.JPasswordField();
        cancel = new javax.swing.JButton();
        login = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("fp.jpg")).getImage());

        title.setFont(new java.awt.Font("Liberation Mono", 1, 16)); // NOI18N
        title.setText("System Database Access");

        usrnam.setFont(new java.awt.Font("Liberation Mono", 1, 16)); // NOI18N
        usrnam.setText("Username :");

        passwd.setFont(new java.awt.Font("Liberation Mono", 1, 16)); // NOI18N
        passwd.setText("Password :");

        usrnamf.setFont(new java.awt.Font("Liberation Mono", 1, 16)); // NOI18N        
        usrnamf.setAction(loginClic);

        passwdf.setFont(new java.awt.Font("Liberation Mono", 1, 16)); // NOI18N
        passwdf.setAction(loginClic);

        cancel.setFont(new java.awt.Font("Liberation Mono", 1, 16)); // NOI18N
        cancel.setText("Cancel");
        cancel.setFocusable(false);
        cancel.addActionListener(this);

        login.setFont(new java.awt.Font("Liberation Mono", 1, 16)); // NOI18N
        login.setFocusable(false);
        login.setAction(loginClic);
        login.getInputMap().put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER,0), null);
        login.getActionMap().put(null,loginClic);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(92, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cancel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(login))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(passwd)
                                    .addComponent(usrnam))
                                .addGap(84, 84, 84)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(usrnamf)
                                    .addComponent(passwdf, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))))
                        .addContainerGap(85, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(122, 122, 122))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usrnam)
                    .addComponent(usrnamf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwdf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwd))
                .addGap(93, 93, 93)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancel)
                    .addComponent(login))
                .addContainerGap(64, Short.MAX_VALUE))
        );

        pack();
        setTitle("Fingerprint Authentication System");
        setResizable(false);
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e)
    {
        if (e.getSource() == cancel)
        {
            new FPRecognizer().setVisible(true);
            this.setVisible(false);
        }
    }
    
    private void loginActionPerformed(java.awt.event.ActionEvent evt)
    {
        String USR = usrnamf.getText();
        String PSW = passwdf.getText();
        usrnamf.setText(null);
        passwdf.setText(null);
        
        javax.swing.JLabel message1 = new javax.swing.JLabel("Username and Password must be filled");
        javax.swing.JLabel message2 = new javax.swing.JLabel("Wrong username or password");
        
        if(USR.length() == 0 && PSW.length() == 0)
            javax.swing.JOptionPane.showMessageDialog(null, message1, "Warning", 2);
        else if (USR.equals("root") && PSW.equals("root"))
        {
            new TemplaterApp().setVisible(true);
            this.setVisible(false);
        }
        else
            javax.swing.JOptionPane.showMessageDialog(null, message2, "Warning", 2);
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Action loginClic;
    private javax.swing.JButton cancel;
    private javax.swing.JButton login;
    private javax.swing.JLabel passwd;
    private javax.swing.JPasswordField passwdf;
    private javax.swing.JLabel title;
    private javax.swing.JLabel usrnam;
    private javax.swing.JTextField usrnamf;
    // End of variables declaration//GEN-END:variables
}