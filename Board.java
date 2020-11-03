import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Board {
    private final int[] tiles;
    private final int n;
    private Board twin = null;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles[0].length;
        this.tiles = new int[n * n];
        for (int i = 0; i < n * n; i++) {
            this.tiles[i] = tiles[i / n][i % n];
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n);
        for (int i = 0; i < n; i++) {
            s.append("\n");
            for (int j = 0; j < n; j++) {
                s.append(tiles[i * n + j] + " ");
            }
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int ham = 0;
        int pos;

        for (int i = 0; i < n * n; i++) {
            if (tiles[i] == 0) continue;
            pos = findPos(tiles[i]);
            if (pos != i) ham++;
        }
        return ham;
    }


    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int man = 0;
        int pos;

        for (int i = 0; i < n * n; i++) {
            if (tiles[i] == 0) continue;
            pos = findPos(tiles[i]);
            if (pos != i)
                man += Math.abs(i / n - pos / n) + Math.abs(i % n - pos % n);
        }

        return man;
    }

    private int findPos(int i) {
        int pos;
        if (i == 0) pos = n * n - 1;
        else pos = i - 1;
        return pos;
    }


    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return (Arrays.equals(this.tiles, that.tiles)) && (this.n == that.n);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int pos0 = 0;
        for (int i = 0; i < n * n; i++) {
            if (tiles[i] == 0) {
                pos0 = i;
                break;
            }
        }

        Stack<Board> stack = new Stack<>();
        int neighbour;
        if (pos0 / n - 1 >= 0) {
            neighbour = pos0 - n;
            stack.push(new Board(exch(neighbour, pos0)));
        }
        if (pos0 / n + 1 < n) {
            neighbour = pos0 + n;
            stack.push(new Board(exch(neighbour, pos0)));
        }
        if (pos0 % n - 1 >= 0) {
            neighbour = pos0 - 1;
            stack.push(new Board(exch(neighbour, pos0)));
        }
        if (pos0 % n + 1 < n) {
            neighbour = pos0 + 1;
            stack.push(new Board(exch(neighbour, pos0)));
        }
        return stack;
    }

    private int[][] exch(int a, int b) {
        int[] copy = new int[n * n];
        int[][] copy2d = new int[n][n];
        for (int i = 0; i < n * n; i++) {
            copy[i] = tiles[i];
        }
        int temp = copy[a];
        copy[a] = copy[b];
        copy[b] = temp;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy2d[i][j] = copy[i * n + j];
            }
        }
        return copy2d;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (twin == null) {
            int pos1;
            int pos2;
            do {
                pos1 = StdRandom.uniform(n * n);
            } while (tiles[pos1] == 0);

            do {
                pos2 = StdRandom.uniform(n * n);
            } while (tiles[pos2] == 0 || tiles[pos2] == tiles[pos1]);
            twin = new Board(exch(pos1, pos2));
        }

        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        Board b = new Board(tiles);
        System.out.println(b);
        System.out.println("Ham: " + b.hamming());
        System.out.println("Man: " + b.manhattan());
        System.out.println(b.isGoal());
        System.out.println("Neig: ");
        for (Board b1 : b.neighbors()) {
            System.out.println(b1);
        }
        System.out.println("Twin:");
        System.out.println(b.twin());
        // System.out.println(b.twin());
        // System.out.println(b.twin());
    }

}
