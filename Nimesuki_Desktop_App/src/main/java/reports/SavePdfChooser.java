/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reports;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.SAVE_DIALOG;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author eloy.castro
 */
public class SavePdfChooser {

    private final JFileChooser chooser;
    private final Component parent;

    public SavePdfChooser(Component parent) {
        this.parent = parent;
        chooser = new JFileChooser() {
            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                String path = f.getAbsolutePath();

                if (!path.toLowerCase().endsWith(".pdf")) {
                    f = new File(path + ".pdf");
                }

                if (f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(
                            this,
                            "El fichero ya existe, ¿quieres sobrescribirlo?",
                            "Fichero existente",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (result != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                setSelectedFile(f);
                super.approveSelection();
            }

        };
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
    }

    public String getFilePath() {
        int returnVal = chooser.showSaveDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
}
