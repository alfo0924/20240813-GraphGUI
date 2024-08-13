import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GraphApp extends JFrame {
    private JPanel graphPanel;
    private JTextField nodesField, edgesField;
    private JButton generateButton;
    private Random random = new Random();
    private ArrayList<Point> nodes = new ArrayList<>();
    private ArrayList<int[]> edges = new ArrayList<>();

    public GraphApp() {
        setTitle("Graph Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGraph(g);
            }
        };
        graphPanel.setBackground(Color.WHITE);

        JPanel controlPanel = new JPanel();
        nodesField = new JTextField(5);
        edgesField = new JTextField(5);
        generateButton = new JButton("Generate Graph");

        controlPanel.add(new JLabel("Nodes:"));
        controlPanel.add(nodesField);
        controlPanel.add(new JLabel("Edges:"));
        controlPanel.add(edgesField);
        controlPanel.add(generateButton);

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateGraph();
            }
        });

        add(graphPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void generateGraph() {
        try {
            int numNodes = Integer.parseInt(nodesField.getText());
            int numEdges = Integer.parseInt(edgesField.getText());

            nodes.clear();
            edges.clear();

            // Generate nodes
            for (int i = 0; i < numNodes; i++) {
                nodes.add(new Point(random.nextInt(700) + 50, random.nextInt(500) + 50));
            }

            // Generate edges
            for (int i = 0; i < numEdges; i++) {
                int start = random.nextInt(numNodes);
                int end = random.nextInt(numNodes);
                if (start != end) {
                    edges.add(new int[]{start, end});
                }
            }

            graphPanel.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for nodes and edges.");
        }
    }

    private void drawGraph(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw edges
        g2d.setColor(Color.BLACK);
        for (int[] edge : edges) {
            Point start = nodes.get(edge[0]);
            Point end = nodes.get(edge[1]);
            g2d.drawLine(start.x, start.y, end.x, end.y);
        }

        // Draw nodes
        g2d.setColor(new Color(173, 216, 230)); // Light blue
        for (int i = 0; i < nodes.size(); i++) {
            Point node = nodes.get(i);
            g2d.fillOval(node.x - 15, node.y - 15, 30, 30);
            g2d.setColor(Color.BLACK);
            g2d.drawString(Integer.toString(i), node.x - 5, node.y + 5);
            g2d.setColor(new Color(173, 216, 230));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GraphApp().setVisible(true);
            }
        });
    }
}