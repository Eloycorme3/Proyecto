/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import controlador.controladorPrincipal;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author eloy.castro
 */
public class GestorAdminUsuario extends javax.swing.JDialog {

    /**
     * Creates new form GestorAdmin
     * @param parent
     * @param modal
     */
    public GestorAdminUsuario(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        txtPasswordUsuario.setEchoChar('*');
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panel = new javax.swing.JPanel();
        btnModContrasenhaUsuario = new javax.swing.JButton();
        btnModNombreUsuario = new javax.swing.JButton();
        cbVerPassword = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        txtNombreUsuario = new javax.swing.JTextField();
        txtPasswordUsuario = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        btnModTipoUsuario = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        comboTipoUsuario = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        panel.setLayout(new java.awt.GridBagLayout());

        btnModContrasenhaUsuario.setText("Cambiar contraseña");
        btnModContrasenhaUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModContrasenhaUsuarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 15, 0, 50);
        panel.add(btnModContrasenhaUsuario, gridBagConstraints);

        btnModNombreUsuario.setText("Cambiar nombre");
        btnModNombreUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModNombreUsuarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 15, 0, 50);
        panel.add(btnModNombreUsuario, gridBagConstraints);

        cbVerPassword.setText("Mostrar contraseña");
        cbVerPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbVerPasswordActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 35;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panel.add(cbVerPassword, gridBagConstraints);

        jLabel5.setText("Contraseña");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 50, 0, 20);
        panel.add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 55;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panel.add(txtNombreUsuario, gridBagConstraints);

        txtPasswordUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordUsuarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 55;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panel.add(txtPasswordUsuario, gridBagConstraints);

        jLabel3.setText("Nombre");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 50, 0, 20);
        panel.add(jLabel3, gridBagConstraints);

        btnModTipoUsuario.setText("Cambiar tipo");
        btnModTipoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModTipoUsuarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 15, 20, 50);
        panel.add(btnModTipoUsuario, gridBagConstraints);

        jLabel1.setText("Tipo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 50, 20, 20);
        panel.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 55;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 20, 0);
        panel.add(comboTipoUsuario, gridBagConstraints);

        getContentPane().add(panel, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnModContrasenhaUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModContrasenhaUsuarioActionPerformed
        // TODO add your handling code here:
        controladorPrincipal.modificarContrasenhaUsuarioAdmin();
    }//GEN-LAST:event_btnModContrasenhaUsuarioActionPerformed

    private void btnModNombreUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModNombreUsuarioActionPerformed
        // TODO add your handling code here:
        controladorPrincipal.modificarNombreUsuarioAdmin();
    }//GEN-LAST:event_btnModNombreUsuarioActionPerformed

    private void cbVerPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbVerPasswordActionPerformed
        // TODO add your handling code here:
        if (cbVerPassword.isSelected()) {
            txtPasswordUsuario.setEchoChar((char) 0);
        } else {
            txtPasswordUsuario.setEchoChar('*');
        }
    }//GEN-LAST:event_cbVerPasswordActionPerformed

    private void txtPasswordUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordUsuarioActionPerformed

    private void btnModTipoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModTipoUsuarioActionPerformed
        // TODO add your handling code here:
        controladorPrincipal.modificarTipoUsuarioAdmin();
    }//GEN-LAST:event_btnModTipoUsuarioActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        controladorPrincipal.comprobarExistenciaUsuarios();
    }//GEN-LAST:event_formWindowClosed

    public JButton getBtnModContrasenhaUsuario() {
        return btnModContrasenhaUsuario;
    }

    public void setBtnModContrasenhaUsuario(JButton btnModContrasenhaUsuario) {
        this.btnModContrasenhaUsuario = btnModContrasenhaUsuario;
    }

    public JButton getBtnModNombreUsuario() {
        return btnModNombreUsuario;
    }

    public void setBtnModNombreUsuario(JButton btnModNombreUsuario) {
        this.btnModNombreUsuario = btnModNombreUsuario;
    }

    public JButton getBtnModTipoUsuario() {
        return btnModTipoUsuario;
    }

    public void setBtnModTipoUsuario(JButton btnModTipoUsuario) {
        this.btnModTipoUsuario = btnModTipoUsuario;
    }

    public JCheckBox getCbVerPassword() {
        return cbVerPassword;
    }

    public void setCbVerPassword(JCheckBox cbVerPassword) {
        this.cbVerPassword = cbVerPassword;
    }

    public JComboBox<String> getComboTipoUsuario() {
        return comboTipoUsuario;
    }

    public void setComboTipoUsuario(JComboBox<String> comboTipoUsuario) {
        this.comboTipoUsuario = comboTipoUsuario;
    }

    public JTextField getTxtNombreUsuario() {
        return txtNombreUsuario;
    }

    public void setTxtNombreUsuario(JTextField txtNombreUsuario) {
        this.txtNombreUsuario = txtNombreUsuario;
    }

    public JPasswordField getTxtPasswordUsuario() {
        return txtPasswordUsuario;
    }

    public void setTxtPasswordUsuario(JPasswordField txtPasswordUsuario) {
        this.txtPasswordUsuario = txtPasswordUsuario;
    }

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnModContrasenhaUsuario;
    private javax.swing.JButton btnModNombreUsuario;
    private javax.swing.JButton btnModTipoUsuario;
    private javax.swing.JCheckBox cbVerPassword;
    private javax.swing.JComboBox<String> comboTipoUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel panel;
    private javax.swing.JTextField txtNombreUsuario;
    private javax.swing.JPasswordField txtPasswordUsuario;
    // End of variables declaration//GEN-END:variables
}
