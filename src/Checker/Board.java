package Checker;

import java.util.*;

public class Board {
    
    public int boardSize;
    static Map<Character, Integer> hs = new HashMap<>();

    public Board(int size){
        this.boardSize = size; 
    }

    Pawn[][]boardArr;
 
    public void setBoard(){
        boardArr = new Pawn[boardSize][boardSize];
        boardArr[0][0] = null;
        boardArr[0][1] = new Pawn('A', 1, false, 'b');
        boardArr[0][2] = null;
        boardArr[0][3] = new Pawn('A', 3, false, 'b');
        
        if(boardSize == 4){
            boardArr[3][0] = new Pawn('D', 0, false, 'w');
            boardArr[3][1] = null;
            boardArr[3][2] = new Pawn('D', 2, false, 'w');
            boardArr[3][3] = null;
            for(int i = 1 ; i < 3; i++){
                for(int j = 0; j < 4; j++){
                    boardArr[i][j] = null;
                }
            }
        }else if(boardSize == 8){
            boardArr[0][5] = new Pawn('A', 5, false, 'b');
            boardArr[0][7] = new Pawn('A', 7, false, 'b');
            boardArr[1][0] = new Pawn('B', 0, false, 'b');
            boardArr[1][2] = new Pawn('B', 2, false, 'b');
            boardArr[1][4] = new Pawn('B', 4, false, 'b');
            boardArr[1][6] = new Pawn('B', 6, false, 'b');
            boardArr[2][1] = new Pawn('C', 1, false, 'b');
            boardArr[2][3] = new Pawn('C', 3, false, 'b');
            boardArr[2][5] = new Pawn('C', 5, false, 'b');
            boardArr[2][7] = new Pawn('C', 7, false, 'b');

            boardArr[5][0] = new Pawn('F', 0, false, 'w');
            boardArr[5][2] = new Pawn('F', 2, false, 'w');
            boardArr[5][4] = new Pawn('F', 4, false, 'w');
            boardArr[5][6] = new Pawn('F', 6, false, 'w');
            boardArr[6][1] = new Pawn('G', 1, false, 'w');
            boardArr[6][3] = new Pawn('G', 3, false, 'w');
            boardArr[6][5] = new Pawn('G', 5, false, 'w');
            boardArr[6][7] = new Pawn('G', 7, false, 'w');
            boardArr[7][0] = new Pawn('H', 0, false, 'w');
            boardArr[7][2] = new Pawn('H', 2, false, 'w');
            boardArr[7][4] = new Pawn('H', 4, false, 'w');
            boardArr[7][6] = new Pawn('H', 6, false, 'w');
        }
        hs.put('A', 0); hs.put('B', 1); hs.put('C', 2); hs.put('D', 3); 
        hs.put('E', 4); hs.put('F', 5); hs.put('G', 6); hs.put('H', 7);
    }
    public void printBoard() {
        if(boardSize == 4){
            String line = "  1 2 3 4 \n" + 
                          " +-+-+-+-+";
            System.out.println(line);
            for(int i = 0, alpha = 'A'; i < boardSize; i++, alpha++){
                System.out.print((char)alpha + "|");
                for(int j = 0; j < boardSize; j++){
                    if(boardArr[i][j] == null){
                        System.out.print(" |");
                    }else{
                        System.out.print(boardArr[i][j].color + "|");   
                    }
                }
                System.out.println("\n +-+-+-+-+");
            }
        }else if(boardSize == 8){
            String line = "  1 2 3 4 5 6 7 8 \n" + " +-+-+-+-+-+-+-+-+";
            System.out.println(line);
            for (int i = 0, alpha = 'A'; i < boardSize; i++, alpha++) {
                System.out.print((char) alpha + "|");
                for (int j = 0; j < boardSize; j++) {
                    if (boardArr[i][j] == null) {
                        System.out.print(" |");
                    } else {
                        System.out.print(boardArr[i][j].color + "|");
                    }
                }
                System.out.println("\n +-+-+-+-+-+-+-+-+");
            }
        }
    }

	public void update(String start, String target) {
        int startRow = hs.get(start.charAt(0));
        int startColumn = Character.getNumericValue(start.charAt(1))-1;

        int targetRow = hs.get(target.charAt(0));
        int targetColumn = Character.getNumericValue(target.charAt(1))-1;

        boardArr[targetRow][targetColumn] = new Pawn(target.charAt(0),targetColumn,false, 
                boardArr[startRow][startColumn].color);
        if(targetRow == boardSize-1 || targetRow == 0){
            boardArr[targetRow][targetColumn].king = true;
        }
        boardArr[startRow][startColumn] = null;
        //single capture case
        if(Math.abs(targetColumn-startColumn) == 2){
            System.out.println("Single capture case");
            boardArr[(startRow + targetRow)/2][(startColumn+targetColumn)/2] = null;
        }
    }

    public int[] count(Board bd){
        int[] number = new int[4];
        int black = 0; int white = 0;
        int blackKing = 0; int whiteKing = 0;
        for(int i = 0; i < bd.boardArr.length; i++){
            for(int j = 0; j < bd.boardArr[i].length; j++){
                if(bd.boardArr[i][j] == null) continue;
                else{
                    if(bd.boardArr[i][j].color == 'b'){
                        black++;
                        if(bd.boardArr[i][j].king == true){
                            blackKing++;
                        }
                    }else if(bd.boardArr[i][j].color == 'w'){
                        white++;
                        if(bd.boardArr[i][j].king == true){
                            whiteKing++;
                        }
                    }
                    
                }
            }
        }
        number[0] = black; number[1] = white; number[2] = blackKing; number[3] = whiteKing;
        return number;
    }
    public boolean isTerminal(Board bd){
        if(bd == null){
            return true;
        }
        int[]temp = count(bd);
        if(temp[0] == 0 || temp[1] == 0){
            return true;
        }
        return false;
    }
}
