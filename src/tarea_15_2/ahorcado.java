package tarea_15_2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;

public class ahorcado extends JFrame {
    private String[] palabrasSecretas = {"PROGRAMACION", "UNIVERSIDAD", "LIMA"};
    private String palabraSecreta;
    private char[] progreso;
    private int intentosRestantes = 6;

    private JLabel lblProgreso;
    private JLabel lblIntentos;
    private JPanel panelDibujo;
    private JLabel lblLetrasDisponibles;

    private Set<Character> letrasSeleccionadas;

    public ahorcado() {
        setTitle("Ahorcado");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        iniciarNuevaPalabra();

        letrasSeleccionadas = new HashSet<>();

        // Panel superior: progreso y vidas
        JPanel panelSuperior = new JPanel(new GridLayout(3, 1));
        lblProgreso = new JLabel(String.valueOf(progreso), SwingConstants.CENTER);
        lblProgreso.setFont(new Font("Arial", Font.BOLD, 24));
        lblIntentos = new JLabel("Intentos restantes: " + intentosRestantes, SwingConstants.CENTER);
        lblIntentos.setFont(new Font("Arial", Font.PLAIN, 16));
        lblLetrasDisponibles = new JLabel(generarLetrasDisponibles(), SwingConstants.CENTER);
        lblLetrasDisponibles.setFont(new Font("Arial", Font.PLAIN, 18));

        panelSuperior.add(lblProgreso);
        panelSuperior.add(lblIntentos);
        panelSuperior.add(lblLetrasDisponibles);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central: dibujo del ahorcado
        panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarAhorcado(g);
            }
        };
        add(panelDibujo, BorderLayout.CENTER);

        // Panel inferior: teclado
        JPanel panelTeclado = new JPanel(new GridLayout(3, 9, 5, 5));
        for (char c = 'A'; c <= 'Z'; c++) {
            JButton btnLetra = new JButton(String.valueOf(c));
            btnLetra.setFont(new Font("Arial", Font.PLAIN, 16));
            btnLetra.addActionListener(e -> {
                procesarLetra(btnLetra.getText().charAt(0));
                btnLetra.setEnabled(false); // Desactivar botón después de presionarlo
            });
            panelTeclado.add(btnLetra);
        }
        add(panelTeclado, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void iniciarNuevaPalabra() {
        palabraSecreta = palabrasSecretas[new Random().nextInt(palabrasSecretas.length)];
        progreso = new char[palabraSecreta.length()];
        for (int i = 0; i < progreso.length; i++) {
            progreso[i] = '_';
        }
        intentosRestantes = 6;
        letrasSeleccionadas = new HashSet<>();
    }

    private void procesarLetra(char letra) {
        letrasSeleccionadas.add(letra);
        boolean acierto = false;
        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (Character.toUpperCase(palabraSecreta.charAt(i)) == letra) {
                progreso[i] = letra;
                acierto = true;
            }
        }

        if (!acierto) {
            intentosRestantes--;
        }

        lblProgreso.setText(String.valueOf(progreso));
        lblIntentos.setText("Intentos restantes: " + intentosRestantes);
        lblLetrasDisponibles.setText(generarLetrasDisponibles());
        panelDibujo.repaint();

        verificarEstado();
    }

    private String generarLetrasDisponibles() {
        StringBuilder letras = new StringBuilder();
        for (char c = 'A'; c <= 'Z'; c++) {
            if (letrasSeleccionadas.contains(c)) {
                letras.append("<html><strike>").append(c).append("</strike></html> "); // Letras tachadas
            } else {
                letras.append(c).append(" ");
            }
        }
        return "<html>" + letras.toString() + "</html>";
    }

    private void verificarEstado() {
        if (String.valueOf(progreso).equalsIgnoreCase(palabraSecreta)) {
            JOptionPane.showMessageDialog(this, "¡Ganaste! La palabra era: " + palabraSecreta);
            reiniciarJuego();
        } else if (intentosRestantes <= 0) {
            JOptionPane.showMessageDialog(this, "¡Perdiste! La palabra era: " + palabraSecreta);
            reiniciarJuego();
        }
    }

    private void reiniciarJuego() {
        iniciarNuevaPalabra();
        lblProgreso.setText(String.valueOf(progreso));
        lblIntentos.setText("Intentos restantes: " + intentosRestantes);
        lblLetrasDisponibles.setText(generarLetrasDisponibles());
        panelDibujo.repaint();
    }

    private void dibujarAhorcado(Graphics g) {
        g.setColor(Color.BLACK);
        // Base del ahorcado
        g.fillRect(50, 300, 200, 10); // Base
        g.drawLine(150, 300, 150, 50); // Poste vertical
        g.drawLine(150, 50, 250, 50); // Poste horizontal
        g.drawLine(250, 50, 250, 100); // Cuerda

        if (intentosRestantes <= 5) {
            g.drawOval(225, 100, 50, 50); // Cabeza
        }
        if (intentosRestantes <= 4) {
            g.drawLine(250, 150, 250, 230); // Cuerpo
        }
        if (intentosRestantes <= 3) {
            g.drawLine(250, 170, 220, 200); // Brazo izquierdo
        }
        if (intentosRestantes <= 2) {
            g.drawLine(250, 170, 280, 200); // Brazo derecho
        }
        if (intentosRestantes <= 1) {
            g.drawLine(250, 230, 220, 270); // Pierna izquierda
        }
        if (intentosRestantes == 0) {
            g.drawLine(250, 230, 280, 270); // Pierna derecha
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ahorcado::new);
    }
}
