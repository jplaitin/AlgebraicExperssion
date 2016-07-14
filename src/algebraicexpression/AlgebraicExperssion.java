package algebraicexpression;

import java.util.Scanner;

import exceptions.InvalidExpressionException;

public class AlgebraicExperssion {

	public static void main(String[] args) {
		System.out.print("This is Algebraich Expression calculator");
		printHelp();
		Scanner scanner = new Scanner(System.in);
		AlgebraicExperssionComputer expressionComputer = new AlgebraicExperssionComputer();
		
	    //  prompt for the user's name
		System.out.println("Write expression: ");
	    // get their input as a String
		String expression = scanner.next();
	    while(!expression.equals("q")){
			try {
				expressionComputer.setExpression(expression);
				double result;
			
				result = expressionComputer.maxResult();
				System.out.println(String.format("MaxResult is "+ result));
			} catch (InvalidExpressionException e) {
				System.out.println(e.getMessage());
			}
			System.out.println("Write expression: ");
			expression = scanner.next();
	    }
	    System.out.println("Thank you and good bye!");
	}
	
	private static void printHelp(){
		System.out.println("Write expression, press enter and application will calculate for you max value for that expression when all variables range from [0, 1]");
		System.out.println("Write expression using ");
		System.out.println("Opererational characters: +, -, *, / and ^");
		System.out.println("Variables are single char from a to z");
		System.out.println("You can use parenthesis in you're expressions");
		System.out.println("Example");
		System.out.println("a*(b+a)");
		System.out.println("Write q to quit application");
		
	}

}
