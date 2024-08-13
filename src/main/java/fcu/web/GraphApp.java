package fcu.web;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GraphApp extends JFrame {
    private JPanel graphPanel;
    private JTextField nodesField, edgesField, startNodeField;
    private JButton generateButton, dfsButton, bfsButton;
    private Random random = new Random();
    private ArrayList<Point> nodes = new ArrayList<>();
    private ArrayList<int[]> edges = new ArrayList<>();
    private ArrayList<Integer> searchPath = new ArrayList<>();
    private boolean isDFS = false;
    private long executionTime;

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
        startNodeField = new JTextField(5);
        generateButton = new JButton("Generate Graph");
        dfsButton = new JButton("DFS");
        bfsButton = new JButton("BFS");

        controlPanel.add(new JLabel("Nodes:"));
        controlPanel.add(nodesField);
        controlPanel.add(new JLabel("Edges:"));
        controlPanel.add(edgesField);
        controlPanel.add(generateButton);
        controlPanel.add(new JLabel("Start Node:"));
        controlPanel.add(startNodeField);
        controlPanel.add(dfsButton);
        controlPanel.add(bfsButton);

        generateButton.addActionListener(e -> generateGraph());
        dfsButton.addActionListener(e -> performSearch(true));
        bfsButton.addActionListener(e -> performSearch(false));

        add(graphPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void generateGraph() {
        try {
            int numNodes = Integer.parseInt(nodesField.getText());
            int numEdges = Integer.parseInt(edgesField.getText());

            nodes.clear();
            edges.clear();
            searchPath.clear();

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

    private void performSearch(boolean isDFS) {
        this.isDFS = isDFS;
        try {
            int startNode = Integer.parseInt(startNodeField.getText());
            if (startNode < 0 || startNode >= nodes.size()) {
                throw new IllegalArgumentException("Invalid start node");
            }

            searchPath.clear();
            long startTime = System.nanoTime();
            if (isDFS) {
                dfs(startNode, new boolean[nodes.size()]);
            } else {
                bfs(startNode);
            }
            long endTime = System.nanoTime();
            executionTime = endTime - startTime;
            graphPanel.repaint();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid start node.");
        }
    }

    private void dfs(int node, boolean[] visited) {
        visited[node] = true;
        searchPath.add(node);
        for (int[] edge : edges) {
            if (edge[0] == node && !visited[edge[1]]) {
                dfs(edge[1], visited);
            } else if (edge[1] == node && !visited[edge[0]]) {
                dfs(edge[0], visited);
            }
        }
    }

    private void bfs(int startNode) {
        boolean[] visited = new boolean[nodes.size()];
        Queue<Integer> queue = new LinkedList<>();
        visited[startNode] = true;
        queue.offer(startNode);

        while (!queue.isEmpty()) {
            int node = queue.poll();
            searchPath.add(node);
            for (int[] edge : edges) {
                if (edge[0] == node && !visited[edge[1]]) {
                    visited[edge[1]] = true;
                    queue.offer(edge[1]);
                } else if (edge[1] == node && !visited[edge[0]]) {
                    visited[edge[0]] = true;
                    queue.offer(edge[0]);
                }
            }
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

        // Draw search path
        if (!searchPath.isEmpty()) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2));
            for (int i = 0; i < searchPath.size() - 1; i++) {
                Point start = nodes.get(searchPath.get(i));
                Point end = nodes.get(searchPath.get(i + 1));
                g2d.drawLine(start.x, start.y, end.x, end.y);
            }
            g2d.setStroke(new BasicStroke(1));
        }

        // Draw nodes
        for (int i = 0; i < nodes.size(); i++) {
            Point node = nodes.get(i);
            if (searchPath.contains(i)) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(new Color(173, 216, 230)); // Light blue
            }
            g2d.fillOval(node.x - 15, node.y - 15, 30, 30);
            g2d.setColor(Color.BLACK);
            g2d.drawString(Integer.toString(i), node.x - 5, node.y + 5);
        }

        // Draw search type and execution time
        if (!searchPath.isEmpty()) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String searchType = isDFS ? "DFS" : "BFS";
            String timeInfo = String.format("%s Path (Execution time: %.3f ms)",
                    searchType, executionTime / 1_000_000.0);
            g2d.drawString(timeInfo, 10, 30);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GraphApp().setVisible(true));
    }
}