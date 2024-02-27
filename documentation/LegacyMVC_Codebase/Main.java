
import model.Model;
import view.View;
import controller.Controller;

// provisoire
// import test.Test;

public class Main {

  public static void main (String[] args) {

    // la méthode static invokeLater de la classe SwingUtilities
    // prend un argument de type Runnable ;
    // comme Runnable est une interface qui ne déclare que la
    // méthode run(), celle-ci doit alors être implémentée pour
    // l'instanciation de l'argument de la méthode invokeLater ;
    // avec la méthode invokeLater, le code de la méthode run()
    // ne sera pas exécuté par le thread EDT (Event Dispatch Thread)
    // mais dans un des thread dit "worker threads" ou "background threads"
    javax.swing.SwingUtilities.invokeLater(

      // création d'un thread via l'implémentation de l'interface
      // Runnable afin d'instancier un objet de classe ;
      new Runnable() {

        // implémentation la méthode run(), l'unique méthode de
        // l'interface Runnable

        public void run() {

            Model model = new Model();
            View view = new View(model);
            Controller controller = new Controller(model, view);

            // faire observe le model par la vue
            model.observedBy(view);

            // faire écouter la vue par le contrôleur
            view.listenedBy(controller);

            // test l'observation du modèle par la vue
            // Test test = new Test(model);

        }

      }

    );

  }

}

// le code à exécuter en réponse à des évènements l'est par le thread appelé
// "Event Dispatch Thread" (EDT) (wikipédia : "most Swing object methods
// are not "thread safe": invoking them from multiple threads risks thread
// interference or memory consistency errors.")
