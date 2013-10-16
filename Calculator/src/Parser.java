public class Parser {

	public static int DO(String e) throws Exception{
		int result;
		Token t = new Token(); 
		t.start=0;
		lex(e, t);
		if (t.type==-1 || t.type==5)
			throw new Exception("Wrong Arithmatic Expression");
		if (t.type==4){
			pushOpd(Integer.parseInt(e.substring(t.start, t.end+1)));
		}
		t.start=t.end+1;
		lex(e, t);
		if (t.type==-1)
			throw new Exception();
		if (t.type==5){
			return popOpd();
		}
		pushOpr(t.type, t.precedence);
		t.start=t.end+1;
		lex(e, t);
		while (!emptyOpr() && t.type!=-1){
			if (t.type==4){
				pushOpd(Integer.parseInt(e.substring(t.start, t.end+1)));
			}
			else if (t.precedence<=topPrecedence()){
				while (!emptyOpr() && t.precedence<=topPrecedence()){
					int result1=0;
					int op = popOpr(); popPrecedence();
					int op2 = popOpd();
					int op1 = popOpd();
					switch (op){
					case 0:
						result1 = op1+op2;
						break;
					case 1:
						result1 = op1-op2;
						break;
					case 2:
						result1 = op1*op2;
						break;
					case 3:
						result1 = op1/op2;
						break;
					}
					pushOpd(result1);
				}
				if (t.type==5){
					continue;
				}
				pushOpr(t.type, t.precedence);
			}
			else {
				pushOpr(t.type, t.precedence);
			}
			t.start=t.end+1;
			lex(e, t);
		}
		result = popOpd();
		return result;
	}
	
	private static class Token {
		String e;
		public int start, end; // inclusive
		public int type; // -1: error 0: + 1: - 2: * 3: / 4: int 5: end of e
		public int precedence; // 0: end; 1: +, - 2: * /  
	}
	private static void lex(String e, Token t){
		if (t.start == e.length()){
			t.end = t.start;
			t.type = 5;
			t.precedence = 0;
			return;			
		}
		if (e.charAt(t.start)=='+'){
			t.end = t.start;
			t.type = 0;
			t.precedence = 1;
			return;
		}
		if (e.charAt(t.start)=='-'){
			t.end = t.start;
			t.type = 1;
			t.precedence = 1;
			return;
		}
		if (e.charAt(t.start)=='*'){
			t.end = t.start;
			t.type = 2;
			t.precedence = 2;
			return;
		}
		if (e.charAt(t.start)=='/'){
			t.end = t.start;
			t.type = 3;
			t.precedence = 2;
			return;
		}
		
		t.type=-1;
		int i=t.start;
		if (Character.isDigit(e.charAt(i))){
			while (i<e.length() && Character.isDigit(e.charAt(i))){
				++i;
			}
			if (i==e.length() || !Character.isDigit(e.charAt(i))){
				t.type = 4;
				t.end=i-1;
			}
		}
	}
	
	static boolean emptyOpr(){
		return operatorTop==-1;
	}
	static void pushOpr(int operator, int precedence){
		++operatorTop;
		operatorStack[operatorTop] = operator;
		precedenceStack[operatorTop] = precedence;
	}
	static int popOpr(){
		--operatorTop;
		return operatorStack[operatorTop+1];
	}
	static int popPrecedence(){
		return precedenceStack[operatorTop+1];
	}
	static int topOpr(){
		return operatorStack[operatorTop];
	}
	static int topPrecedence(){
		return precedenceStack[operatorTop];
	}
	static void pushOpd(int operand){
		++operandTop;
		operandStack[operandTop] = operand;
	}
	static int popOpd(){
		--operandTop;
		return operandStack[operandTop+1];
	}
	static int topOpd(){
		return operandStack[operandTop];
	}
	private static int operatorStack[] = new int[1000];
	private static int precedenceStack[] = new int[1000];
	private static int operatorTop = -1;
	private static int operandStack[] = new int[1001];
	private static int operandTop = -1;
}