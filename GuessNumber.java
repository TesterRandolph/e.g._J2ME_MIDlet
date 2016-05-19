import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.*;

public class GuessNumber extends MIDlet implements CommandListener, ItemCommandListener{
	private Display display;
	private Form mainForm;
		
	private final static Command startCommand = new Command("START", Command.OK, 1);
	private final static Command newCommand = new Command("NEW GAME", Command.OK, 1);
	private final static Command exitCommand = new Command("EXIT", Command.EXIT, 1);
	private final static Command submitCommand = new Command("SUBMIT", Command.ITEM, 1);
	
	private TextField input = new TextField("輸入 : ", "", 4, TextField.NUMERIC);
	private StringItem count = new StringItem("次數 : ", "");
	private StringItem output = new StringItem("結果 : ", "");
	private StringItem statement = new StringItem("評語 : ", "");
	
	private int Counter[] = new int[2], RandomDefault[] = new int[4], GetInput[] = new int[4];
	private int GuessCount = 0, gameState = 0;
	private Random random = new Random();
	private String results = "";
	
	protected void startApp() throws MIDletStateChangeException {
		display = Display.getDisplay(this);
		mainForm = new Form("Guess Number !!");
		mainForm.addCommand(startCommand);
		mainForm.addCommand(exitCommand);
		mainForm.setCommandListener(this);
		display.setCurrent(mainForm);		
	}
	
	protected void pauseApp() {
	}
	
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
	}
	
	public void commandAction(Command c, Item i) {
		if(c == submitCommand && gameState == 1) {
			String tmpString = "";
			if(getInput(Integer.parseInt(input.getString()))) {
				GuessCount++;
				getCompare();
				getReward();
				tmpString += Integer.toString(Counter[0]);
				tmpString += " A : ";
				tmpString += Integer.toString(Counter[1]);
				tmpString += " B ";
				if(GuessCount == 10) {
					gameState = 2;
					gameCheck();
				}
				else if(GuessCount < 10) {
					if(Counter[0] == 4) {
						gameState = 2;
						gameCheck();
					}
					else {
						gameState = 1;
					}
				}
			}
			else {
				tmpString = "請重新輸入正確的數字...";
				gameState = 1;
			}
			input.setString("");
			output.setText(tmpString);
			count.setText(Integer.toString(GuessCount));
			statement.setText(results);
		}
	}
	
	public void commandAction(Command c, Displayable d) {
		if(c == exitCommand) {
			try {
				destroyApp(false);
				notifyDestroyed();
			}
			catch(MIDletStateChangeException e) {}
		}
		else if(c == startCommand || c == newCommand) {
			gameState = 1;
			initCheck();
			mainForm.removeCommand(startCommand);
			mainForm.removeCommand(newCommand);
			input.setLayout(Item.LAYOUT_EXPAND);
			input.setDefaultCommand(submitCommand);
			input.setItemCommandListener(this);
			mainForm.append(input);
			input.setString("");
			output.setText("");
			output.setLayout(Item.LAYOUT_EXPAND);
			mainForm.append(output);
			count.setText("");
			count.setLayout(Item.LAYOUT_EXPAND);
			mainForm.append(count);
			statement.setText("");
			statement.setLayout(Item.LAYOUT_EXPAND);
			mainForm.append(statement);
		}
	}

	private void gameCheck() {
		if(gameState == 2) {
			input.removeCommand(submitCommand);
			mainForm.removeCommand(submitCommand);
			mainForm.addCommand(newCommand);
		}
	}
	
	private void initCheck() {
		Counter[0] = 0;
		Counter[1] = 0;
		RandomDefault[0] = -1;
		RandomDefault[1] = -1;
		RandomDefault[2] = -1;
		RandomDefault[3] = -1;
		GetInput[0] = -1;
		GetInput[1] = -1;
		GetInput[2] = -1;
		GetInput[3] = -1;
		GuessCount = 0;
		input.setString("");
		count.setText("");
		output.setText("");
		statement.setText("");
		getDefault();
	}
	
	private void getDefault() {
		int tmp = 0;
		for(int i = 0; i < 4; i++) {
			tmp = random.nextInt()%10;
			if(tmp < 0) {
				tmp = tmp * (-1);
			}

			for(int j = 0; j < 4; j++) {
				if(RandomDefault[j] < 0) {
					RandomDefault[j] = tmp;
					j = 4;
				}
				else if(RandomDefault[j] == tmp) {
					i--;
					j = 4;
				}
			}
		}
	}
	
	private boolean getInput(int tmpInt) {
		boolean iChecker = true;
		for(int i = 3; i >= 0; i--) {
			GetInput[i] = tmpInt%10;
			tmpInt = (tmpInt-GetInput[i])/10;
		}
		
		for(int j = 0; j < 4; j++) {
			for(int k = j + 1; k < 4; k++) {
				if(GetInput[j] == GetInput[k]) {
					iChecker = false;
					k = 4;
					j = 4;
				}
			}
		}
		return iChecker;
	}
	
	private void getCompare() {
		Counter[0] = 0;
		Counter[1] = 0;
		results = "";
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				if(RandomDefault[i] == GetInput [j]) {
					if(i == j) {
						Counter[0]++;
						j = 4;
					}
					else {
						Counter[1]++;
						j = 4;
					}
				}
			}
		}
	}
	
	private void getReward() {
		results = "";
		int tmpSum = 0;
		
		if(GuessCount == 10) {
			if(Counter[0] == 4) {
				tmpSum = 5;
			}
			else {
				tmpSum = 6;
			}
		}
		else {
			tmpSum = Counter[0]+Counter[1];
		}
		
		switch(tmpSum) {
			case 0 :
				results = "不要亂猜好嘛！";
				break;
			case 1 :
				results = "邁出成功的第一步！";
				break;
			case 2 :
				results = "不錯嘛！有進步喔！";
				break;
			case 3 :
				results = "再加把勁！就快成功了！";
				break;
			case 4 :
				if(Counter[0] == 4) {
					results = "恭喜！終於成功了！";
				}
				else {
					results = "勝利就在眼前了！";
				}
				break;
			case 5 :
				results = "恭喜！終於成功了！";
				break;
			case 6 :
				results = "笨死了！居然猜不到！";
				break;
			default :
				results = "發生例外！！";
				break;
		}
	}
}
