package utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Utilidad para cargar y manipular imágenes.
 */
public class ImageLoader {

    /**
     * Carga una imagen desde un archivo.
     *
     * @param path Ruta a la imagen
     * @return ImageIcon con la imagen cargada
     */
    public static ImageIcon loadImage(String path) {
        try {
            // Intentar cargar desde classpath (recursos)
            URL url = ImageLoader.class.getClassLoader().getResource(path);
            if (url != null) {
                return new ImageIcon(url);
            }

            // Si no se encuentra en classpath, intentar cargar como archivo
            File file = new File(path);
            if (file.exists()) {
                return new ImageIcon(file.getAbsolutePath());
            }

            // Si no se encuentra la imagen, devolver una imagen de placeholder
            return createPlaceholderImage(200, 150);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + path);
            e.printStackTrace();
            return createPlaceholderImage(200, 150);
        }
    }

    /**
     * Carga una imagen y la redimensiona al tamaño especificado.
     *
     * @param path Ruta a la imagen
     * @param width Ancho deseado
     * @param height Alto deseado
     * @return ImageIcon con la imagen redimensionada
     */
    public static ImageIcon loadAndResizeImage(String path, int width, int height) {
        ImageIcon originalIcon = loadImage(path);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    /**
     * Crea una imagen placeholder cuando no se puede cargar la imagen original.
     *
     * @param width Ancho de la imagen
     * @param height Alto de la imagen
     * @return ImageIcon con la imagen placeholder
     */
    private static ImageIcon createPlaceholderImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Fondo gris
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, width, height);

        // Borde
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRect(0, 0, width - 1, height - 1);

        // Texto "Imagen no disponible"
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Imagen no disponible", 10, height / 2);

        g2d.dispose();
        return new ImageIcon(image);
    }
}