import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;


public class Solver {
    private boolean solvable;
    private Node goal;

    private class Node implements Comparable<Node> {

        private final Board board;
        private final int priority;
        private final int move;
        private final int man;
        private final Node previous;

        public Node(Board b, int manhattan, int mo, Node newPrevious) {
            board = b;
            man = manhattan;
            move = mo;
            priority = man + move;
            previous = newPrevious;
        }

        public Board getBoard() {
            return board;
        }

        public int getPriority() {
            return priority;
        }

        public int getMan() {
            return man;
        }

        public int getMove() {
            return move;
        }

        public Node getPrevious() {
            return previous;
        }

        public int compareTo(Node that) {
            return this.getPriority() - that.getPriority();
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {


        if (initial == null) {
            throw new IllegalArgumentException("Null board");
        }

        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> pqTwin = new MinPQ<>();

        Board twin = initial.twin();

        pq.insert(new Node(initial, initial.manhattan(), 0, null));
        pqTwin.insert(new Node(twin, twin.manhattan(), 0, null));

        Node currentNode = pq.min();
        Node currentNodeTwin = pqTwin.min();
        Node parentNode;
        Node parentNodeTwin;
        while (!currentNode.getBoard().isGoal() && !currentNodeTwin.getBoard().isGoal()) {

            parentNode = currentNode.getPrevious();
            parentNodeTwin = currentNodeTwin.getPrevious();

            if (parentNode == null || parentNodeTwin == null) {
                for (Board neigh : currentNode.getBoard().neighbors()) {
                    pq.insert(new Node(neigh, neigh.manhattan(), currentNode.getMove() + 1,
                                       currentNode));

                }
                for (Board neigh : currentNodeTwin.getBoard().neighbors()) {
                    pqTwin.insert(new Node(neigh, neigh.manhattan(), currentNodeTwin.getMove() + 1,
                                           currentNodeTwin));

                }
                currentNode = pq.delMin();

                currentNodeTwin = pqTwin.delMin();
                // System.out.println(first);
                continue;
            }

            for (Board neigh : currentNode.getBoard().neighbors()) {
                if (!neigh.equals(parentNode.getBoard())) {
                    pq.insert(new Node(neigh, neigh.manhattan(), currentNode.getMove() + 1,
                                       currentNode));
                }
            }

            for (Board neigh : currentNodeTwin.getBoard().neighbors()) {
                if (!neigh.equals(parentNodeTwin.getBoard())) {
                    pqTwin.insert(new Node(neigh, neigh.manhattan(), currentNodeTwin.getMove() + 1,
                                           currentNodeTwin));
                }
            }

            currentNode = pq.delMin();
            currentNodeTwin = pqTwin.delMin();

        }

        if (!currentNode.getBoard().isGoal() && currentNodeTwin.getBoard().isGoal()) {
            solvable = false;
            // System.out.println(currentNodeTwin.getBoard());

        }
        else {
            solvable = true;
            goal = currentNode;
        }
        // System.out.println("Total moves: " + move);
        // System.out.println(currentNode.getBoard());
        // System.out.println(currentNode.getMove());
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!solvable) {
            return -1;
        }
        return goal.getMove();
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) {
            return null;
        }
        Stack<Board> stack = new Stack<>();
        Node temp = goal;

        while (temp != null) {
            stack.push(temp.getBoard());
            temp = temp.getPrevious();
        }
        return stack;
    }

    // test client (see below)
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

        Solver solver = new Solver(new Board(tiles));
        System.out.println("Moves: " + solver.moves());
        int m = 0;
        if (solver.solvable) {
            for (Board b : solver.solution()) {
                System.out.println("Move: " + m);
                System.out.println(b.toString());
                m++;
            }
        }
    }

}


