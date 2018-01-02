import controller.Controller;
import common.GameDTO;

import java.util.Scanner;

public class JavaTester {
    public static void main(String[] args) {
        Controller controller = new Controller();

        GameDTO game = controller.game();
        System.out.println(game.getWord());

        Scanner sc = new Scanner(System.in);
        System.out.println(game.guessWord(sc.next()));
        System.out.println("Score: " + game.getScore());
    }
}
