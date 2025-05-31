import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

// Debo crear la lógica en donde el front deba leer la información almacenada del backend, la cual es un array que debe mostrar la visualización del item con sus preguntas y la funcionalidad de poder realizar la selección multiple almacenando los valores respondidos para corroborarlo con los valores de las respuestas almacenados en el backend.

// 1) Cargar archivo de prueba con respuestas
// 2) Al momento de cargar el archivo, debe ejecutarse la lectura del archivo desde el backend
// 3) Luego, debe rescatar cada items con sus preguntas, tener la posibilidad de seleccionar cada pregunta y tener un oton que permita ir al siguiente item o volver al item anterior , salvo el primero, en el ultimo item en vez del boton siguiente debe ser remplazado por el boton entregar.
// 4) Al pulsar el boton entregar, mostrara una ventana/panel que dara el resultado de preguntas correcta en porcentaje total y por cada uno de los item. Debe comprobar las respuestas seleccionadas con las que tiene almacenadas el backend.




//Interfaz gráfica del programa basado en libreria swing.

public class MainFrame extends JFrame {
    final private Font mainFont = new Font("monospaced", Font.BOLD, 18);
    JTextField tfFirstName, tfLastName;
    JLabel lbWelcome;

    public void initialize() {

        // Form Panel 🎈
        // A continuación, se muestra código referente al panel de formulario y sus estilos.
        JLabel lbFirstName = new JLabel("First Name");
        lbFirstName.setFont(mainFont);

        tfFirstName = new JTextField();
        tfFirstName.setFont(mainFont);

        JLabel lbLastName = new JLabel("Last Name");
        lbLastName.setFont(mainFont);

        tfLastName = new JTextField();
        tfLastName.setFont(mainFont);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 1, 5, 5));
        formPanel.setOpaque(false);
        formPanel.add(lbFirstName);
        formPanel.add(tfFirstName);
        formPanel.add(lbLastName);
        formPanel.add(tfLastName);
      
        // Welcome/Home Panel 🏠
        // Panel de bienvenida del programa.

        lbWelcome = new JLabel();
        lbWelcome.setFont(mainFont);

        // Buttons Panel

        JButton btnOk = new JButton("OK");
        btnOk.setFont(mainFont);
        btnOk.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String firstName = tfFirstName.getText();
                String lastName = tfLastName.getText();
                lbWelcome.setText("Hola " + firstName + " " + lastName);
            }
        });

        JButton btnClear = new JButton("Clear");
        btnClear.setFont(mainFont);
        btnClear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                tfFirstName.setText("");
                tfLastName.setText("");
                lbWelcome.setText("");
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 5, 5));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(btnOk);
        buttonsPanel.add(btnClear);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(128, 128, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(lbWelcome, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setTitle("Welcome");
        setSize(500, 600);
        setMinimumSize(new Dimension(300, 400));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        
    }

    public static void main(String[] args) {
        MainFrame myFrame = new MainFrame();
        myFrame.initialize();
    }
}

