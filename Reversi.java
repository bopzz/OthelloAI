import java.util.ArrayList;
import java.util.Arrays;

enum TURN {
    WHITE,
    BLACK
}

public class Reversi implements FormalModel<char[][], TURN>{
    @Override
    public TURN toMove(char[][] state) {
        return turn;
    }

    @Override
    public ArrayList action(char[][] state) {
        return nextLegalMove;
    }

    @Override
    public char[][] result(char[][] state, int[] move) {
        move(move[0], move[1]);
        return board;
    }

    @Override
    public boolean isTerminal(char[][] state) {
        return isGameOver;
    }

    @Override
    public int utility(char[][] state, TURN turn) {
        return utility();
    }
    
    public static int count;
    int size;
    public char[][] board; 
    boolean isGameOver = false;
    TURN turn; 
    TURN currentTurn;
    ArrayList<LegalMove> nextLegalMove;

    public class LegalMove {
        int row;
        int col;
        int dir;
        public LegalMove(int row, int col, int dir) {
            this.row = row;
            this.col = col;
            this.dir = dir;
        }

        public int[] getLegalMove() {
            int pos[] = {row, col, dir};
            return pos;
        }

        public void print() {
            System.out.println(row + " " + col + " " + dir);
        }
    }

    public Reversi(int size, TURN turn) {
        this.size = size;
        this.turn = turn;
        nextLegalMove = new ArrayList<>();
        board = new char[size][size];
        this.reset();
        searchLegal();   
    }

    public Reversi(Reversi copy) {
        
        this.size = copy.size;
        this.board = new char[size][size];
        for(int i = 0; i < size; i++) {
            for(int k = 0; k < size; k++) {
                this.board[i][k] = copy.board[i][k];
            }
        }
        this.turn = copy.turn;
        this.isGameOver = copy.isGameOver;
        this.nextLegalMove = new ArrayList<>();
        for(LegalMove move : copy.nextLegalMove) {
            this.nextLegalMove.add(move);
        }
    }

    public void markLegal(){
        for (LegalMove m : nextLegalMove){
            board[m.getLegalMove()[0]][m.getLegalMove()[1]] = '.';
        }
    }
    
