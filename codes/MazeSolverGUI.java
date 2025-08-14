import java.awt.*;
import java.util.*;
import javax.swing.*;

public class MazeSolverGUI {

  private static final int ROWS = 10;
  private static final int COLS = 10;
  private static final char WALL = '#';
  private static final char PATH = '.';
  private static final char START = 'S';
  private static final char END = 'E';
  private static final char SOLUTION = '*';

  private JFrame frame;
  private JPanel mazePanel;
  private char[][] maze;
  private boolean[][] visited;

  public MazeSolverGUI() {
    maze = new char[ROWS][COLS];
    visited = new boolean[ROWS][COLS];
    initializeGUI();
  }

  private void initializeGUI() {
    frame = new JFrame("Maze Solver");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 600);

    // Maze Panel
    mazePanel = new JPanel();
    mazePanel.setLayout(new GridLayout(ROWS, COLS));
    frame.add(mazePanel, BorderLayout.CENTER);

    // Control Panel
    JPanel controlPanel = new JPanel();
    JButton generateButton = new JButton("Generate Maze");
    JButton solveButton = new JButton("Solve Maze");
    controlPanel.add(generateButton);
    controlPanel.add(solveButton);
    frame.add(controlPanel, BorderLayout.SOUTH);

    // Button Listeners
    generateButton.addActionListener(e -> generateMaze());
    solveButton.addActionListener(e -> {
      if (solveMazeDFS(0, 0)) {
        JOptionPane.showMessageDialog(frame, "Maze Solved!");
        updateMazePanel();
      } else {
        JOptionPane.showMessageDialog(frame, "No solution found for the maze.");
      }
    });

    frame.setVisible(true);
  }

  private void generateMaze() {
    Random rand = new Random();
    mazePanel.removeAll();
    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        maze[i][j] = (rand.nextInt(3) == 0) ? WALL : PATH;
        JButton cell = new JButton();
        if (maze[i][j] == WALL) {
          cell.setBackground(Color.BLACK);
        } else {
          cell.setBackground(Color.WHITE);
        }
        cell.setEnabled(false);
        mazePanel.add(cell);
      }
    }
    maze[0][0] = START;
    maze[ROWS - 1][COLS - 1] = END;
    visited = new boolean[ROWS][COLS];
    updateMazePanel();
  }

  private boolean solveMazeDFS(int row, int col) {
    if (row < 0 || row >= ROWS || col < 0 || col >= COLS ||
        maze[row][col] == WALL || visited[row][col]) {
      return false;
    }

    if (row == ROWS - 1 && col == COLS - 1) {
      maze[row][col] = SOLUTION;
      return true;
    }

    visited[row][col] = true;
    maze[row][col] = SOLUTION;

    // Explore neighbors: up, right, down, left
    if (solveMazeDFS(row - 1, col) || solveMazeDFS(row, col + 1) ||
        solveMazeDFS(row + 1, col) || solveMazeDFS(row, col - 1)) {
      return true;
    }

    // Backtrack
    maze[row][col] = PATH;
    return false;
  }

  private void updateMazePanel() {
    mazePanel.removeAll();
    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        JButton cell = new JButton();
        if (maze[i][j] == WALL) {
          cell.setBackground(Color.BLACK);
        } else if (maze[i][j] == SOLUTION) {
          cell.setBackground(Color.GREEN);
        } else if (maze[i][j] == START) {
          cell.setBackground(Color.BLUE);
        } else if (maze[i][j] == END) {
          cell.setBackground(Color.RED);
        } else {
          cell.setBackground(Color.WHITE);
        }
        cell.setEnabled(false);
        mazePanel.add(cell);
      }
    }
    frame.revalidate();
    frame.repaint();
  }

  public static void main(String[] args) { new MazeSolverGUI(); }
}
