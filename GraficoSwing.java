import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class GraficoSwing {
    private GraficoSwing() {
    }

    public static void main(String[] args) throws IOException {
        List<Long> values = new ArrayList<>();
        Path csv = Paths.get(args.length == 0 ? "resultados/benchmark.csv" : args[0]);
        try (var lines = Files.lines(csv)) {
            lines.skip(1).limit(100).forEach(line -> values.add(Long.parseLong(line.substring(line.lastIndexOf(',') + 1))));
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Benchmark de algoritmos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1_000, 600);
            frame.add(new ChartPanel(values));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static final class ChartPanel extends JPanel {
        private final List<Long> values;

        private ChartPanel(List<Long> values) {
            this.values = values;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (values.isEmpty()) return;
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int left = 60, top = 30, width = getWidth() - 90, height = getHeight() - 70;
            long max = values.stream().mapToLong(Long::longValue).max().orElse(1L);
            g.setColor(Color.DARK_GRAY);
            g.drawLine(left, top, left, top + height);
            g.drawLine(left, top + height, left + width, top + height);
            g.setColor(new Color(35, 115, 190));
            g.setStroke(new BasicStroke(2f));
            int previousX = left;
            int previousY = top + height - (int) (values.get(0) * height / (double) max);
            for (int i = 1; i < values.size(); i++) {
                int x = left + i * width / (values.size() - 1);
                int y = top + height - (int) (values.get(i) * height / (double) max);
                g.drawLine(previousX, previousY, x, y);
                previousX = x;
                previousY = y;
            }
        }
    }
}
