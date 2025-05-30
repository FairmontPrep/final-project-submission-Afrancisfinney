import java.util.ArrayList;
import java.util.Arrays;

public class Client{
    
    private static final ArrayList<ArrayList<Integer>> inputMaze = new ArrayList<ArrayList<Integer>>() {{
        add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 1)));
        add(new ArrayList<>(Arrays.asList(0, 0, 0, 0,7, 0, 0, 0, 0, 1)));
        add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 1)));
        add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 3, 0, 0, 0, 1)));
        add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 6, 1, 0, 0, 1)));
        add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 1)));
        add(new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)));
    }};

    public static void main(String[] args) {
        ArrayList<String> path = findPath(inputMaze);
        System.out.println(path);
        printPath(inputMaze, path);
    }

    public static ArrayList<String> findPath(ArrayList<ArrayList<Integer>> maze) {
        
        ArrayList<int[]> edgeCells = new ArrayList<>();
        int rows = maze.size();
        int cols = maze.get(0).size();
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze.get(i).get(j) == 1 && isEdgeCell(i, j, rows, cols)) {
                    edgeCells.add(new int[]{i, j});
                }
            }
        }

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

    private static boolean dfs(ArrayList<ArrayList<Integer>> maze, int row, int col, boolean[][] visited, 
                             ArrayList<String> path, Direction lastDir, boolean hasTurned) {
      
        if (row < 0 || row >= maze.size() || col < 0 || col >= maze.get(0).size() || 
            maze.get(row).get(col) != 1 || visited[row][col]) {
            return false;
        }

        visited[row][col] = true;
        path.add("A[" + row + "][" + col + "]");

        if (path.size() > 1 && isEdgeCell(row, col, maze.size(), maze.get(0).size())) {
            int[] start = parseCoordinate(path.get(0));
            if (isAdjacentWall(start[0], start[1], row, col, maze.size(), maze.get(0).size()) && hasTurned) {
                return true;
            }
        }

  
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

    private static void printPath(ArrayList<ArrayList<Integer>> maze, ArrayList<String> path) {
        if (path.isEmpty()) {
            System.out.println("No valid path found");
            return;
        }


        String[][] display = new String[maze.size()][maze.get(0).size()];
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(0).size(); j++) {
                display[i][j] = " "; 
            }
        }

       

        for (String coord : path) {
            int[] pos = parseCoordinate(coord);
            display[pos[0]][pos[1]] = "1";
        }

        
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