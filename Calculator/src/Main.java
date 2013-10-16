import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

	/**
	 * @param args
	 */
	private static boolean op1Set = false;
	private static int operand1 = 0;
	private static int op = -1;             // (-1 = no operation)  --  (0 = +)  --  (1 = -)  --  (2 = *)  --  (3 = /)
	private static boolean calc = false;
	public static String currentString = "";
	public static String currentNumber = "";
	public static String currentAnswer = "";
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("The Calculator!");
		//frame.setBackground(Color.GRAY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Top Display
		
		JPanel contentPane = (JPanel)frame.getContentPane();
		final JLabel display = new JLabel();//("",JLabel.RIGHT);
		display.setFont(new Font("Helvetica", Font.PLAIN, 22));
		display.setHorizontalAlignment(JLabel.RIGHT);
		display.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		display.setPreferredSize(new Dimension(100, 50));
		contentPane.add(display, BorderLayout.NORTH); 

		// Keyboard
		
		JPanel keyboard = new JPanel();
		keyboard.setLayout(new GridLayout(4, 0));
		contentPane.add(keyboard, BorderLayout.CENTER);
		
		// Status bar for the bottom of the calculator
		
		JPanel statusPane = (JPanel)frame.getContentPane();
		final JLabel statusBar = new JLabel();
		statusBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		statusBar.setPreferredSize(new Dimension(100, 20));
		statusPane.add(statusBar, BorderLayout.SOUTH);
		
		
		
		/////////////////////////////////////////////////////////////////
		
		ActionListener listenerNumeric = new ActionListener(){

			public void actionPerformed(ActionEvent numberPushed) {
				JButton b = (JButton)numberPushed.getSource();
				
				if (calc) {
					calc = false;
					display.setText("");					
				}
				if (op != -1 && !op1Set) {
					operand1 = Integer.parseInt(display.getText());
					op1Set=true;
					display.setText("");
				}
				
				String text;
				
				text = display.getText()+b.getText();
				display.setText("");
				
				// Prevents user from entering a preceding zero
				
				@SuppressWarnings("unused")
				int len;
	
				if (text.startsWith("0") && (len = text.length()) > 1) {
					text = text.substring(1);
				}
				
				// Prevents entering to large an integer 
				
				long x = Integer.parseInt(text);
				long max = 214748367;
				
				if (x > max) {
					statusBar.setText("You've entered too large a number!");
					display.setText("");
					operand1 = Integer.parseInt(display.getText());
					op1Set=true;
					
					text = display.getText()+b.getText();
					
					Exception myException = new ArithmeticException("You've entered too large a number!");
				    try {
						throw myException;						
					} catch (Exception e1) {
						text = null;
					}
				}  				
				
				// Prevents the users from dividing by zero 
				
				while (op == 3 && text.equals("0")) {
					statusBar.setText("Can't divide by zero...please enter a differnt divisor");
					text = null;
					operand1 = Integer.parseInt(display.getText());
				}		
				
				
				currentNumber = text;
				currentString = currentString + text;
				
				display.setText(currentString);		
				statusBar.setText("");
			}
		};
			
			
			
		/////////////////////////////////////////////////////////////////
			
		ActionListener listenerOperation = new ActionListener(){
				public void actionPerformed(ActionEvent operationPushed) {
					JButton b = (JButton)operationPushed.getSource();
									
					if (op != -1 && op1Set){
						int result = 0;
						switch (op) {
							case 0: // +
								result = Integer.parseInt(currentNumber)+Integer.parseInt(display.getText());
								break;
							case 1: // -
								result = Integer.parseInt(currentNumber)-Integer.parseInt(display.getText());
								break;
							case 2: // *
								result = Integer.parseInt(currentNumber)*Integer.parseInt(display.getText());
								break;
							case 3: // /
								int divisor = Integer.parseInt(display.getText());
								result = Integer.parseInt(currentNumber)/divisor;
								break;
							}
						op = -1;
						op1Set = false;
						display.setText(Integer.toString(result));
					}
					if (op == -1 && !calc){
						if (b.getText().equals("+")){
							op = 0;
							display.setText(currentString = currentString + " + ");
							
						}
						if (b.getText().equals("-")){
							op = 1;
							display.setText(currentString = currentString + " - ");
						}
						if (b.getText().equals("*")){
							op = 2;
							display.setText(currentString = currentString + " * ");
						}
						if (b.getText().equals("/")){
							op = 3;
							display.setText(currentString = currentString + " / ");
						}
					}	
				}};

		/////////////////////////////////////////////////////////////////
				
		ActionListener listenerEvaluate = new ActionListener(){
			public void actionPerformed(ActionEvent evaluate) {
				
			    if (op == 0) {	
					int result = operand1+Integer.parseInt(display.getText());
					display.setText(Integer.toString(result));
					op = -1;
					op1Set = false;
					calc = true;
				}
				if (op == 1) {
					int result = operand1-Integer.parseInt(display.getText());
					display.setText(Integer.toString(result));
					op = -1;
					op1Set = false;
					calc = true;
				}
				if (op == 2) {
					int result = operand1*Integer.parseInt(display.getText());
					display.setText(Integer.toString(result));
					op = -1;
					op1Set = false;
					calc = true;
				}
				if (op == 3) {
					int divisor = Integer.parseInt(display.getText());
					statusBar.setText("Can't divide by zero...please try another number");
					while (divisor == 0) {
						display.setText("");
						statusBar.setText("Can't divide by zero...please try another number");
						divisor = Integer.parseInt(display.getText());
					}
					
					int result = operand1/divisor;
					display.setText(Integer.toString(result));
					statusBar.setText("");
					op = -1;
					op1Set = false;
					calc = true;
				}
			}
		};
		
		/////////////////////////////////////////////////////////////////
		
		ActionListener listenerClear = new ActionListener(){
			public void actionPerformed(ActionEvent clear) {
				display.setText("");
			}
		};

		/////////////////////////////////////////////////////////////////
		
		JButton zero = new JButton("0");
		zero.addActionListener(listenerNumeric);

		JButton one = new JButton("1");
		one.addActionListener(listenerNumeric);

		JButton two = new JButton("2");
		two.addActionListener(listenerNumeric);

		JButton plus = new JButton("+");
		plus.addActionListener(listenerOperation);
		
		JButton three = new JButton("3");
		three.addActionListener(listenerNumeric);

		JButton four = new JButton("4");
		four.addActionListener(listenerNumeric);

		JButton five = new JButton("5");
		five.addActionListener(listenerNumeric);

		JButton minus = new JButton("-");
		minus.addActionListener(listenerOperation);
		
		JButton six = new JButton("6");
		six.addActionListener(listenerNumeric);

		JButton seven = new JButton("7");
		seven.addActionListener(listenerNumeric);

		JButton eight = new JButton("8");
		eight.addActionListener(listenerNumeric);

		JButton mult = new JButton("*");
		mult.addActionListener(listenerOperation);
		
		JButton nine = new JButton("9");
		nine.addActionListener(listenerNumeric);

		JButton div = new JButton("/");
		div.addActionListener(listenerOperation);
		
		JButton enter = new JButton("=");
		enter.addActionListener(listenerEvaluate);
		
		JButton parenthesesRight = new JButton(")");
		enter.addActionListener(listenerEvaluate);
		
		JButton parenthesesLeft = new JButton("(");
		enter.addActionListener(listenerEvaluate);
		
		enter.setForeground(Color.BLUE);
		
		JButton clear = new JButton("c");
		clear.setForeground(Color.BLACK);
		clear.addActionListener(listenerClear);
		
		/////////////////////////////////////////////////////////////////
		
		
		
		keyboard.add(one);
		keyboard.add(two);
		keyboard.add(three);
		keyboard.add(plus);
		keyboard.add(minus);
		keyboard.add(four);
		keyboard.add(five);
		keyboard.add(six);
		keyboard.add(mult);
		keyboard.add(div);
		keyboard.add(seven);
		keyboard.add(eight);
		keyboard.add(nine);
		keyboard.add(enter);
		keyboard.add(clear);
		keyboard.add(zero);
		keyboard.add(parenthesesLeft);
		keyboard.add(parenthesesRight);
	
		
			
		/////////////////////////////////////////////////////////////////

		frame.pack();
		frame.setResizable(false);
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(size.width/2, size.height/2);
		frame.setVisible(true);

		/////////////////////////////////////////////////////////////////
	}

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