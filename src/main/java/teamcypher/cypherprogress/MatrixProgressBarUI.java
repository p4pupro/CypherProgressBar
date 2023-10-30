package teamcypher.cypherprogress;

import com.intellij.util.ui.JBUI;
import teamcypher.cypherprogress.configuration.CypherProgressState;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.util.Random;

public class MatrixProgressBarUI extends BasicProgressBarUI {
    private final TintableImageIcon cypherIcon;
    private MatrixColumn[] matrixColumns;

    private boolean lastDirFlipped = false;
    private int lastX = 0;

    public MatrixProgressBarUI() {
        this(CypherProgressState.getInstance());
    }

    // also used for previewing settings without modifying the state instance
    public MatrixProgressBarUI(CypherProgressState settings) {
        cypherIcon = new TintableImageIcon(ImagePreloader.getImage(), settings.tintColor);
        initMatrixEffect(32, 32);
    }

    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new MatrixProgressBarUI();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(super.getPreferredSize(c).width, JBUI.scale(cypherIcon.getIconHeight()));
    }

    @Override
    protected void paintIndeterminate(Graphics g2d, JComponent c) {
        if (!(g2d instanceof Graphics2D)) return;
        Graphics2D g2 = (Graphics2D) g2d;


        Insets b = this.progressBar.getInsets();
        int barRectWidth = this.progressBar.getWidth() - (b.right + b.left);
        int barRectHeight = this.progressBar.getHeight() - (b.top + b.bottom);

        paintMatrixEffect(g2, barRectWidth, barRectHeight);

        this.boxRect = this.getBox(this.boxRect);
        if (this.boxRect != null) {

            if (lastX != this.boxRect.x)
                lastDirFlipped = (this.boxRect.x - lastX < 0);

            cypherIcon.paintIcon(c, g2, this.boxRect.x, b.top, lastDirFlipped);

            lastX = this.boxRect.x;
        }

        if (this.progressBar.isStringPainted()) {
            this.paintString(g2, b.left, b.top, barRectWidth, barRectHeight, barRectHeight, b);
        }
    }

    @Override
    protected void paintDeterminate(Graphics g, JComponent c) {
        if (!(g instanceof Graphics2D)) return;
        Graphics2D g2 = (Graphics2D) g;


        Insets b = this.progressBar.getInsets();
        int barRectWidth = this.progressBar.getWidth() - (b.right + b.left);
        int barRectHeight = this.progressBar.getHeight() - (b.top + b.bottom);

        paintMatrixEffect((Graphics2D) g, barRectWidth, barRectHeight);

        int amountFull = this.getAmountFull(b, barRectWidth, barRectHeight);

        // Clamp cypher so it never clips
        int cypherPosition = amountFull - cypherIcon.getIconWidth() / 2;
        if (cypherPosition + cypherIcon.getIconWidth() > barRectWidth) {
            cypherPosition = barRectWidth - cypherIcon.getIconWidth();
        }

        // Paint the cypher icon
        cypherIcon.paintIcon(c, g2, b.left + cypherPosition, b.top);

        if (this.progressBar.isStringPainted()) {
            this.paintString(g2, b.left, b.top, barRectWidth, barRectHeight, barRectHeight, b);
        }
    }

    private static final int COLUMN_WIDTH = 15; // Ancho de las columnas, ajusta según sea necesario
    private static final int CHAR_HEIGHT = 7; // Altura aproximada de cada carácter

    // Clase interna para representar cada columna
    private class MatrixColumn {

        int speed;
        int x; // Posición en X de la columna
        int startY; // Posición inicial en Y para el efecto de desplazamiento continuo
        int offset; // Desplazamiento actual desde startY
        char[] chars;

        MatrixColumn(int x, int h, Random random) {
            this.speed = 1 + random.nextInt(3);
            this.x = x;
            this.startY = -h; // Comenzar fuera del área visible en la parte superior
            this.offset = 0;
            int length = h / CHAR_HEIGHT + random.nextInt(5); // Determina la longitud de la columna
            this.chars = new char[length];
            for (int i = 0; i < length; i++) {
                chars[i] = MATRIX_CHARS.charAt(random.nextInt(MATRIX_CHARS.length()));
            }
        }

        void draw(Graphics2D g2, int h, int w) {
            Random random = new Random();
            // Dibujar cada carácter en la columna
            for (int i = 0; i < chars.length; i++) {
                int posY = startY + offset + i * CHAR_HEIGHT;
                if (random.nextInt(50) == 0) { // Cambia aleatoriamente el carácter a veces
                    chars[i] = MATRIX_CHARS.charAt(random.nextInt(MATRIX_CHARS.length()));
                }
                if (posY >= -w && posY < h) {
                    float alpha = (float) i / chars.length; // Gradualmente aumenta
                    g2.setFont(new Font("Monospaced", Font.BOLD, JBUI.scale(random.nextInt(3) + 10))); // Tamaños de fuente de 10 a 12
                    g2.setColor(new Color(MATRIX_COLOR.getRed(), MATRIX_COLOR.getGreen(), MATRIX_COLOR.getBlue(), (int)(alpha * 255)));
                    g2.drawString(String.valueOf(chars[i]), x, posY);
                }
            }

            offset += speed;
            if (startY + offset >= h) {
                offset = -h; // Reiniciar la columna cuando haya salido del área visible
            }
        }
    }

    private void initMatrixEffect(int w, int h) {
        Random random = new Random();
        int numColumns = w / COLUMN_WIDTH;
        matrixColumns = new MatrixColumn[numColumns];
        for (int i = 0; i < numColumns; i++) {
            matrixColumns[i] = new MatrixColumn(i * COLUMN_WIDTH, h, random);
        }
    }


    private static final String MATRIX_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+=-[]{}|;:<>,.?/漢字";

    private static final Color MATRIX_COLOR = new Color(0, 255, 0); // Color verde característico
    private void paintMatrixEffect(Graphics2D g2, int h, int w) {
        g2.setFont(new Font("Monospaced", Font.BOLD, JBUI.scale(12))); // Fuente monoespaciada
        g2.setColor(MATRIX_COLOR);

        for (MatrixColumn column : matrixColumns) {
            column.draw(g2, h, w);
        }
    }


    @Override
    protected int getBoxLength(int availableLength, int otherDimension) {
        return cypherIcon.getIconWidth();
    }
}
