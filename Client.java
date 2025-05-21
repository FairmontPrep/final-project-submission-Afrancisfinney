import java.util.ArrayList;

public class Client {
    // Static variable for the maze input as specified in requirements
    private static final int[][] inputMaze = {
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {0, 0, 0, 0, 7, 0, 0, 0, 0, 1},
        {0, 0, 0, 0, 4, 0, 0, 0, 1, 1},
        {0, 0, 0, 1, 0, 0, 9, 0, 0, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public static void main(String[] args) {
        ArrayList<String> path = findPath(inputMaze);
        System.out.println(path);
        printPath(inputMaze, path);
    }

    public static ArrayList<String> findPath(int[][] maze) {
        // Find all edge cells that are 1s
        ArrayList<int[]> edgeCells = new ArrayList<>();
        int rows = maze.length;
        int cols = maze[0].length;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 1 && isEdgeCell(i, j, rows, cols)) {
                    edgeCells.add(new int[]{i, j});
                }
            }
        }

        // Try each edge cell as starting point
        for (int[] start : edgeCells) {
            ArrayList<String> path = new ArrayList<>();
            boolean[][] visited = new boolean[rows][cols];
            if (dfs(maze, start[0], start[1], visited, path, Direction.NONE, false)) {
                return path;
            }
        }
        
        return new ArrayList<>();
    }

    private static boolean isEdgeCell(int row, int col, int rows, int cols) {
        return row == 0 || row == rows - 1 || col == 0 || col == cols - 1;
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }

    private static boolean dfs(int[][] maze, int row, int col, boolean[][] visited, 
                             ArrayList<String> path, Direction lastDir, boolean hasTurned) {
        // Check bounds and if cell is valid
        if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length || 
            maze[row][col] != 1 || visited[row][col]) {
            return false;
        }

        // Mark as visited and add to path
        visited[row][col] = true;
        path.add("A[" + row + "][" + col + "]");

        // Check if we're on an adjacent wall with a turn made
        if (path.size() > 1 && isEdgeCell(row, col, maze.length, maze[0].length)) {
            int[] start = parseCoordinate(path.get(0));
            if (isAdjacentWall(start[0], start[1], row, col, maze.length, maze[0].length) && hasTurned) {
                return true;
            }
        }

        // Try all directions
        Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        for (Direction dir : directions) {
            int newRow = row, newCol = col;
            switch (dir) {
                case UP: newRow--; break;
                case DOWN: newRow++; break;
                case LEFT: newCol--; break;
                case RIGHT: newCol++; break;
            }

            boolean newTurn = hasTurned;
            if (lastDir != Direction.NONE && dir != lastDir) {
                newTurn = true;
            }

            if (dfs(maze, newRow, newCol, visited, path, dir, newTurn)) {
                return true;
            }
        }

        // Backtrack if no path found
        path.remove(path.size() - 1);
        visited[row][col] = false;
        return false;
    }

    private static boolean isAdjacentWall(int startRow, int startCol, int endRow, int endCol, int rows, int cols) {
        boolean startTop = startRow == 0;
        boolean startBottom = startRow == rows - 1;
        boolean startLeft = startCol == 0;
        boolean startRight = startCol == cols - 1;

        boolean endTop = endRow == 0;
        boolean endBottom = endRow == rows - 1;
        boolean endLeft = endCol == 0;
        boolean endRight = endCol == cols - 1;

        if (startTop) {
            return endLeft || endRight;
        } else if (startBottom) {
            return endLeft || endRight;
        } else if (startLeft) {
            return endTop || endBottom;
        } else if (startRight) {
            return endTop || endBottom;
        }
        return false;
    }

    private static int[] parseCoordinate(String coord) {
        String[] parts = coord.substring(2, coord.length() - 1).split("\\]\\[");
        return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }

    private static void printPath(int[][] maze, ArrayList<String> path) {
        if (path.isEmpty()) {
            System.out.println("No valid path found");
            return;
        }

        // Create a grid to represent the path
        String[][] display = new String[maze.length][maze[0].length];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                display[i][j] = " "; // Initialize with empty space
            }
        }

        // Mark the path with "1"
        for (String coord : path) {
            int[] pos = parseCoordinate(coord);
            display[pos[0]][pos[1]] = "1";
        }

        // Print the grid in the required format
        for (String[] row : display) {
            System.out.print("[");
            for (int i = 0; i < row.length; i++) {
                System.out.print(row[i]);
                if (i < row.length - 1) {
                    System.out.print(",");
                }
            }
            System.out.println("]");
        }
    }
}