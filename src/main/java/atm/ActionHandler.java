package atm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

enum State {
	WITHDRAW, DEPOSIT
}

/**
 * Class that perform actions when buttons clicked
 * 
 * @author Artiom
 *
 */
public class ActionHandler implements Initializable {

	// Withdraw/Deposit amount components
	@FXML
	private Rectangle rectangleAmount;
	@FXML
	private Label labelAmount;
	@FXML
	private Label labelWrittenAmount;

	// Card information components
	@FXML
	private Label labelBalance;
	@FXML
	private Label labelNumber;

	// Label that notifies about an error
	@FXML
	private Label labelError;

	// Receipt label
	@FXML
	private Label labelReceipt;

	// Int that stores card's balance
	private double balance;

	// Int that stores card's number
	private int number;

	// Int that stores card's pin
	private int pin;

	// State object that stores current state of action. Deposit/Withdraw
	State state;

	public void initialize(URL location, ResourceBundle resources) {
		setVisibleAmountComponents(false);
		initializeCardInformation();

		labelError.setVisible(false);

		balance = getBalanceFromFile();
	}

	/**
	 * Switches to Scene1 when logout button pressed
	 * 
	 * @param event - Event gotten from Scene
	 */
	public void logout(ActionEvent event) {
		SceneController sceneController = new SceneController();
		sceneController.switchToInsertScene(event);
	}

	/**
	 * Shows interface to withdraw an amount from card
	 * 
	 * @param event - Event gotten from Scene
	 */
	public void withdraw(ActionEvent event) {
		state = State.WITHDRAW;
		setVisibleAmountComponents(true);
		clear(event);
	}

	/**
	 * Shows interface to deposit an amount to card
	 * 
	 * @param event - Event gotten from Scene
	 */
	public void deposit(ActionEvent event) {
		state = State.DEPOSIT;
		setVisibleAmountComponents(true);
		clear(event);
	}

	/**
	 * Hides the interface to deposit/withdraw
	 * 
	 * @param event - Event gotten from Scene
	 */
	public void cancel(ActionEvent event) {
		setVisibleAmountComponents(false);
		labelError.setVisible(false);
		clear(event);
	}

	/**
	 * Performs the action. Deposit/Withdraw
	 * 
	 * @param event - Event gotten from Scene
	 */
	public void enter(ActionEvent event) {
		// Get the value written by user
		double amount = Double.parseDouble(labelWrittenAmount.getText());

		if (setBalance(amount)) {
			updateCard();

			if (amount != 0) {
				generateReceipt(amount);
			}

			labelError.setVisible(false);
			setVisibleAmountComponents(false);
			clear(event);
		}
	}

	/**
	 * Clears the written amount
	 * 
	 * @param event - Event gotten from Scene
	 */
	public void clear(ActionEvent event) {
		labelError.setVisible(false);
		labelWrittenAmount.setText("");
	}

	/**
	 * Adds a character to written amount
	 * 
	 * @param ch - Character you add to written amount
	 */
	private void addCharToAmount(String ch) {
		labelWrittenAmount.setText(labelWrittenAmount.getText() + ch);
	}

	/**
	 * Handles and perform actions when buttonOne clicked
	 * 
	 * @param event - Event gotten from source
	 */
	public void handleButtonOne(ActionEvent event) {
		addCharToAmount("1");
	}

	/**
	 * Handles and perform actions when buttonTwo clicked
	 * 
	 * @param event - Event gotten from source
	 */
	public void handleButtonTwo(ActionEvent event) {
		addCharToAmount("2");
	}

	/**
	 * Handles and perform actions when buttonThree clicked
	 * 
	 * @param event - Event gotten from source
	 */
	public void handleButtonThree(ActionEvent event) {
		addCharToAmount("3");
	}

	/**
	 * Handles and perform actions when buttonFour clicked
	 * 
	 * @param event - Event gotten from source
	 */
	public void handleButtonFour(ActionEvent event) {
		addCharToAmount("4");
	}

	/**
	 * Handles and perform actions when buttonFive clicked
	 * 
	 * @param event - Event gotten from source
	 */
	public void handleButtonFive(ActionEvent event) {
		addCharToAmount("5");
	}

	/**
	 * Handles and perform actions when buttonSix clicked
	 * 
	 * @param event - Event gotten from source
	 */
	public void handleButtonSix(ActionEvent event) {
		addCharToAmount("6");
	}

