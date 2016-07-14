package algebraicexpression;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import org.junit.Test;

import exceptions.InvalidExpressionException;

public class AlgebraicExperssionComputerTest {
	
	final String[] TEST_EXPRESSIONS = {"((a+b)*(b^c+(d-e)/a))^(c+d/e)",//4
            "a+b",//2
            "a-b",//1
            "a*b",//1
            "a/b",//1
            "a^b",//1
            "a+b^c",//2
            "a^b+c",//2
            "(a+b)^(c+d)",//4
            "a+b*(b+c)",//3
            "a+b/(b-c)"//3
			};
	
	final String[] TEST_INVALID_EXPRESSIONS = {null,
            "a+",//missing variables
            "ab",//missing operataions
            "a*b+(c-a",//parenthesis match
            "a%b",//wrong characters
            "a/(b-b)"//zero denominator
			};
	
	final int[] CORRECT_TEST_VALUES = {4, 2, 1, 1, 1, 1, 2, 2, 4, 3, 2};
	final String[] CORRECT_TEST_VARIABLES = {"abcde", "ab", "ab", "ab", "ab", "ab", "abc", "abc", "abcd", "abc", "abc"}; 

	@Test
	public void testVariables() {
		int count = TEST_EXPRESSIONS.length;
		for (int i=0; i<count; i++) {
			String expression = TEST_EXPRESSIONS[i];
			AlgebraicExperssionComputer test;
			try {
				test = new AlgebraicExperssionComputer(expression);
				System.out.println("testVariables: ");
				System.out.println(test.getVariables());
				if(!isVariablesCorrect(test.getVariables(), CORRECT_TEST_VARIABLES[i])){
					fail("Expression: "+ TEST_EXPRESSIONS[i] +" failed to collect correct variables.\n"
							+ "Received test.getVariables(), should have got "+ CORRECT_TEST_VARIABLES[i]);
				}
			} catch (InvalidExpressionException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	@Test
	public void testInvalidExpressions() {
		int count = TEST_INVALID_EXPRESSIONS.length;
		AlgebraicExperssionComputer test = new AlgebraicExperssionComputer();
		for (int i=0; i<count; i++) {
			String expression = TEST_INVALID_EXPRESSIONS[i];
			try {
				System.out.println("testInvalidExpressions: "+ expression);
				test.setExpression(expression);
				test.maxResult();
				fail();
			} catch (InvalidExpressionException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	@Test
	public void testComputation() {
		int count = TEST_EXPRESSIONS.length;
		for (int i=0; i<count; i++) {
			String expression = TEST_EXPRESSIONS[i];
			AlgebraicExperssionComputer test;
			try {
				test = new AlgebraicExperssionComputer(expression);
				System.out.println("testComputation: ");
				double result = test.maxResult();
				System.out.println("expression: "+ expression);
				System.out.println("maxResult: "+ result);
			} catch (InvalidExpressionException e) {
				System.out.println(e.getMessage());
			}
			
		}
	}
	
	private boolean isVariablesCorrect(char[] receivedTestVariables, String correctTestVariables){
		if(receivedTestVariables.length != correctTestVariables.length()){
			return false;
		}
		for (char c : receivedTestVariables) {
			if(correctTestVariables.indexOf(c) < 0){
				return false;
			}
		}
		return true;
	}

}
