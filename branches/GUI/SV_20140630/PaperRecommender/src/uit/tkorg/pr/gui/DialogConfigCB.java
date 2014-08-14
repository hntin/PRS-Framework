/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.gui;

import javax.swing.UIManager;

/**
 *
 * @author Zoe
 */
public class DialogConfigCB extends javax.swing.JDialog {

    /**
     * Creates new form NewJDialog
     */
    //<editor-fold defaultstate="collapsed" desc="variables of DiaglogConfigCB">
    int combineAuthor = 0;
    int weightingAuthor = 0;
    int timeAware = 0;
    float gamma = 0;
    int combinePaper = 0;
    int weightingPaper = 0;
    float pruning = 0;
    //</editor-fold>

    public DialogConfigCB(java.awt.Frame parent, boolean modal) {
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

        jPanel15 = new javax.swing.JPanel();
        timeAware_CheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        gamma_TextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        combineAuthor_ComboBox = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        weightingAuthor_ComboBox = new javax.swing.JComboBox();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        CMAuthorTextPane = new javax.swing.JTextPane();
        moreInformation_Button = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        combinePaper_ComboBox = new javax.swing.JComboBox();
        weightingPaper_ComboBox = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        ok_Button = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pruning_TextField = new javax.swing.JTextField();

        setTitle("Configuration of Content Based Algorithm");
        setResizable(false);

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Constructing Profile for Author"));

        timeAware_CheckBox.setText("TimeAware");
        timeAware_CheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeAware_CheckBoxActionPerformed(evt);
            }
        });

        jLabel1.setText("Gamma:");

        gamma_TextField.setText("0.0");
        gamma_TextField.setEnabled(false);

        jLabel12.setText("Combination Method");

        combineAuthor_ComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Feature Vector Of Paper", "Feature Vector Of Paper+ Citation Papers", "Feature Vector Of Paper+ Reference Papers", "Feature Vector Of Paper+ Citation Papers + Reference Papers" }));
        combineAuthor_ComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combineAuthor_ComboBoxActionPerformed(evt);
            }
        });

        jLabel13.setText("Weighting Combination");

        weightingAuthor_ComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Linear combination(LC)", "Cosine similarity(SIM)", "Reciprocal of the difference between published years(RPY)" }));
        weightingAuthor_ComboBox.setEnabled(false);
        weightingAuthor_ComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weightingAuthor_ComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(0, 200, Short.MAX_VALUE)
                        .addComponent(timeAware_CheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addGap(23, 23, 23)
                        .addComponent(gamma_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(combineAuthor_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(weightingAuthor_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 4, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(combineAuthor_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(weightingAuthor_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeAware_CheckBox)
                    .addComponent(jLabel1)
                    .addComponent(gamma_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Discription Option"));

        CMAuthorTextPane.setEditable(false);
        CMAuthorTextPane.setText("In here, we have to set up coefficients for author's profiles construction and feature vector construction for papers.This seems to prepared data for recommendation.\n");
        jScrollPane6.setViewportView(CMAuthorTextPane);

        moreInformation_Button.setText("More");
        moreInformation_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moreInformation_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(moreInformation_Button)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(moreInformation_Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder("Constructing Feature Vector for Paper"));

        jLabel15.setText("Combination Method");

        combinePaper_ComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Feature Vector Of Paper", "Feature Vector Of Paper+ Citation Papers", "Feature Vector Of Paper+ Reference Papers", "Feature Vector Of Paper+ Citation Papers + Reference Papers" }));
        combinePaper_ComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combinePaper_ComboBoxActionPerformed(evt);
            }
        });

        weightingPaper_ComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Linear combination(LC)", "Cosine similarity(SIM)", "Reciprocal of the difference between published years(RPY)" }));
        weightingPaper_ComboBox.setEnabled(false);
        weightingPaper_ComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weightingPaper_ComboBoxActionPerformed(evt);
            }
        });

        jLabel16.setText("Weighting Combination");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(combinePaper_ComboBox, 0, 1, Short.MAX_VALUE)
                    .addComponent(weightingPaper_ComboBox, 0, 1, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(combinePaper_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(weightingPaper_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        ok_Button.setText("OK");
        ok_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_ButtonActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Threshold "));

        jLabel2.setText("Pruning");

        pruning_TextField.setText("0.0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pruning_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(pruning_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ok_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 461, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ok_Button)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void moreInformation_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moreInformation_ButtonActionPerformed
        DialogDescriptionCB descriptionCB = new DialogDescriptionCB(this, rootPaneCheckingEnabled);
        descriptionCB.setLocationRelativeTo(this);
        descriptionCB.show();
    }//GEN-LAST:event_moreInformation_ButtonActionPerformed

    private void ok_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_ButtonActionPerformed
        if (combineAuthor_ComboBox.getSelectedIndex() == 0) {
            combineAuthor = 0;
        } else if (combineAuthor_ComboBox.getSelectedIndex() == 1) {
            combineAuthor = 1;
        } else if (combineAuthor_ComboBox.getSelectedIndex() == 2) {
            combineAuthor = 2;
        } else if (combineAuthor_ComboBox.getSelectedIndex() == 3) {
            combineAuthor = 3;
        }

        if (weightingAuthor_ComboBox.getSelectedIndex() == 0) {
            weightingAuthor = 0;
        } else if (weightingAuthor_ComboBox.getSelectedIndex() == 1) {
            weightingAuthor = 1;
        }

        if (combinePaper_ComboBox.getSelectedIndex() == 0) {
            combinePaper = 0;
        } else if (combinePaper_ComboBox.getSelectedIndex() == 1) {
            combinePaper = 1;
        } else if (combinePaper_ComboBox.getSelectedIndex() == 2) {
            combinePaper = 2;
        } else if (combinePaper_ComboBox.getSelectedIndex() == 3) {
            combinePaper = 3;
        }

        if (weightingPaper_ComboBox.getSelectedIndex() == 0) {
            weightingPaper = 0;
        } else if (weightingPaper_ComboBox.getSelectedIndex() == 1) {
            weightingPaper = 1;
        } else if (weightingPaper_ComboBox.getSelectedIndex() == 2) {
            weightingPaper = 2;
        }

        if (timeAware_CheckBox.isSelected()) {
            timeAware = 1;
            gamma = Float.valueOf(gamma_TextField.getText().trim());
        } else {
            timeAware = 0;
            gamma = 0;
        }
        pruning = Float.valueOf(pruning_TextField.getText());
        this.hide();
    }//GEN-LAST:event_ok_ButtonActionPerformed

    private void timeAware_CheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeAware_CheckBoxActionPerformed
        if (timeAware_CheckBox.isSelected()) {
            gamma_TextField.setEnabled(true);
        } else {
            gamma_TextField.setEnabled(false);
        }
    }//GEN-LAST:event_timeAware_CheckBoxActionPerformed

    private void combineAuthor_ComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combineAuthor_ComboBoxActionPerformed
        if (combineAuthor_ComboBox.getSelectedIndex() == 0) {
            combineAuthor = 0;
            weightingAuthor_ComboBox.setEnabled(false);
        } else if (combineAuthor_ComboBox.getSelectedIndex() == 1) {
            combineAuthor = 1;
            weightingAuthor_ComboBox.setEnabled(true);
        } else if (combineAuthor_ComboBox.getSelectedIndex() == 2) {
            combineAuthor = 2;
            weightingAuthor_ComboBox.setEnabled(true);
        } else if (combineAuthor_ComboBox.getSelectedIndex() == 3) {
            combineAuthor = 3;
            weightingAuthor_ComboBox.setEnabled(true);
        }
    }//GEN-LAST:event_combineAuthor_ComboBoxActionPerformed

    private void weightingAuthor_ComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weightingAuthor_ComboBoxActionPerformed
        if (weightingAuthor_ComboBox.isEnabled()) {
            if (weightingAuthor_ComboBox.getSelectedIndex() == 0) {
                weightingAuthor = 0;
            } else if (weightingAuthor_ComboBox.getSelectedIndex() == 1) {
                weightingAuthor = 1;
            } else if (weightingAuthor_ComboBox.getSelectedIndex() == 2) {
                weightingAuthor = 2;
            }
        }
    }//GEN-LAST:event_weightingAuthor_ComboBoxActionPerformed

    private void combinePaper_ComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combinePaper_ComboBoxActionPerformed
        if (combinePaper_ComboBox.getSelectedIndex() == 0) {
            combinePaper = 0;
            weightingPaper_ComboBox.setEnabled(false);
        } else if (combinePaper_ComboBox.getSelectedIndex() == 1) {
            combinePaper = 1;
            weightingPaper_ComboBox.setEnabled(true);
        } else if (combinePaper_ComboBox.getSelectedIndex() == 2) {
            combinePaper = 2;
            weightingPaper_ComboBox.setEnabled(true);
        } else if (combinePaper_ComboBox.getSelectedIndex() == 3) {
            combinePaper = 3;
            weightingPaper_ComboBox.setEnabled(true);
        }
    }//GEN-LAST:event_combinePaper_ComboBoxActionPerformed

    private void weightingPaper_ComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weightingPaper_ComboBoxActionPerformed
        if (weightingPaper_ComboBox.isEnabled()) {
            if (weightingPaper_ComboBox.getSelectedIndex() == 0) {
                weightingPaper = 0;
            } else if (weightingPaper_ComboBox.getSelectedIndex() == 1) {
                weightingPaper = 1;
            } else if (weightingPaper_ComboBox.getSelectedIndex() == 2) {
                weightingPaper = 2;
            }
        }
    }//GEN-LAST:event_weightingPaper_ComboBoxActionPerformed

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
                DialogConfigCB dialog = new DialogConfigCB(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox combineAuthor_ComboBox;
    private javax.swing.JComboBox combinePaper_ComboBox;
    private javax.swing.JTextField gamma_TextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JButton moreInformation_Button;
    private javax.swing.JButton ok_Button;
    private javax.swing.JTextField pruning_TextField;
    private javax.swing.JCheckBox timeAware_CheckBox;
    private javax.swing.JComboBox weightingAuthor_ComboBox;
    private javax.swing.JComboBox weightingPaper_ComboBox;
    // End of variables declaration//GEN-END:variables
}