	/**
	 * Handles and perform actions when buttonSeven clicked
	 * 
	 * @param event - Event gotten from source
	 */
	public void handleButtonSeven(ActionEvent event) {
		addCharToAmount("7");
	}

	/**
	 * Handles and perform actions when buttonEight clicked
	 * 
	 * @param event - Event gotten from source
	 */
	public void handleButtonEight(ActionEvent event) {
		addCharToAmount("8");
	}

	/**
	 * Handles and perform actions when buttonNine clicked
	 * 
	 * @param event - Event gotten from source
	 */
	public void handleButtonNine(ActionEvent event) {
		addCharToAmount("9");
	}

	/**
	 * Handles and perform actions when buttonDot clicked
	 * 
	 * @param event - Event gotten from source
	 */
	public void handleButtonDot(ActionEvent event) {
		addCharToAmount(".");
	}

	/**
	 * Handles and perform actions when buttonZero clicked
	 * 
	 * @param event - Event gotten from source
	 */
	public void handleButtonZero(ActionEvent event) {
		addCharToAmount("0");
	}

	/**
	 * Handles and perform actions when buttonZeroZero clicked
	 * 
	 * @param event - Event gotten from source
	 */
	public void handleButtonZeroZero(ActionEvent event) {
		addCharToAmount("00");
	}

	/**
	 * Set components' visibility in dependance of boolean value
	 * 
	 * @param value - Set visibility to true or false
	 */
	private void setVisibleAmountComponents(boolean value) {
		rectangleAmount.setVisible(value);
		labelAmount.setVisible(value);
		labelWrittenAmount.setVisible(value);
	}

	/**
	 * Initializes information about card on the screen
	 */
	private void initializeCardInformation() {
		try {
			Scanner reader = new Scanner(CardController.currentCard);

			while (reader.hasNextLine()) {
				// String that stores a whole line from the file
				String data = reader.nextLine();

				// Checks if data stores balance/number information
				if (data.substring(0, 9).equals("balance: ")) {
					// Set balance
					labelBalance.setText("Balance: " + data.substring(9, data.length()));
				} else if (data.substring(0, 8).equals("number: ")) {
					// Set number
					number = Integer.parseInt(data.substring(8, data.length()));
					labelNumber.setText("Number: " + number);
				} else if (data.substring(0, 5).equals("pin: ")) {
					// Set pin
					pin = Integer.parseInt(data.substring(5, data.length()));
				}
			}
			
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns balance from file
	 * 
	 * @return - double value of balance
	 */
	private double getBalanceFromFile() {
		try {
			Scanner reader = new Scanner(CardController.currentCard);

			while (reader.hasNextLine()) {
				// String that stores a whole line from the file
				String data = reader.nextLine();

				// Checks if data stores balance/number information
				if (data.substring(0, 9).equals("balance: ")) {
					// Set balance
					return Double.parseDouble(data.substring(9, data.length()));
				}
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	/**
	 * Changes the value of balance in dependance of value and state
	 * 
	 * @param value - Value to deposit or withdraw to balance
	 */
	private boolean setBalance(double value) {
		if (state == State.DEPOSIT) {
			balance += value;
			labelBalance.setText("Balance: " + balance);

			return true;
		} else {
			// If after withdrawing balance will be less than 0
			if (balance - value < 0) {
				labelError.setText("You don't have enough money on your balance!");
				labelError.setVisible(true);

				return false;
			}

			balance -= value;
			labelBalance.setText("Balance: " + balance);

			return true;
		}
	}

	/**
	 * Updates the card's information
	 */
	private void updateCard() {
		try {
			// Creates BufferedWriter to write info about the card
			BufferedWriter writer = new BufferedWriter(new FileWriter(CardController.currentCard));

			// Generates random number, sets balance to zero and generates a random pin
			writer.write("number: " + number + "\nbalance: " + balance + "\npin: " + pin);

			// Closes the opened file
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates receipt for last transaction
	 * 
	 * 
	 * @param amount - Value that was deposited/withdrawed
	 */
	private void generateReceipt(double amount) {
		Date date = new Date();

		labelReceipt.setText("The transaction is completed. \n" + DateFormat.getDateTimeInstance().format(date)
				+ ".\nAmount: " + amount + "\nCard number: " + number + "\nBalance: " + balance);
	}
}
