
package view;

// importation de la classe Graphics2D pour gérer le dessin
import java.awt.Graphics2D;

// importation pour gérer le traitement des images
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

// classe statique regroupant les méthodes de gestion des images
public final class ImageManager {

  // redimentionne l'image d'adresse passée en premier argument suivant
  // les dimentions passées en troisième et quatrième argument puis
  // écrit l'image redimentionnée dans un fichier d'adresse passée
  // en deuxième argument
  public static void resize(
      String inputImageName,
      String  outputImageName,
      int outputImageWidth,
      int outputImageHeight) {

    // l'image à redimentionnée puis l'image redimentionnée
    BufferedImage inputImage, outputImage;

    // pour dessiner l'image redimentionnée
    Graphics2D graphics2D;

    // adresse de l'image redimentionnée privée de son extension
    String outputImageNameWithoutExtension;

    // lecture de l'image à redimentionner puis création et écriture
    // de l'image redimentionnée
    try {

      // lit l'image d'entrée
      inputImage = ImageIO.read(new File(inputImageName));

      // crée l'image de sortie
      outputImage
        = new BufferedImage(
          outputImageWidth,
          outputImageHeight,
          inputImage.getType()
        );

      // balancer l'image d'entrée à l'image de sortie
      graphics2D = outputImage.createGraphics();
      graphics2D.drawImage(
        inputImage,
        0,
        0,
        outputImageWidth,
        outputImageHeight,
        null
      );
      graphics2D.dispose();

      // extrait l'extension du fichier de sortie
      outputImageNameWithoutExtension
        = outputImageName.substring(outputImageName.lastIndexOf(".") + 1);

      // écrit dans le fichier de sortie
      ImageIO.write(
        outputImage,
        outputImageNameWithoutExtension,
        new File(outputImageName)
      );

    }


    catch (IOException exception) {

      exception.printStackTrace();

    }

  }

}
