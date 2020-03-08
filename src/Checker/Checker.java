package Checker;

import java.util.Scanner;

public class Checker {

    public static void main(String[] args) throws Exception {
        System.out.println("Checker by Jiahao Lu");
        String intro = "Choose your game:\n" + 
                      "1. Small 4 x 4 Checkers\n" + 
                      "2. Standard 8 x 8 Checkers\n" + 
                      "Your choice? ";
        System.out.print(intro);
        Scanner scan = new Scanner(System.in);
        int choiceForIntro = scan.nextInt();
        // if(choiceForIntro == 1){
        //      Board bd = new Board(4); bd.setBoard(); bd.printBoard();}
        // else{Board bd = new Board(8); bd.setBoard(); bd.printBoard();}
        String options = "Choose Your Opponent:\n" + 
                         "1. An agent that plays randomly\n" + 
                         "2. An agent that uses MINIMAX\n" + 
                         "3. An agent that uses MINIMAX with alpha-beta pruning\n" + 
                         "4. An agent that uses H-MINIMAX with a fixed depth cutoff\n" + 
                         "Your choice? ";
        System.out.print(options);
        int choiceForOptions = scan.nextInt();
        int depthLimit;
        if(choiceForOptions == 3 || choiceForOptions == 4){
            System.out.print("Depth Limit? ");
            depthLimit = scan.nextInt();
        }else{depthLimit = Integer.MIN_VALUE;}  
        System.out.print("Do you want to play BLACK (b) or WHITE (w)? ");
        char choiceForColor = scan.next().charAt(0);
        if(choiceForColor == 'b'){
            System.out.println();
        }
        Gameplay gp = new Gameplay(choiceForIntro, choiceForOptions, depthLimit, choiceForColor);
        gp.startGame();
        scan.close();
    }
}
