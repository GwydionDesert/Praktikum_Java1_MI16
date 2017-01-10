import javafx.application.Application; 
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Scene; 
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.control.ColorPicker;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.stage.Stage; 
import javafx.scene.layout.VBox;

public class EinErstesBeispiel  extends Application
{
    
    public static Point2D pOld = new Point2D(0,0);
    public static Point2D pNew = new Point2D(0,0);
    public static Slider slider = new Slider(0, 0.99, 0.5);
    private static Color zeichenFarbe;
        /*
        Application:
        Anwendungsklasse; Sie stellt ein Fenster mit Rahmen,
        Systemmenue und Standardschaltflaechen zur Verfuegung.
        */
        
    public static void main(String[] args){
        // launch() startet die Anwendung
        
        Application.launch(args);
    }
    
    /*
    Ueberschriebene Methode start() : wird beim Erzeugen der 
    Anwendung aufgerufen und legt den Inhalt des Fensters fest.
    */

    public void start(Stage primaryStage) { 
            //Stage: oberster JavaFX-Container, in der Regel ein Fenster
            primaryStage.setTitle("MausMalen"); 
            primaryStage.setResizable(false);
            
            //Erzeugung der Komponente Canvas
            Canvas canvas = new Canvas(750,500);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            
            canvas.onMousePressedProperty();
            canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> neuerAktuellerPunkt(e));
            canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> setAktuellerPunkt(e, gc));
            
            //Erzeugen eines ColorPicker
            ColorPicker color = new ColorPicker();
            color.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            
            color.setOnAction(e -> zeichenFarbe = color.getValue());
            
            //Erzeugen eines Clear Buttons
            Button clear = new Button();
            clear.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            clear.setOnAction(e -> gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()));
            
            //VBox: Layout Container, in dem die Inhalte fliessend vertikal angeordnet werden
            VBox root = new VBox();
            
            //Canvas zur VBox hinzufuegen
            root.getChildren().add(canvas);
            root.getChildren().add(slider);
            root.getChildren().add(color);
            root.getChildren().add(clear);
            
            //Scene: beschreibt den Inhalt des Fensters
            Scene scene = new Scene(root );     
            
            //Scene dem Fenster hinzufuegen
            primaryStage.setScene(scene); 
            
            //show() zeigt das Fenster auf dem Bildschirm an.
            primaryStage.show(); 
    } 
    
    public static void paintLine(GraphicsContext gc){
        gc.setLineWidth(2.0);
        gc.setStroke(zeichenFarbe);
        gc.strokeLine(pOld.getX(), pOld.getY(), pNew.getX(), pNew.getY());
    }
    
    public static void neuerAktuellerPunkt(MouseEvent e) {
        pNew = pOld = pNew.add(e.getX() - pNew.getX(), e.getY() - pNew.getY());
    }
    
    public static void setAktuellerPunkt(MouseEvent e, GraphicsContext gc) {
        pOld = pOld.add (pNew.getX() - pOld.getX(), pNew.getY() - pOld.getY());
        pNew = new Point2D(lerp((float)e.getX(), (float)pNew.getX()), lerp((float)e.getY(), (float)pNew.getY()));
        paintLine(gc);
    }
    
    public static float lerp(float p1, float p2){
        float alpha = (float) slider.getValue();
        return p1 + alpha * (p2 - p1);
    }
}
