package algebraicexpression;

import java.util.HashMap;
import java.util.Map;

import exceptions.InvalidExpressionException;
import exceptions.ZeroDenominatorException;

public class AlgebraicExperssionComputer {
	
	private char[] variables;
	private String expression;
	
	public AlgebraicExperssionComputer(){
	}
	
	public AlgebraicExperssionComputer(String expression) throws InvalidExpressionException{
		setExpression(expression);	
	}
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) throws InvalidExpressionException {
		if(expression == null)
			 throw new InvalidExpressionException("No expression found");
		String clearedExpression = expression.replaceAll("\\s+","");
		if(!AlgebraicExperssionParser.areExpressionsCharsValid(clearedExpression)){
			 throw new InvalidExpressionException("Expression contains invalid characters");
		}
		if(!AlgebraicExperssionParser.isParenthesisMatch(clearedExpression)){
			 throw new InvalidExpressionException("Expression parenthesis aren't in correct format");
		}
		this.expression = clearedExpression;
		setVariables(AlgebraicExperssionParser.collectVariables(clearedExpression));
		if(variables == null)
			 throw new InvalidExpressionException("Expression doesn't contain any variables");
		else if(variables.length < 2){
			throw new InvalidExpressionException("Expression doesn't contain enough variables");
		}
	}

	public double maxResult() throws InvalidExpressionException{
		double maxResult = -Double.MAX_VALUE;
		Map<String, Object> vars = new HashMap<String, Object>();
		for (char c : variables) {
			vars.put(String.valueOf(c), 1);
		}
		
		for (int i = 0; i < Math.pow(2, variables.length); i++) { //all possible variants 
            String bin = Integer.toBinaryString(i); 
            while (bin.length() < variables.length)
                bin = "0" + bin;
            char[] chars = bin.toCharArray();
            for (int j = 0; j < chars.length; j++) {
            	vars.put(String.valueOf(variables[j]), Character.getNumericValue(chars[j]));
            } 
            double result;
			try {
				result = AlgebraicExperssionParser.eval(expression, vars);
				if(Double.isFinite(result) && result > maxResult){
					maxResult = result;
				}
			} catch (ZeroDenominatorException e) {
				//System.out.println("Mid ZeroDenominatorException");
			}				
		}
		if(maxResult == -Double.MAX_VALUE)
			throw new InvalidExpressionException("Denominator is 0 for all possible variants");
		return maxResult;
	}
	
	public char[] getVariables() {
		return variables;
	}
	public void setVariables(char[] variables) {
		this.variables = variables;
	}

	
}
