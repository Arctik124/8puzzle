import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Board {
    private final int[][] tiles;
    private final int n;
    private Board twin = null;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles[0].length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board  
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n);
        for (int i = 0; i < n; i++) {
            s.append("\n");
            for (int j = 0; j < n; j++) {
                s.append(tiles[i][j] + " ");
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
        boolean check;
        int[] pos;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                pos = findPos(tiles[i][j]);
                check = (pos[0] == i && pos[1] == j);
                if (!check) {
                    ham++;
                }

            }
        }
        return ham;
    }


    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int man = 0;
        boolean check;
        int[] pos;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                pos = findPos(tiles[i][j]);
                check = (pos[0] == i && pos[1] == j);
                if (!check) {
                    man += Math.abs(i - pos[0]) + Math.abs(j - pos[1]);
                }

            }
        }

        return man;
    }

    private int[] findPos(int i) {
        int[] pos = new int[2];
        if (i == 0) {
            pos[0] = n - 1;
            pos[1] = n - 1;
        }
        else {
            i--;
            pos[0] = i / n;
            pos[1] = i % n;
        }
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
        return (Arrays.deepEquals(this.tiles, that.tiles)) && (this.n == that.n);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[] pos0 = new int[2];
        outer:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    pos0[0] = i;
                    pos0[1] = j;
                    break outer;
                }
            }
        }
        Stack<Board> stack = new Stack<>();
        int[] neighbour = new int[2];
        if (pos0[0] - 1 >= 0) {
            neighbour[0] = pos0[0] - 1;
            neighbour[1] = pos0[1];
            stack.push(new Board(exch(neighbour, pos0)));
        }
        if (pos0[0] + 1 < n) {
            neighbour[0] = pos0[0] + 1;
            neighbour[1] = pos0[1];
            stack.push(new Board(exch(neighbour, pos0)));
        }
        if (pos0[1] - 1 >= 0) {
            neighbour[0] = pos0[0];
            neighbour[1] = pos0[1] - 1;
            stack.push(new Board(exch(neighbour, pos0)));
        }
        if (pos0[1] + 1 < n) {
            neighbour[0] = pos0[0];
            neighbour[1] = pos0[1] + 1;
            stack.push(new Board(exch(neighbour, pos0)));
        }
        return stack;
    }

    private int[][] exch(int[] a, int[] b) {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        int temp = copy[a[0]][a[1]];
        copy[a[0]][a[1]] = copy[b[0]][b[1]];
        copy[b[0]][b[1]] = temp;
        return copy;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (twin == null) {
            int[] pos1 = new int[2];
            int[] pos2 = new int[2];

            do {
                pos1[0] = StdRandom.uniform(n);
                pos1[1] = StdRandom.uniform(n);
            } while (tiles[pos1[0]][pos1[1]] == 0);


            do {
                pos2[0] = StdRandom.uniform(n);
                pos2[1] = StdRandom.uniform(n);
            } while (tiles[pos2[0]][pos2[1]] == 0
                    || tiles[pos2[0]][pos2[1]] == tiles[pos1[0]][pos1[1]]);
            twin = new Board(exch(pos1, pos2));
        }

        return twin;
        //
        // while (true) {
        //     int sw = StdRandom.uniform(4);
        //     int[] pos2 = pos1.clone();
        //     switch (sw) {
        //         case 0:
        //             if (pos1[0] - 1 >= 0) {
        //                 pos2[0] = pos1[0] - 1;
        //                 pos2[1] = pos1[1];
        //                 if (tiles[pos2[0]][pos2[1]] != 0)
        //                     return new Board(exch(pos1, pos2));
        //             }
        //             break;
        //         case 1:
        //             if (pos1[0] + 1 < n) {
        //                 pos2[0] = pos1[0] + 1;
        //                 pos2[1] = pos1[1];
        //                 if (tiles[pos2[0]][pos2[1]] != 0)
        //                     return new Board(exch(pos1, pos2));
        //             }
        //             break;
        //         case 2:
        //             if (pos1[1] - 1 >= 0) {
        //                 pos2[0] = pos1[0];
        //                 pos2[1] = pos1[1] - 1;
        //                 if (tiles[pos2[0]][pos2[1]] != 0)
        //                     return new Board(exch(pos1, pos2));
        //             }
        //             break;
        //         case 3:
        //             if (pos1[1] + 1 < n) {
        //                 pos2[0] = pos1[0];
        //                 pos2[1] = pos1[1] + 1;
        //                 if (tiles[pos2[0]][pos2[1]] != 0)
        //                     return new Board(exch(pos1, pos2));
        //             }
        //             break;
        //         default:
        //             break;
        //     }
        // if (pos1[0] - 1 >= 0 && sw == 0) {
        //     pos2[0] = pos1[0] - 1;
        //     pos2[1] = pos1[1];
        //     return new Board(exch(pos1, pos2));
        // }
        // if (pos1[0] + 1 < n && sw == 2) {
        //     pos2[0] = pos1[0] + 1;
        //     pos2[1] = pos1[1];
        //     return new Board(exch(pos1, pos2));
        // }
        // if (pos1[1] - 1 >= 0 && sw == 3) {
        //     pos2[0] = pos1[0];
        //     pos2[1] = pos1[1] - 1;
        //     return new Board(exch(pos1, pos2));
        // }
        // if (pos1[1] + 1 < n && sw == 1) {
        //     pos2[0] = pos1[0];
        //     pos2[1] = pos1[1] + 1;
        //     return new Board(exch(pos1, pos2));
        // }
        // }
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
        // System.out.println("Ham: " + b.hamming());
        // System.out.println("Man: " + b.manhattan());
        // System.out.println(b.isGoal());
        // System.out.println("Neig: ");
        // for (Board b1 : b.neighbors()) {
        //     System.out.println(b1);
        // }
        System.out.println("Twin:");
        System.out.println(b.twin());
        System.out.println(b.twin());
        System.out.println(b.twin());
    }

}
