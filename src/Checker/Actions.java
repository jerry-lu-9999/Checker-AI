package Checker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Actions {

    char computer;
    boolean terminated;

    public Actions(char computer, boolean terminated) {
        this.computer = computer;
        this.terminated = terminated;
    }

    public Board random(Board bd, char player) {
        Map<Integer, Board> branch = new HashMap<>();
        Random rand = new Random();
        Set<Board> set = exploreActions(bd, player);
        if (set.isEmpty()) {
            System.out.println(player + " LOST");
            return null;
        }
        int i = 0;
        for (Board e : set) {
            branch.put(i, e);
            i++;
        }
        int randomNumber = rand.nextInt(set.size());
        Board ran = branch.get(randomNumber);
        ran.printBoard();
        return ran;
    }

    public Board miniMax(Board bd, char player, int depthLimit) {
        int value = Integer.MIN_VALUE;
        Board branch = null;
        Set<Board> hSet = new HashSet<>();
        hSet = exploreActions(bd, player);
        if (player == 'w') {
            player = 'b';
        } else {
            player = 'w';
        }
        depthLimit--;
        for (Board e : hSet) {
            int min = minValue(e, player, depthLimit);
            value = Math.max(value, min);
            if (value == min) {
                branch = e;
            }
        }
        branch.printBoard();
        return branch;
    }

    public Board alphaBeta(Board bd, char player, int depthLimit){
        Set<Board> hSet = new HashSet<>();
        hSet = exploreActions(bd, player);
        Board branch = null;
        if (player == 'w') {
            player = 'b';
        } else {
            player = 'w';
        }
        depthLimit--;
        int v = Integer.MIN_VALUE;
        for (Board e : hSet) {
            int min = maxValue(bd, player, depthLimit, Integer.MIN_VALUE, Integer.MAX_VALUE);
            v = Math.max(v, min);
            if (v == min) {
                branch = e;
            }
        }
        branch.printBoard();
        return branch;
    }

    public Board hminiMax(Board bd, char player, int cutoff) {
        double value = Double.MIN_NORMAL;
        double min;
        Board branch = null;
        Set<Board> hSet = new HashSet<>();
        hSet = exploreActions(bd, player);
        if (player == 'w') {
            player = 'b';
        } else {
            player = 'w';
        }
        cutoff--;
        for (Board e : hSet) {
            min = hminValue(e, player, cutoff);
            value = Math.max(value, min);
            if (value == min) {
                branch = e;
            }
        }
        branch.printBoard();
        return branch;
    }
    //check if it can do maximum capture
    public Board vicinity(Board bd, Pawn p) {
        Map<Character, Integer> map = Board.hs;
        int x = map.get(p.x);
        int y = p.y;
        if (p.color == 'w' || p.king == true) {
            // top right corner
            if ((y + 2 < bd.boardSize) && (x - 2 >= 0) && bd.boardArr[x - 1][y + 1] != null) {
                if (bd.boardArr[x - 1][y + 1].color != p.color && bd.boardArr[x - 2][y + 2] == null) {
                    if ((x - 2) != 0) {
                        bd.boardArr[x - 2][y + 2] = new Pawn((char) (x - 2), y + 2, false, p.color);
                    } else {
                        bd.boardArr[x - 2][y + 2] = new Pawn('A', y + 2, true, p.color);
                    }
                    bd.boardArr[x][y] = null;
                    bd.boardArr[x - 1][y + 1] = null;
                }
            }
            // top left corner
            if ((y - 2 >= 0) && (x - 2 >= 0) && bd.boardArr[x - 1][y - 1] != null) {
                if (bd.boardArr[x - 1][y - 1].color != p.color && bd.boardArr[x - 2][y - 2] == null) {
                    if ((x - 2) != 0) {
                        bd.boardArr[x - 2][y - 2] = new Pawn((char) (x - 2), y - 2, false, p.color);
                    } else {
                        bd.boardArr[x - 2][y - 2] = new Pawn('A', y - 2, true, p.color);
                    }
                    bd.boardArr[x][y] = null;
                    bd.boardArr[x - 1][y - 1] = null;
                }
            }
        }
        if (p.color == 'b' || p.king == true) {
            // btm right
            if ((x + 2 < bd.boardSize) && (y + 2 < bd.boardSize) && bd.boardArr[x + 1][y + 1] != null) {
                if (bd.boardArr[x + 1][y + 1].color != p.color && bd.boardArr[x + 2][y + 2] == null) {
                    if (((x + 2) != bd.boardSize - 1)) {
                        bd.boardArr[x + 2][y + 2] = new Pawn((char) (x + 2), y + 2, false, p.color);
                    } else {
                        bd.boardArr[x + 2][y + 2] = new Pawn((char) (x + 2), y + 2, true, p.color);
                    }
                    bd.boardArr[x][y] = null;
                    bd.boardArr[x + 1][y + 1] = null;
                }
            }
            // btm left
            if ((x + 2 < bd.boardSize) && (y - 2 >= 0) && bd.boardArr[x + 1][y - 1] != null) {
                if (bd.boardArr[x + 1][y - 1].color != p.color && bd.boardArr[x + 2][y - 2] == null) {
                    if (((x + 2) != bd.boardSize - 1)) {
                        bd.boardArr[x + 2][y - 2] = new Pawn((char) (x + 2), y - 2, false, p.color);
                    } else {
                        bd.boardArr[x + 2][y - 2] = new Pawn((char) (x + 2), y - 2, true, p.color);
                    }
                    bd.boardArr[x][y] = null;
                    bd.boardArr[x + 1][y - 1] = null;
                }
            }
        }
        return bd;
    }

    public Set<Board> legalMove(Board bd, Pawn p1, char color) {
        Set<Board> tempSet = new HashSet<>();
        Map<Character, Integer> map = Board.hs;
        // black pawn and not king
        if ((color == 'b' && p1.king == false) || p1.king == true) {
            Board tempBoard = new Board(bd.boardSize);
            tempBoard.boardArr = deepCopy2D(bd.boardArr);
            // btm right
            if ((p1.y + 1) < bd.boardSize && (map.get(p1.x) + 1) < bd.boardSize) {
                if (tempBoard.boardArr[map.get(p1.x) + 1][p1.y + 1] == null) {
                    if ((map.get(p1.x) + 1) != tempBoard.boardSize - 1) {
                        tempBoard.boardArr[map.get(p1.x) + 1][p1.y + 1] = new Pawn((char) (p1.x + 1), p1.y + 1, false,
                                color);
                    } else {
                        tempBoard.boardArr[map.get(p1.x) + 1][p1.y + 1] = new Pawn((char) (p1.x + 1), p1.y + 1, true,
                                color);
                    }
                    tempBoard.boardArr[map.get(p1.x)][p1.y] = null;
                    tempSet.add(tempBoard);
                }
            }
            // big btm right capture
            if ((p1.y + 2) < bd.boardSize && (map.get(p1.x) + 2 <= bd.boardSize - 1)) {
                if (tempBoard.boardArr[map.get(p1.x) + 2][p1.y + 2] == null
                        && tempBoard.boardArr[map.get(p1.x) + 1][p1.y + 1].color != color) {
                    Pawn temp = tempBoard.boardArr[map.get(p1.x) + 2][p1.y + 2] = new Pawn((char) (p1.x + 2), p1.y + 2,
                            false, color);
                    if ((map.get(p1.x) + 2) == bd.boardSize) {
                        temp.king = true;
                    }
                    tempBoard.boardArr[map.get(p1.x) + 1][p1.y + 1] = null;
                    tempBoard.boardArr[map.get(p1.x)][p1.y] = null;
                    if (bd.boardSize == 8) {
                        //tempBoard = vicinity(tempBoard, temp);
                    }
                    tempSet.add(tempBoard);
                } else if (tempBoard.boardArr[map.get(p1.x) + 2][p1.y + 2] != null
                        && tempBoard.boardArr[map.get(p1.x) + 1][p1.y + 1] != null) {
                    // nothing to be added
                }
            }

            Board tempBoard2 = new Board(bd.boardSize);
            tempBoard2.boardArr = deepCopy2D(bd.boardArr);
            // btm left
            if ((p1.y - 1) >= 0 && (map.get(p1.x) + 1) < bd.boardSize) {
                if (tempBoard2.boardArr[map.get(p1.x) + 1][p1.y - 1] == null) {
                    if ((map.get(p1.x) + 1) != bd.boardSize - 1) {
                        tempBoard2.boardArr[map.get(p1.x) + 1][p1.y - 1] = new Pawn((char) (p1.x + 1), p1.y - 1, false,
                                color);
                    } else {
                        tempBoard2.boardArr[map.get(p1.x) + 1][p1.y - 1] = new Pawn((char) (p1.x + 1), p1.y - 1, true,
                                color);
                    }
                    tempBoard2.boardArr[map.get(p1.x)][p1.y] = null;
                    tempSet.add(tempBoard2);
                }
            } // big btm left
            if ((p1.y - 2) >= 0 && (map.get(p1.x) + 2 <= bd.boardSize - 1)) {
                if (tempBoard2.boardArr[map.get(p1.x) + 2][p1.y - 2] == null
                        && tempBoard2.boardArr[map.get(p1.x) + 1][p1.y - 1].color != color) {
                    Pawn temp = tempBoard2.boardArr[map.get(p1.x) + 2][p1.y - 2] = new Pawn((char) (p1.x + 2), p1.y - 2,
                            false, color);
                    if ((map.get(p1.x) + 2) == tempBoard2.boardSize - 1) {
                        temp.king = true;
                    }
                    tempBoard2.boardArr[map.get(p1.x) + 1][p1.y - 1] = null;
                    tempBoard2.boardArr[map.get(p1.x)][p1.y] = null;
                    if (tempBoard2.boardSize == 8) {
                        //tempBoard2 = vicinity(tempBoard2, temp);
                    }
                    tempSet.add(tempBoard2);
                } else if (tempBoard2.boardArr[map.get(p1.x) + 2][p1.y - 2] != null
                        && tempBoard2.boardArr[map.get(p1.x) + 1][p1.y - 1] != null) {
                    // return nothing
                }
            }
        }

        if ((color == 'w' && p1.king == false) || p1.king == true) {
            Board tempBoard = new Board(bd.boardSize);
            tempBoard.boardArr = deepCopy2D(bd.boardArr);
            // top right
            if ((p1.y + 1) < bd.boardSize && ((map.get(p1.x) - 1) >= 0)) {
                if (tempBoard.boardArr[map.get(p1.x) - 1][p1.y + 1] == null) {
                    if ((map.get(p1.x) - 1) != 0) {
                        tempBoard.boardArr[map.get(p1.x) - 1][p1.y + 1] = new Pawn((char) (p1.x - 1), p1.y + 1, false,
                                color);
                    } else {
                        tempBoard.boardArr[map.get(p1.x) - 1][p1.y + 1] = new Pawn((char) (p1.x - 1), p1.y + 1, true,
                                color);
                    }
                    tempBoard.boardArr[map.get(p1.x)][p1.y] = null;
                    tempSet.add(tempBoard);
                }
            }
            if ((p1.y + 2) < tempBoard.boardSize && (map.get(p1.x) - 2 >= 0)) {
                if (tempBoard.boardArr[map.get(p1.x) - 2][p1.y + 2] == null
                        && tempBoard.boardArr[map.get(p1.x) - 1][p1.y + 1].color != color) {
                    Pawn temp = tempBoard.boardArr[map.get(p1.x) - 2][p1.y + 2] = new Pawn((char) (p1.x - 2), p1.y + 2,
                            false, color);
                    if ((map.get(p1.x) - 2) == 0) {
                        temp.king = true;
                    }
                    tempBoard.boardArr[map.get(p1.x) - 1][p1.y + 1] = null;
                    tempBoard.boardArr[map.get(p1.x)][p1.y] = null;
                    if (tempBoard.boardSize == 8) {
                        //tempBoard = vicinity(tempBoard, temp);
                    }
                    tempSet.add(tempBoard);
                } else if (tempBoard.boardArr[map.get(p1.x) - 2][p1.y + 2] != null
                        && tempBoard.boardArr[map.get(p1.x) - 1][p1.y + 1] != null) {
                    // nothing to be added
                }
            }
            // top left
            Board tempBoard2 = new Board(bd.boardSize);
            tempBoard2.boardArr = deepCopy2D(bd.boardArr);
            if ((p1.y - 1) >= 0 && ((map.get(p1.x) - 1) >= 0)) {
                if (tempBoard2.boardArr[map.get(p1.x) - 1][p1.y - 1] == null) {
                    if ((map.get(p1.x) - 1) != 0) {
                        tempBoard2.boardArr[map.get(p1.x) - 1][p1.y - 1] = new Pawn((char) (p1.x - 1), p1.y - 1, false,
                                color);
                    } else {
                        tempBoard2.boardArr[map.get(p1.x) - 1][p1.y - 1] = new Pawn((char) (p1.x - 1), p1.y - 1, true,
                                color);
                    }
                    tempBoard2.boardArr[map.get(p1.x)][p1.y] = null;
                    tempSet.add(tempBoard2);
                }
            }
            if ((p1.y - 2) >= 0 && (map.get(p1.x) - 2 >= 0)) {
                if (tempBoard2.boardArr[map.get(p1.x) - 2][p1.y - 2] == null
                        && tempBoard2.boardArr[map.get(p1.x) - 1][p1.y - 1].color != color) {
                    Pawn temp = tempBoard2.boardArr[map.get(p1.x) - 2][p1.y - 2] = new Pawn((char) (p1.x - 2), p1.y - 2,
                            false, color);
                    if ((map.get(p1.x) - 2) == 0) {
                        temp.king = true;
                    }
                    tempBoard2.boardArr[map.get(p1.x) - 1][p1.y - 1] = null;
                    tempBoard2.boardArr[map.get(p1.x)][p1.y] = null;
                    if (tempBoard2.boardSize == 8) {
                        //tempBoard2 = vicinity(tempBoard2, temp);
                    }
                    tempSet.add(tempBoard2);
                } else if (tempBoard2.boardArr[map.get(p1.x) - 2][p1.y - 2] != null
                        && tempBoard2.boardArr[map.get(p1.x) - 1][p1.y - 1] != null) {
                    // return nothing
                }
            }
        }
        return tempSet;
    }

    public Set<Board> exploreActions(Board bd, char color) {
        
        Set<Board> set = new HashSet<>();
        for (int i = 0; i < bd.boardSize; i++) {
            for (int j = 0; j < bd.boardSize; j++) {
                if (bd.boardArr[i][j] == null) {
                    continue;
                } else if (bd.boardArr[i][j].color == color) {
                    set.addAll(legalMove(bd, bd.boardArr[i][j], color));
                }
            }
        }
        if (set.contains(bd)) {
            set.remove(bd);
        }
        return set;
    }
    //for regular miniMax
    public int maxValue(Board bd, char player, int depthLimit) {
        Set<Board> hSet = new HashSet<>();
        hSet = exploreActions(bd, player);
        if (depthLimit == 0){
            return (int)heuristics(bd);
        }
        if (bd.isTerminal(bd) || hSet.isEmpty()) {
            return utility(bd);
        }
        depthLimit--;
        int v = Integer.MIN_VALUE;
        if (player == 'w') {
            player = 'b';
        } else {
            player = 'w';
        }
        for (Board a : hSet) {
            v = Math.max(v, minValue(a, player, depthLimit));
        }
        return v;
    }

    public int minValue(Board bd, char player, int depthLimit) {
        Set<Board> hSet = new HashSet<>();
        hSet = exploreActions(bd, player);
        if (depthLimit == 0){
            return (int)heuristics(bd);
        }
        if (bd.isTerminal(bd) || hSet.isEmpty()) {
            return utility(bd);
        }
        depthLimit--;
        int v = Integer.MAX_VALUE;
        if (player == 'w') {
            player = 'b';
        } else {
            player = 'w';
        }
        for (Board a : hSet) {
            v = Math.min(v, maxValue(a, player, depthLimit));
        }
        return v;
    }

    public double hmaxValue(Board bd, char player, int cutoff) {
        Set<Board> hSet = new HashSet<>();
        hSet = exploreActions(bd, player);
        if (cutoff == 0) {
            return heuristics(bd);
        } else if (bd.isTerminal(bd) || hSet.isEmpty()) {
            return utility(bd);
        }
        cutoff--;
        double v = Double.MIN_VALUE;
        if (player == 'w') {
            player = 'b';
        } else {
            player = 'w';
        }
        for (Board a : hSet) {
            v = Math.max(v, hminValue(a, player, cutoff));
        }
        return v;
    }
    // just for h-minimax
    public double hminValue(Board bd, char player, int cutoff) {
        Set<Board> hSet = new HashSet<>();
        hSet = exploreActions(bd, player);
        //System.out.println("in min");
        if (cutoff == 0) {
            return heuristics(bd);
        } else if (bd.isTerminal(bd) || hSet.isEmpty()) {
            return utility(bd);
        }
        cutoff--;
        double v = Double.MAX_VALUE;
        if (player == 'w') {
            player = 'b';
        } else {
            player = 'w';
        }
        for (Board a : hSet) {
            v = Math.min(v, hmaxValue(a, player, cutoff));
        }
        return v;
    }
    //for pruning
    public int maxValue(Board bd, char player, int depthLimit, int alpha, int beta){
        if(depthLimit == 0){
            return (int)heuristics(bd);
        }
        if(bd.isTerminal(bd)){
            return utility(bd);
        }
        int v = Integer.MIN_VALUE;
        Set<Board> hSet = new HashSet<>();
        hSet = exploreActions(bd, player);
        if (player == 'w') {
            player = 'b';
        } else {
            player = 'w';
        }
        depthLimit--;
        for(Board a : hSet){
            v = Math.max(v, minValue(a, player,depthLimit, alpha, beta));
            if(v >= beta){
                return v;
            }
            alpha = Math.max(alpha, v);
        }
        return v;
    }   
    
    public int minValue(Board bd, char player, int depthLimit, int alpha, int beta){
        if(depthLimit == 0){
            return (int)heuristics(bd);
        }
        if(bd.isTerminal(bd)){
            return utility(bd);
        }
        int v = Integer.MAX_VALUE;
        Set<Board> hSet = new HashSet<>();
        hSet = exploreActions(bd, player);
        if (player == 'w') {
            player = 'b';
        } else {
            player = 'w';
        }
        depthLimit--;
        for(Board a : hSet){
            v = Math.min(v, maxValue(a, player,depthLimit, alpha, beta));
            if(v <= alpha){
                return v;
            }
            beta = Math.min(beta, v);
        }
        return v;
    }

    public double heuristics(Board bd) {
        double estimate = 0.0;
        double black = bd.count(bd)[0];
        double white = bd.count(bd)[1];
        double blackKing = bd.count(bd)[2];
        double whiteKing = bd.count(bd)[3];
        
        double percent = black / (black + white);
        double percentage = 0.0;
        if (blackKing + whiteKing != 0) {
            percentage = blackKing / (blackKing + whiteKing);
        }

        if (computer == 'b') {
            estimate = percent + percentage;
        } else {
            estimate = -(percent + percentage);
        }
        return estimate;
    }

    public int utility(Board bd) {
        for (int i = 0; i < bd.boardSize; i++) {
            for (int j = 0; j < bd.boardSize; j++) {
                if (bd.boardArr[i][j] == null) {
                    continue;
                } else {
                    if (bd.boardArr[i][j].color == computer) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        }
        return 0;
    }

    public static Pawn[][] deepCopy2D(Pawn[][] array) {
        Pawn[][] temp = new Pawn[array.length][];
        for (int i = 0; i < array.length; i++) {
            temp[i] = new Pawn[array[i].length];
            System.arraycopy(array[i], 0, temp[i], 0, temp[i].length);
        }
        return temp;
    }
}