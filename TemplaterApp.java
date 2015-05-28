
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nazmulchowdhury
 */

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import com.googlecode.javacv.cpp.*;
import com.googlecode.javacpp.Loader;

public class TemplaterApp extends javax.swing.JFrame implements java.awt.event.ActionListener {

    /**
     * Creates new form TemplaterApp
     */
    public TemplaterApp() {
        
        // Preload the opencv_objdetect module to work around a known bug.
        Loader.load(opencv_objdetect.class);
        
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
        
        thnTdP = new ImagePanel(PANEL_WIDTH,PANEL_HEIGHT);
        thnTd = new javax.swing.JScrollPane(thnTdP);
        upload = new javax.swing.JButton();
        crttmpt = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        fpv = new javax.swing.JLabel();
        mainImgP = new ImagePanel(PANEL_WIDTH,PANEL_HEIGHT);
        mainImg = new javax.swing.JScrollPane(mainImgP);
        scanPanel = new ScanPanel(thnTdP);
        

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("fp.jpg")).getImage());

        upload.setFont(new java.awt.Font("Liberation Mono", 1, 16)); // NOI18N
        upload.setText("Upload");
        upload.setFocusable(false);
        upload.addActionListener(this);

        crttmpt.setFont(new java.awt.Font("Liberation Mono", 1, 16)); // NOI18N
        crttmpt.setText("Create Template");
        crttmpt.setFocusable(false);
        crttmpt.addActionListener(this);

        cancel.setFont(new java.awt.Font("Liberation Mono", 1, 16)); // NOI18N
        cancel.setText("Cancel");
        cancel.setFocusable(false);
        cancel.addActionListener(this);

        fpv.setFont(new java.awt.Font("Liberation Mono", 1, 16)); // NOI18N
        fpv.setText("Fingerprint Template Creation");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(upload)
                        .addGap(60, 60, 60)
                        .addComponent(crttmpt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mainImg, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(thnTd, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fpv, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fpv, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(mainImg, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(thnTd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(upload)
                    .addComponent(crttmpt)
                    .addComponent(cancel))
                .addGap(46, 46, 46))
        );

        pack();
        setTitle("Fingerprint Recognition System");
        setResizable(false);
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e)
    {
        if (e.getSource() == upload)
        {
            JFileChooser jfc = new JFileChooser(CLAIMER);
            jfc.setAcceptAllFileFilterUsed(true);
            
            try
            {
                if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)       
                {
                    String fnm = jfc.getSelectedFile().getPath();
                    printName  = jfc.getSelectedFile().getName();
                    System.out.println(printName);
                    image = javax.imageio.ImageIO.read(new File(fnm));
                    mainImgP.setImage(image);
                    scanPanel.getAbsPath(fnm);
                }
            }
            catch (HeadlessException | IOException et)
            {
                System.exit(0);
            }
        }
        else if (e.getSource() == crttmpt)
        {
            if (image == null)
                return;
            scanPanel.findPrint();
            scanPanel.analyzePrint(printName);
        }
        else if (e.getSource() == cancel)
        {
            new FPRecognizer().setVisible(true);
            this.setVisible(false);
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JButton crttmpt;
    private javax.swing.JLabel fpv;
    private javax.swing.JScrollPane mainImg;
    private javax.swing.JScrollPane thnTd;
    private javax.swing.JButton upload;
    private ScanPanel scanPanel;
    private ImagePanel mainImgP,thnTdP;
    private java.awt.image.BufferedImage image;
    public static final String CLAIMER = "claimer/";
    private static final int PANEL_WIDTH = 450;
    private static final int PANEL_HEIGHT = 350;
    String printName;
    // End of variables declaration//GEN-END:variables
}