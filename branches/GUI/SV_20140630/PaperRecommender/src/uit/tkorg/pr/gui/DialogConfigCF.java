/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.gui;

import java.util.HashMap;
import java.util.HashSet;
import javax.swing.UIManager;

/**
 *
 * @author Zoe
 */
public class DialogConfigCF extends javax.swing.JDialog {

    /**
     * Creates new form configurationCFCosineGui
     */
//    int cfMethod = 1;//1: KNN Pearson, 2: KNN Cosine, 3: SVD
//    int kNeighbourhood = 8;// number of neighbhood
    int f = 5;//SVD
    double l = 0.001;//SVD
    int i = 100;//SVD

    DialogConfigCFPearson dialogConfigCFPearson = new DialogConfigCFPearson(null, rootPaneCheckingEnabled);
    DialogConfigCFCosine dialogConfigCFCosine = new DialogConfigCFCosine(null, rootPaneCheckingEnabled);
    DialogConfigCFSVD dialogConfigCFSVD = new DialogConfigCFSVD(null, rootPaneCheckingEnabled);

    HashSet<Integer> cfMethodHS = new HashSet<>();
    HashMap<Integer, Integer> kNeighborHM = new HashMap<>();

    public DialogConfigCF(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
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

        jPanel23 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        CMAuthorTextPane = new javax.swing.JTextPane();
        ok_Button = new javax.swing.JButton();
        knnPearson_CheckBox = new javax.swing.JCheckBox();
        knnCosine_CheckBox = new javax.swing.JCheckBox();
        knnCosine_Button = new javax.swing.JButton();
        knnPearson_Button = new javax.swing.JButton();
        svd_CheckBox = new javax.swing.JCheckBox();
        svd_Button = new javax.swing.JButton();

        setTitle("Configuration of CF Algorithm");
        setResizable(false);

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Discription Option"));

        CMAuthorTextPane.setEditable(false);
        CMAuthorTextPane.setText("This is collaborative filtering based on k_ nearest neighbor. We have to input coefficient K neighbor to construct input matrix to input for Collaborative Filtering recommendation algorithm based on cosine similarity. If you don't enter k, defaut 's system is 8.");
        jScrollPane6.setViewportView(CMAuthorTextPane);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        ok_Button.setText("OK");
        ok_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_ButtonActionPerformed(evt);
            }
        });

        knnPearson_CheckBox.setText("Pearson Correlation-based approach");
        knnPearson_CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knnPearson_CheckBoxActionPerformed(evt);
            }
        });

        knnCosine_CheckBox.setText("Cosine-based approach");
        knnCosine_CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knnCosine_CheckBoxActionPerformed(evt);
            }
        });

        knnCosine_Button.setText("Configuration");
        knnCosine_Button.setEnabled(false);
        knnCosine_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knnCosine_ButtonActionPerformed(evt);
            }
        });

        knnPearson_Button.setText("Configuration");
        knnPearson_Button.setEnabled(false);
        knnPearson_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                knnPearson_ButtonActionPerformed(evt);
            }
        });

        svd_CheckBox.setText("SVD approach");
        svd_CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svd_CheckBoxActionPerformed(evt);
            }
        });

        svd_Button.setText("Configuration");
        svd_Button.setEnabled(false);
        svd_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svd_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(knnPearson_CheckBox)
                    .addComponent(knnCosine_CheckBox)
                    .addComponent(svd_CheckBox))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(knnPearson_Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(svd_Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(knnCosine_Button))
                    .addComponent(ok_Button, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(knnPearson_CheckBox)
                    .addComponent(knnPearson_Button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(knnCosine_Button)
                    .addComponent(knnCosine_CheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(svd_CheckBox)
                    .addComponent(svd_Button))
                .addGap(14, 14, 14)
                .addComponent(ok_Button))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ok_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_ButtonActionPerformed

        //<editor-fold defaultstate="collapsed" desc="Step 1: get cfMethod">
        cfMethodHS.clear();
        if (knnPearson_CheckBox.isSelected()) {
            cfMethodHS.add(1);
        }
        if (knnCosine_CheckBox.isSelected()) {
            cfMethodHS.add(2);
        }
        if (svd_CheckBox.isSelected()) {
            cfMethodHS.add(3);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Step 2: get configuration for each algorithm">
        for (Integer cfMethod : cfMethodHS) {
            if (cfMethod == 1) {
                kNeighborHM.put(1, dialogConfigCFPearson.kNeighbourhood);
            } else if (cfMethod == 2) {
                kNeighborHM.put(2, dialogConfigCFCosine.kNeighbourhood);
            } else if (cfMethod == 3) {
                kNeighborHM.put(3, dialogConfigCFSVD.kNeighbourhood);
                f = dialogConfigCFSVD.f;
                l = dialogConfigCFSVD.l;
                i = dialogConfigCFSVD.i;
            }
        }
        //</editor-fold>
        this.hide();
    }//GEN-LAST:event_ok_ButtonActionPerformed

    private void knnPearson_CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knnPearson_CheckBoxActionPerformed
        if (knnPearson_CheckBox.isSelected()) {
            knnPearson_Button.setEnabled(true);
        } else {
            knnPearson_Button.setEnabled(false);
        }
    }//GEN-LAST:event_knnPearson_CheckBoxActionPerformed

    private void knnCosine_CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knnCosine_CheckBoxActionPerformed
        if (knnCosine_CheckBox.isSelected()) {
            knnCosine_Button.setEnabled(true);
        } else {
            knnCosine_Button.setEnabled(false);
        }
    }//GEN-LAST:event_knnCosine_CheckBoxActionPerformed

    private void svd_CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svd_CheckBoxActionPerformed
        if (svd_CheckBox.isSelected()) {
            svd_Button.setEnabled(true);
        } else {
            svd_Button.setEnabled(false);
        }
    }//GEN-LAST:event_svd_CheckBoxActionPerformed

    private void knnPearson_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knnPearson_ButtonActionPerformed
        dialogConfigCFPearson.setLocationRelativeTo(this);
        dialogConfigCFPearson.show();
    }//GEN-LAST:event_knnPearson_ButtonActionPerformed

    private void knnCosine_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_knnCosine_ButtonActionPerformed
        dialogConfigCFCosine.setLocationRelativeTo(this);
        dialogConfigCFCosine.show();
    }//GEN-LAST:event_knnCosine_ButtonActionPerformed

    private void svd_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svd_ButtonActionPerformed
        dialogConfigCFSVD.setLocationRelativeTo(this);
        dialogConfigCFSVD.show();
    }//GEN-LAST:event_svd_ButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            System.out.println("Unable to load Windows look and feel");
        }
        //</editor-fold>


        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogConfigCF dialog = new DialogConfigCF(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane CMAuthorTextPane;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JButton knnCosine_Button;
    private javax.swing.JCheckBox knnCosine_CheckBox;
    private javax.swing.JButton knnPearson_Button;
    private javax.swing.JCheckBox knnPearson_CheckBox;
    private javax.swing.JButton ok_Button;
    private javax.swing.JButton svd_Button;
    private javax.swing.JCheckBox svd_CheckBox;
    // End of variables declaration//GEN-END:variables
}
