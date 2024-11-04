/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.capturap;

/**
 *
 * @author yahircanseco
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BotonCapturaPantalla extends JButton implements ActionListener {
    private int contadorCapturas = 0; // Contador de capturas realizadas
    private File directorioGuardado;   // Directorio donde se guardarán las capturas

    public BotonCapturaPantalla() {
        super("Capturar Pantalla");
        seleccionarCarpeta();
        addActionListener(this);
    }

    // Getter para obtener el directorio de guardado
    public File getDirectorioGuardado() {
        return directorioGuardado;
    }

    // Setter para establecer el directorio de guardado
    public void setDirectorioGuardado(File directorioGuardado) {
        this.directorioGuardado = directorioGuardado;
    }

    // Getter para obtener el contador de capturas
    public int getContadorCapturas() {
        return contadorCapturas;
    }

    // Setter para establecer el contador de capturas (opcional)
    public void setContadorCapturas(int contadorCapturas) {
        this.contadorCapturas = contadorCapturas;
    }

    // Método para seleccionar la carpeta de guardado
    private void seleccionarCarpeta() {
        JFileChooser selectorCarpeta = new JFileChooser();
        selectorCarpeta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int resultado = selectorCarpeta.showSaveDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            directorioGuardado = selectorCarpeta.getSelectedFile();
        } else {
            directorioGuardado = new File(System.getProperty("user.home")); // Carpeta predeterminada
        }
    }

    // Acción al hacer clic en el botón
    @Override
    public void actionPerformed(ActionEvent e) {
        if (directorioGuardado == null) {
            seleccionarCarpeta(); // Solicitar carpeta si aún no se ha seleccionado
        }
        capturarPantalla();
    }

    // Método para capturar la pantalla
    private void capturarPantalla() {
        try {
            // Obtener la ventana principal
            Window ventana = SwingUtilities.getWindowAncestor(this);
            if (ventana == null) {
                JOptionPane.showMessageDialog(this, "Error: No se ha seleccionado la ventana para capturar :c");
                return;
            }

            // Capturar la ventana en una imagen
            BufferedImage captura = new Robot().createScreenCapture(ventana.getBounds());

            // Guardar la captura de pantalla
            File archivoCaptura = new File(directorioGuardado, "Captura" + contadorCapturas + ".png");
            ImageIO.write(captura, "PNG", archivoCaptura);

            // Mostrar mensaje de confirmación
            JOptionPane.showMessageDialog(this, "Captura guardada en: " + archivoCaptura.getAbsolutePath() +":)");

            // Incrementar contador y verificar si es necesario eliminar capturas antiguas
            contadorCapturas++;
            if (contadorCapturas >= 10) {
                limpiarCapturas();
            }
        } catch (AWTException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al capturar pantalla :(");
        }
    }

    // Método para limpiar capturas antiguas, mueve las capturas a la papelera
    private void limpiarCapturas() {
        File[] archivos = directorioGuardado.listFiles((dir, name) -> name.startsWith("Captura") && name.endsWith(".png"));
        if (archivos != null && archivos.length > 10) {
            for (File archivo : archivos) {
                archivo.delete(); // Elimina las capturas antiguas
            }
        }
        contadorCapturas = 0; // Reinicia el contador
    }
}