    // directions: up = 1, right = 2, down = 3, left = 4, up-right = 5, down-right = 6, down-left = 7, down-right = 8
    public void searchLegal(){
        nextLegalMove = new ArrayList<LegalMove>();
        char currentOtherColor = (turn == TURN.BLACK) ? 'o' : 'x';
        
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                if(board[row][col] != ' ' && board[row][col] == currentOtherColor) {
                    for(int dir = 1; dir <= 8; dir++) {
                        try {
                            checkAndAdd(row, col, dir, currentOtherColor);
                        } catch (Exception e) {
                        }
                    }
                }            
            }
        }
    }

    public void checkAndAdd(int row, int col, int dirOfMove, char fce) {
        switch(dirOfMove) {
            case 1: //up
            if(board[row+1][col] == ' ') {
                for(int i = 1; row-i >= 0; i++) {
                    if(board[row-i][col] == ' ') {
                        break;
                    } else if(board[row-i][col] != fce) {
                        nextLegalMove.add(new LegalMove(row+1, col, dirOfMove));
                        break;
                    }
                }
            }
            break;
                
            case 2: //right
                if(board[row][col-1] == ' ') {
                    for(int i = 1; col+i < size; i++) {
                        if(board[row][col+i] == ' ') {
                            break;
                        } else if(board[row][col+i] != fce) {
                            nextLegalMove.add(new LegalMove(row, col-1, dirOfMove));
                            break;
                        }
                    }
                }
            break;
            case 3: //down
                if(board[row-1][col] == ' ') {
                    for(int i = 1; row+i < size; i++) {
                        if(board[row+i][col] == ' ') {
                            break;
                        } else if(board[row+i][col] != fce) {
                            nextLegalMove.add(new LegalMove(row-1, col, dirOfMove));
                            break;
                        }
                    }
                }
                break;
            case 4: //left
                if(board[row][col+1] == ' ') {
                    for(int i = 1; col-i >= 0; i++) {
                        if(board[row][col-i] == ' ') {
                            break;
                        } else if(board[row][col-i] != fce) {
                            nextLegalMove.add(new LegalMove(row, col+1, dirOfMove));
                            break;
                        }
                    }
                }
                break;
            case 5: // up-right
                if(board[row+1][col-1] == ' ') {
                    for(int i = 1; row - i >= 0 && col + i <size; i++) {
                        if(board[row-i][col+i] == ' ') {
                            break;
                        } else if(board[row-i][col+i] != fce) {
                            nextLegalMove.add(new LegalMove(row+1, col-1, dirOfMove));
                            break;
                        }
                    }
                }
                break;
            case 6: // down-right
                if(board[row-1][col-1] == ' ') {
                    for(int i = 1; row + i < size && col + i <size; i++) {
                        if(board[row+i][col+i] == ' ') {
                            break;
                        } else if(board[row+i][col+i] != fce) {
                            nextLegalMove.add(new LegalMove(row-1, col-1, dirOfMove));
                            break;
                        }
                    }
                }
                break;

            case 7: // down-left
                if(board[row-1][col+1] == ' ') {
                    for(int i = 1; row + i < size  && col - i >= 0; i++) {
                        if(board[row+i][col-i] == ' ') {
                            break;
                        } else if(board[row+i][col-i] != fce) {
                            nextLegalMove.add(new LegalMove(row-1, col+1, dirOfMove));
                            break;
                        }
                    }
                }
                break;

            case 8: // up-left
                if(board[row+1][col+1] == ' ') {
                    for(int i = 1; row - i >=0 && col - i >= 0; i++) {
                        if(board[row-i][col-i] == ' ') {
                            break;
                        } else if(board[row-i][col-i] != fce) {
                            nextLegalMove.add(new LegalMove(row+1, col+1, dirOfMove));
                            break;
                        }
                    }
                }
                break;
                
        }
    }

    public ArrayList<int[]> legalMoveCheck(int row, int col) {
        
        ArrayList<int[]> moveSet = new ArrayList<int[]>();

        for(LegalMove move : nextLegalMove) {
            if(row == move.getLegalMove()[0] && col == move.getLegalMove()[1]) {
                moveSet.add(move.getLegalMove());
            }
        }
        return moveSet;
    }

    public boolean moveWithGraphics(int row, int col) {
        
        ArrayList<int[]> moveSet = legalMoveCheck(row, col);

        if(nextLegalMove.isEmpty()) {
            System.out.print(turn + " has no legal moves. ");
            turn = (turn == TURN.BLACK) ? TURN.WHITE : TURN.BLACK;
            System.out.println(turn + " continues");
            return false;
        }

        if(moveSet.isEmpty()) {
            System.out.println(turn);
            System.out.println("Illegal move, please choose again!");
            return false;
        }

        move(row, col);
        printBoard();
        space(1);
        if(isGameOver) {
            int finalScore = utility();
            if(finalScore > 0) {
                System.out.println("GAME OVER, BLACK WINS!");
            } else if (finalScore == 0) {
                System.out.println("GAME OVER, TIES.");
            } else {
                System.out.println("GAME OVER, WHITE WINS!");
            }
        }
        return true;
    }

    public void move(int row, int col) {
        ArrayList<int[]> moveSet = legalMoveCheck(row, col);

        while(!moveSet.isEmpty()) {
            int[] move = moveSet.remove(0);
            int i = 1;
            char ori;
            board[row][col] = (turn == TURN.BLACK) ? 'x' : 'o';
            switch(move[2]) {
                case 1:
                    ori = board[row-1][col];
                    while(row-i >= 0 && board[row-i][col] == ori) {
                        board[row-i][col] = (turn == TURN.BLACK) ? 'x' : 'o';
  
                        i++;
                    }
                    break;  
                case 2:
                    ori = board[row][col+1];
                    while(col+i < size && board[row][col+i] == ori) {
                        board[row][col+i] = (turn == TURN.BLACK) ? 'x' : 'o';
                        i++;
                    }
                    break;  
                case 3:
                    ori = board[row+1][col];
                    while(row+i < size && board[row+i][col] == ori) {
                        board[row+i][col] = (turn == TURN.BLACK) ? 'x' : 'o';
                        i++;
                    }
                    break;
                case 4:
                    ori = board[row][col-1];
                    while(col - i >= 0 && board[row][col-i] == ori) {
                        board[row][col-i] = (turn == TURN.BLACK) ? 'x' : 'o';
                        i++;
                    }
                    break;
                case 5:
                    ori = board[row-1][col+1];
                    while(col+i < size && row -i >= 0 && board[row-i][col+i] == ori) {
                        board[row-i][col+i] = (turn == TURN.BLACK) ? 'x' : 'o';
                        i++;
                    }
                    break;
                case 6:
                    ori = board[row+1][col+1];
                        while(row + i < size && col + i < size && board[row+i][col+i] == ori) {
                            board[row+i][col+i] = (turn == TURN.BLACK) ? 'x' : 'o';
                            i++;
                        }
                    break;
                case 7:
                    ori = board[row+1][col-1];
                        while(row+i < size && col-i >= 0 && board[row+i][col-i] == ori) {
                            board[row+i][col-i] = (turn == TURN.BLACK) ? 'x' : 'o';
                            i++;
                        }
                    break;
                case 8:
                    ori = board[row-1][col-1];
                        while(row-i >= 0 && col-i >= 0 && board[row-i][col-i] == ori) {
                            board[row-i][col-i] = (turn == TURN.BLACK) ? 'x' : 'o';
                            i++;
                        }
                    break;
                
            }  
        }
      
        
        if(countAll() == size*size) {
            isGameOver = true;
            
        } else {
            turn = (turn == TURN.BLACK) ? TURN.WHITE : TURN.BLACK;
            
            searchLegal();
            if(nextLegalMove.isEmpty()) {
                turn = (turn == TURN.BLACK) ? TURN.WHITE : TURN.BLACK; 
                searchLegal();
                
                if(nextLegalMove.isEmpty()) {
                    isGameOver = true;
                }   
                                                                                       
            }
            
        }
    }

    public boolean isGameOver() {
        return isGameOver;
    }
    // state: 0 = equal ties continue, 1 = BLACK wins, 2 = WHITE wins, 3 = BLACK has no legal moves, 4 = WHITE has no legal moves, 5 = TIE
    public int utility() {
        int numBlack = 0;
        int numWhite = 0;
        for(char[] row : board) {
            for(char col : row) {
                if(col == 'x') numBlack++;
                else if (col == 'o') numWhite++;
            }
        }
        return numBlack - numWhite;
    }

    public int countAll() {
        int total = 0;
        for(char[] row : board) {
            for(char col : row) {
                if(col != ' ')
                    total++;
            }
        }
        return total;
    }

    public void printBoard() {
        System.out.print(" ");
        for(int i = 0; i < size; i++) {
            System.out.printf(" %s", getCharForNumber(i+1));
        }

        space(1);
        for(int i = 0; i < size; i ++) {
            System.out.print(i+1);
            for(int k = 0; k < size; k++) { 
                System.out.printf(" %c", board[i][k]);
            }
            System.out.printf(" ");
            System.out.println(i+1);
        }
        System.out.print(" ");
        for(int i = 0; i < size; i++) {
            System.out.printf(" %s", getCharForNumber(i+1));
        }
        space(2);

    }



    public static String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 96)) : null;
    }

    public void reset() {
        board = new char[size][size];
        
        for(char[] row : board) {
            Arrays.fill(row, ' ');
        } 

        board[size/2-1][size/2-1] = 'o';
        board[size/2][size/2-1] = 'x';
        board[size/2-1][size/2] = 'x';
        board[size/2][size/2] = 'o';
    
    }

    public static void space(int time) {
        for(int i = 0; i < time; i++) {
            System.out.println();
        }
    }

    public int[] optimalMove() {
        return this.minValue()[1];
    }

    public int[][] maxValue() {
        int[][] result = new int[2][2];
        if(isGameOver()) {
            count++;
            result[0][0] = utility();
            return result;
        }

        result[0][0] = -1* (size*size) - 1;

        for(int i = 0; i < nextLegalMove.size(); i++) {

            LegalMove move = nextLegalMove.get(i);
            int row = move.getLegalMove()[0];
            int col = move.getLegalMove()[1];
            while(  i < nextLegalMove.size()-1 &&
                    row == nextLegalMove.get(i+1).getLegalMove()[0] &&
                    col == nextLegalMove.get(i+1).getLegalMove()[1]) {
                i++;
            }

            Reversi next = new Reversi(this);
            
            TURN before = this.turn;
            next.move(row, col);
            int[][] nextResult = (before != next.turn) ? next.minValue() : next.maxValue();

            if(nextResult[0][0] > result[0][0]) {
                result[0][0] = nextResult[0][0];
                result[1][0] = row;
                result[1][1] = col;
            }
        }
        return result;
    }
    
    public int[][] minValue() {
        int[][] result = new int[2][2];
        if(isGameOver()) {
            count++;
            result[0][0] = utility();
            return result;
        }

        result[0][0] = (size*size) + 1;

        for(int i = 0; i < nextLegalMove.size(); i++) {

            LegalMove move = nextLegalMove.get(i);
            int row = move.getLegalMove()[0];
            int col = move.getLegalMove()[1];
            while(i < nextLegalMove.size() - 1 &&
                 row == nextLegalMove.get(i+1).getLegalMove()[0] &&
                 col == nextLegalMove.get(i+1).getLegalMove()[1]) {
                i++;
            }
            
            Reversi next = new Reversi(this);
            
            TURN before = this.turn;
            next.move(row, col);
            int[][] nextResult = (before != next.turn) ? next.maxValue() : next.minValue(); 
            
            if(nextResult[0][0] < result[0][0]) {
                result[0][0] = nextResult[0][0];
                result[1][0] = row;
                result[1][1] = col;
                
            }
        }
        return result;
    }

    public int[] alphaBetaOptimalMove() {
        return this.minValue(-1* (size*size) - 1, (size*size) + 1)[1];
    }

    public int[][] maxValue(int alpha, int beta) {
        int[][] result = new int[2][2];
        if(isGameOver()) {
            count++;
            result[0][0] = utility();
            return result;
        }

        result[0][0] = -1* (size*size) - 1;

        for(int i = 0; i < nextLegalMove.size(); i++) {

            LegalMove move = nextLegalMove.get(i);
            int row = move.getLegalMove()[0];
            int col = move.getLegalMove()[1];
            while(  i < nextLegalMove.size()-1 &&
                    row == nextLegalMove.get(i+1).getLegalMove()[0] &&
                    col == nextLegalMove.get(i+1).getLegalMove()[1]) {
                i++;
            }

            Reversi next = new Reversi(this);
            TURN before = this.turn;
            next.move(row, col);
            int[][] nextResult = (before != next.turn) ? next.minValue(alpha, beta) : next.maxValue(alpha, beta);

            if(nextResult[0][0] > result[0][0]) {
                result[0][0] = nextResult[0][0];
                result[1][0] = row;
                result[1][1] = col;
                alpha = Integer.max(alpha, result[0][0]);
            }
            if(result[0][0] >= beta) {
                return result;
            }
        }
        

        return result;
    }
    
    public int[][] minValue(int alpha, int beta) {
        int[][] result = new int[2][2];
        if(isGameOver()) {
            count++;
            result[0][0] = utility();
            return result;
        }

        result[0][0] = (size*size) + 1;

        for(int i = 0; i < nextLegalMove.size(); i++) {

            LegalMove move = nextLegalMove.get(i);
            int row = move.getLegalMove()[0];
            int col = move.getLegalMove()[1];
            while(  i < nextLegalMove.size() - 1 &&
                    row == nextLegalMove.get(i+1).getLegalMove()[0] &&
                    col == nextLegalMove.get(i+1).getLegalMove()[1]) {
                i++;
            }

            Reversi next = new Reversi(this);
            TURN before = this.turn;

            next.move(row, col);
            int[][] nextResult = (before != next.turn) ? next.maxValue(alpha, beta) : next.minValue(alpha, beta); 

            if(nextResult[0][0] < result[0][0]) {
                result[0][0] = nextResult[0][0];
                result[1][0] = row;
                result[1][1] = col;
                beta = Integer.min(beta, result[0][0]);
            }
            if(result[0][0] <= alpha) {
                return result;
            }

        }
        return result;
    }

    public int[] HeuristicAlphaBetaOptimalMove(int depth) {
        return this.HMaxValue(-1* (size*size) - 1, (size*size) + 1, depth, 0)[1];
    }

    public int[][] HMaxValue(int alpha, int beta, int depth, int current) {
        int[][] result = new int[2][2];
        if(isGameOver() || current > depth) {
            count++;
            result[0][0] = utility();
            return result;
        }

        result[0][0] = -1* (size*size) - 1;

        for(int i = 0; i < nextLegalMove.size(); i++) {

            LegalMove move = nextLegalMove.get(i);
            int row = move.getLegalMove()[0];
            int col = move.getLegalMove()[1];
            while(  i < nextLegalMove.size()-1 &&
                    row == nextLegalMove.get(i+1).getLegalMove()[0] &&
                    col == nextLegalMove.get(i+1).getLegalMove()[1]) {
                i++;
            }

            Reversi next = new Reversi(this);
            TURN before = this.turn;
            next.move(row, col);
            int[][] nextResult = (before != next.turn) ? next.HMinValue(alpha, beta, depth, current+1) : next.HMaxValue(alpha, beta, depth, current+1);

            if(nextResult[0][0] > result[0][0]) {
                result[0][0] = nextResult[0][0];
                result[1][0] = row;
                result[1][1] = col;
                alpha = Integer.max(alpha, result[0][0]);
            }
            if(result[0][0] >= beta) {
                return result;
            }
        }
        return result;
    }
    
    public int[][] HMinValue(int alpha, int beta, int depth, int current) {
        int[][] result = new int[2][2];
        if(isGameOver() || current >= depth) {
            count++;
            result[0][0] = utility();
            return result;
        }

        result[0][0] = (size*size) + 1;

        for(int i = 0; i < nextLegalMove.size(); i++) {

            LegalMove move = nextLegalMove.get(i);
            int row = move.getLegalMove()[0];
            int col = move.getLegalMove()[1];
            while(  i < nextLegalMove.size() - 1 &&
                    row == nextLegalMove.get(i+1).getLegalMove()[0] &&
                    col == nextLegalMove.get(i+1).getLegalMove()[1]) {
                i++;
            }
            
            Reversi next = new Reversi(this);
            TURN before = this.turn;

            
            next.move(row, col);
            int[][] nextResult = (before != next.turn) ? next.HMaxValue(alpha, beta, depth, current+1) : next.HMinValue(alpha, beta, depth, current+1); 

            if(nextResult[0][0] < result[0][0]) {
                result[0][0] = nextResult[0][0];
                result[1][0] = row;
                result[1][1] = col;
                beta = Integer.min(beta, result[0][0]);
            }
            if(result[0][0] <= alpha) {
                return result;
            }

        }
        return result;
    }
}

