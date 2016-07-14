package algebraicexpression;

import java.util.Map;
import java.util.Stack;

import exceptions.InvalidExpressionException;
import exceptions.ZeroDenominatorException;

public class AlgebraicExperssionParser {
	private static final String VARIABLES = "abcdefghijklmnopqrstuvxyz";
	public static final String OPERATIONS = "+-*/^";
	private static final String PARENTHESIS = "()";
	//final String VALID_CHARS = PARAMETERS+PARENTHESIS+OPERATIONS+" "; //added empty space there since it's easy to filter away
	
	private static final String VALID_PATERN = "[a-z\\+\\-\\*/\\^\\(\\)]+";
	
	
	/**
	 * Checks that expression string contains only needed characters.
	 * Expression can still be invalid if there are more than one alphapetical
	 * character in a row or if parenthesis are opened but not closed etc.
	 * @param expression
	 * @return
	 */
	public static boolean areExpressionsCharsValid(String expression){
		return expression.matches(VALID_PATERN);
	}
	
	public static boolean isParenthesisMatch(String expression) {
	    Stack<Character> stack = new Stack<Character>();

	    char c;
	    for(int i=0; i < expression.length(); i++) {
	        c = expression.charAt(i);

	        if(c == '(')
	            stack.push(c);
	        else if(c == ')')
	            if(stack.empty())
	                return false;
	            else
	                stack.pop();
	    }
	    return stack.empty();
	}
	
	public static char[] collectVariables(String expression){
		Stack<Character> variablesObj = new Stack<Character>();
		
		int length = expression.length();
		for(int i=0; i<length; i++) {
			Character c = new Character(expression.charAt(i));
			if(VARIABLES.indexOf(c)>=0 && !variablesObj.contains(c)){
				variablesObj.push(c);
			}
		}
		
		char[] variables = new char[variablesObj.size()];
		int i=0;
		while(!variablesObj.isEmpty()){
			variables[i] = variablesObj.pop();
			i++;
		}
		return variables;
	}
	
	/**
	 * Uses recursion to compute expression in correct order. Separates + and - calculations 
	 * as terms. *, /, ^ are separated as factors and sub expressions as an expression. Giving 'priority'
	 * to factors and expressions (sub expressions) 
	 * 
	 * Thanks to Boan
	 * http://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form
	 * used Boans Answer as basis. This is nearly identical to he's solution just did some tinkering
	 * 
	 * Helpful link about this subject
	 * https://theantlrguy.atlassian.net/wiki/display/ANTLR3/Five+minute+introduction+to+ANTLR+3
	 * They also have library that you can use for this. 
	 * 
	 * I used boans answer to keeping this simple. Original idea was to create an operation tree and analyze 
	 * it to create min max values for sub expressions and perhaps even further to analyze which parts
	 * affect negatively to results so I could minimize them not to return actual value like it now does.
	 * 
	 * @param str
	 * @param vars
	 * @return
	 * @throws ZeroDenominatorException 
	 * @throws Exception 
	 */
	public static double eval(final String str, final Map<String, Object> vars) throws InvalidExpressionException, ZeroDenominatorException {
		return new Object() {
	        int pos = -1, ch;

	        void nextChar() {
	            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
	        }

	        boolean eat(int charToEat) {
	            if (ch == charToEat) {
	                nextChar();
	                return true;
	            }
	            return false;
	        }

	        double parse() throws InvalidExpressionException, ZeroDenominatorException {
	            nextChar();
	            double x = parseExpression();
	            if (pos < str.length()) throw new InvalidExpressionException("Unexpected: " + (char)ch);
	            return x;
	        }
	        
	        // Grammar:
	        // expression = term | expression `+` term | expression `-` term
	        // term = factor | term `*` factor | term `/` factor
	        // factor = `+` factor | `-` factor | `(` expression `)`
	        //        | number | functionName factor | factor `^` factor

	        double parseExpression() throws InvalidExpressionException, ZeroDenominatorException {
	        	double x = parseTerm();
	            while (true) {
	                if      (eat('+')) x += parseTerm(); // addition
	                else if (eat('-')) x -= parseTerm(); // subtraction
	                else return x;
	            }
	        }

	        double parseTerm() throws InvalidExpressionException, ZeroDenominatorException {
	            double x = parseFactor();
	            while (true) {
	                if      (eat('*')) x *= parseFactor(); // multiplication
	                else if (eat('/')){
	                	double denominator = parseFactor();
	                	if(denominator == 0){
	                		throw new ZeroDenominatorException("Denominator is 0");
	                	}
	                	else{
	                		x /= denominator; // division
	                	}
	                }
	                else return x;
	            }
	        }

	        double parseFactor() throws InvalidExpressionException, ZeroDenominatorException {
	        	if (eat('+')) return parseFactor(); // unary plus
	            if (eat('-')) return -parseFactor(); // unary minus

	        	double x;
	            if (eat('(')) { // parentheses
	            	x = parseExpression();
	                eat(')');
	            } else if (ch >= 'a' && ch <= 'z') { // variables
	            	String key = String.valueOf((char)ch);
	            	x = ((Integer)vars.get(key)).intValue();
	            	nextChar();
	            } else {
	                throw new InvalidExpressionException("Unexpected: " + (char)ch);
	            }

	            if (eat('^')){
	            	x = Math.pow(x, parseFactor()); // exponentiation
	            }
	            return x;
	        }	        
	    }.parse();
	    
	}
}
