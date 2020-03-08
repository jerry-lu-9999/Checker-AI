package Checker;

import java.util.Scanner;

public class Gameplay {

    int choiceForIntro;
    int choiceForOptions;
    char choiceForColor;
    int depthLimit;
    Board bd;

	public Gameplay(int choiceForIntro, int choiceForOptions, int depthLimit, char choiceForColor) {
        this.choiceForColor = choiceForColor;
        this.choiceForIntro = choiceForIntro;
        this.choiceForOptions = choiceForOptions;
        this.depthLimit = depthLimit;
    }
    
    public String[] parser(String move){
        String[] arr;
        String start = move.substring(0, 2);
        //simply just moving
        if(move.length() == 5){
            String target = move.substring(3, 5);
            arr = new String[2]; 
            arr[0] = start; arr[1] = target;
        }else{
            String middle = move.substring(3, 5);
            String target = move.substring(6, 8);
            arr = new String[3];
            arr[0] = start; arr[1] =  middle; arr[2] = target;
        }
        return arr;
    }

    public Board humanMove(Board bd){
        System.out.print("Your move (Don't ? for help):\n");
        Scanner scan = new Scanner(System.in);
        String move = scan.nextLine();
        if(move.equals("?")){
            System.out.println("DUDEEEEE I SAID DON'T ? FOR HELP");
        }
        String[]temp = parser(move);
        if(temp.length == 2){
            bd.update(temp[0], temp[1]);
        }else{
            bd.update(temp[0], temp[1]);
            bd.update(temp[1], temp[2]);
        }
        bd.printBoard();
        if(choiceForColor == 'b'){System.out.println("Next to play: White");}
   else if(choiceForColor == 'w'){System.out.println("Next to play: BLACK");}
        return bd;
    }

    public void startGame(){
        char turn;
        Actions ac;
        int ply = 10;
        if(choiceForIntro == 1){
            bd = new Board(4);
        }else{
            bd = new Board(8);
        }
        bd.setBoard();
        bd.printBoard();
        if(choiceForColor == 'b'){   
            ac = new Actions('w', false);
            while(ac.terminated == false){
                turn = 'b';
                Board temp = humanMove(bd);
                turn = 'w';
                Board temp1 = null;
                if(choiceForOptions == 1){
                    temp1 = ac.random(temp, 'w');
                }else if(choiceForOptions == 2){
                    temp1 = ac.miniMax(temp,'w', ply);
                }else if(choiceForOptions == 3){
                    temp1 = ac.alphaBeta(temp, 'w', depthLimit);
                }else if(choiceForOptions == 4){
                    temp1 = ac.hminiMax(temp, 'w', depthLimit);
                }
                
                bd = temp1; turn = 'b';
                if(temp1 == null || bd.isTerminal(temp1)){
                    ac.terminated = true;
                }
                if(ac.exploreActions(bd, turn).isEmpty()){
                    ac.terminated = true;
                    System.out.println(turn + " LOST");
                }
                System.out.println("Next to play: " + choiceForColor);
            }
        }else{
            ac = new Actions('b', false);
            while(ac.terminated == false){
                turn = 'b'; Board temp = null;
                if(choiceForOptions == 1){
                    temp = ac.random(bd, 'b');
                }else if(choiceForOptions == 2){
                    temp = ac.miniMax(bd, 'b', ply);
                }else if(choiceForOptions == 3){
                    temp = ac.alphaBeta(bd, 'b', depthLimit);
                }else if(choiceForOptions == 4){
                    temp = ac.hminiMax(bd, 'b', depthLimit);
                }
                turn = 'w';
                Board temp1 = humanMove(temp);
                bd = temp1; turn = 'b';
                if(temp1 == null ||bd.isTerminal(temp1)){
                    ac.terminated = true;
                }
                if(ac.exploreActions(bd, turn).isEmpty()){
                    ac.terminated = true;
                    System.out.println(turn  + " LOST");
                }
                System.out.println("Next to play: " + choiceForColor);
            } 
        }
    }
}