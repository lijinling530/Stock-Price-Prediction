import java.awt.Choice;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.ui.RefineryUtilities;

public class StockPanel {

	private JFrame frame;
	private JPanel panel;
	private JInternalFrame internalFrame;
	
	private JLabel label;
	private JLabel marketDay;
	private JLabel stockName;
	private JLabel transaction;
	private JLabel amount;
	private JLabel portfolio;
	private JLabel stockSum;
	private JLabel stockSumAmount;
	private JLabel balance;
	private JLabel credit;
	private JLabel interest;
	private JLabel interestAmount;
	private JLabel total;
	private JLabel totalAmount;
	private JLabel transactionHistory;
	
	private JTable stockTable;
	private JTable portfolioTable;
	
	private JButton nextDay;
	private JButton confirm;
	private JButton button;
	private JButton show;
	
	private Choice stockChoice;
	private Choice transactionChoice;
	private Choice stockHsChoice;
	
	private JTextField amountInput;
	
	private int day = 1;
	private int find = 0;
	private int curMoney = 100000;
	private int gain = 0;
	private int sumStock = 0;
	private int totalMoney = 100000;
	private int marketStart;
	private int marketCurrent;
	
	private String[] stockList = {"Amazon", "Facebook", "Google", "Microsoft"};
	private int[] stockPrice = {1100, 970, 1040, 1090};	
	private int[] stockAmount = new int[4];
	
	private int[][] stockHistory = new int[4][100];
	private double[][] marketHistory = new double[2][100];
	private int[] predictHistory = new int[100];
	
	private List<int[]> amznHistory = new ArrayList<>();
	private List<int[]> fbHistory = new ArrayList<>();
	private List<int[]> googlHistory = new ArrayList<>();
	private List<int[]> msftHistory = new ArrayList<>();
	private List<List<int[]>> transHistory = new ArrayList<>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StockPanel window = new StockPanel();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StockPanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		for(int i = 0; i < 4; i++) {
			stockHistory[i][0] = stockPrice[i];
			marketStart += stockPrice[i];
		}
		marketHistory[0][0] = 0;
		marketHistory[1][0] = 0;
		
		transHistory.add(amznHistory);
		transHistory.add(fbHistory);
		transHistory.add(googlHistory);
		transHistory.add(msftHistory);
		
		frame = new JFrame("Stock Simulation System");
		frame.setBounds(100, 100, 1000, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	   
		
		panel = new JPanel();
		panel.setVisible(true);
		panel.setLayout(null);
		frame.setContentPane(panel);
		   
		label = new JLabel(" Stock Market");
		label.setLocation(20, 10);
		label.setSize(200, 20);
		panel.add(label);
		
		marketDay = new JLabel("Day 1");
		marketDay.setLocation(920, 10);
		marketDay.setSize(100, 20);
		panel.add(marketDay);
	
		stockTable = new JTable();
		stockTable.setLocation(20, 30);
		stockTable.setSize(940, 100);
		panel.add(stockTable);
		stockTable.setBorder(new EtchedBorder(EtchedBorder.LOWERED,
			    UIManager.getColor("nimbusInfoBlue"),
			    UIManager.getColor("nimbusFocus")));
		stockTable.setRowHeight(20);
		stockTable.setShowVerticalLines(true);
		DefaultTableModel tableModel = new DefaultTableModel(
			    new Object[][]{
			    	{"  Stock Name", "Day 1", "Day 2 Prediction"},
			    	{"  Amazon", "$" + stockPrice[0], "$" + (stockPrice[0] + 5)},
			        {"  Facebook", "$" + stockPrice[1], "$" + (stockPrice[1] + 9)},
			        {"  Google", "$" + stockPrice[2], "$" + (stockPrice[2] - 12)},
			        {"  Microsoft", "$" + stockPrice[3], "$" + (stockPrice[3] + 15)},},
			    new String[]{
			        "", "", ""}
			    ) {

				
				boolean[] columnEditables = new boolean[]{
			        false, false, false
			    };

			    public boolean isCellEditable(int row, int column) {
			        return columnEditables[column];
			    }
			};
		stockTable.setModel(tableModel);
		
		nextDay = new JButton("Next Day");
		nextDay.setLocation(850, 410);
		nextDay.setSize(100, 20);
//		nextDay.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//			}
//		});
		nextDay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				day++;
				marketDay.setText("Day " + day);
				Random random = new Random();
				double aRan = random.nextDouble() * 0.2 - 0.1;
				double fRan = random.nextDouble() * 0.2 - 0.1;
				double gRan = random.nextDouble() * 0.2 - 0.1;
				double mRan = random.nextDouble() * 0.2 - 0.1;
				stockPrice[0] += stockPrice[0] * aRan;
				stockPrice[1] += stockPrice[1] * fRan;
				stockPrice[2] += stockPrice[2] * gRan;
				stockPrice[3] += stockPrice[3] * mRan;
				tableModel.addColumn("");
				find++;
				int cur = day + find;
				tableModel.setValueAt("Day " + day, 0, cur);
				tableModel.setValueAt("$" + stockPrice[0], 1, cur);
				tableModel.setValueAt("$" + stockPrice[1], 2, cur);
				tableModel.setValueAt("$" + stockPrice[2], 3, cur);
				tableModel.setValueAt("$" + stockPrice[3], 4, cur);
				
				int predict = cur + 1;
				tableModel.addColumn("");
				tableModel.setValueAt("Day " + (day + 1) + " Prediction", 0, predict);
				int[] stockPredict = getNext(stockHistory, day);
				tableModel.setValueAt("$" + stockPredict[0], 1, predict);
				tableModel.setValueAt("$" + stockPredict[1], 2, predict);
				tableModel.setValueAt("$" + stockPredict[2], 3, predict);
				tableModel.setValueAt("$" + stockPredict[3], 4, predict);
				
				stockChoice.select(0);
				transactionChoice.select(0);
				amountInput.setText("");
				
				totalMoney = curMoney;
				sumStock = 0;
				for(int i = 0; i < 4; i++){
					if(stockAmount[i] != 0){
						totalMoney += stockPrice[i] * stockAmount[i];
						sumStock += stockPrice[i] * stockAmount[i];
						portfolioTable.setValueAt("$" + stockPrice[i] * stockAmount[i], i + 1, 2);
					}
					stockHistory[i][day - 1] = stockPrice[i];
					marketCurrent += stockPrice[i];
				}
				marketHistory[0][day - 1] = (double) marketCurrent / marketStart - 1;
				marketStart = marketCurrent;
				marketCurrent = 0;
				
				stockSumAmount.setText("$" + sumStock);
				gain = totalMoney - 100000;
				marketHistory[1][day - 1] = (double) gain / 100000;
				
				totalAmount.setText("$" + totalMoney);
				StringBuilder sb = new StringBuilder();
				sb.append('$').append(Math.abs(gain));
				if(gain < 0)
					sb.insert(0, '-');
				interestAmount.setText(sb.toString());
				
				predictHistory[day] = stockPredict[0];
				System.out.println("Day " + day);
				System.out.print("Real: " + stockHistory[0][day - 1]);
				System.out.println("; Day " + (day + 1) + " Predict: " + predictHistory[day]);
				
			}
		});
		panel.add(nextDay);
		
		stockName = new JLabel("Stock");
		stockName.setBounds(20, 150, 90, 20);
		panel.add(stockName);
		
		transaction = new JLabel("Transaction");
		transaction.setBounds(140, 150, 90, 20);
		panel.add(transaction);
		
		amount = new JLabel("Amount");
		amount.setBounds(220, 150, 90, 20);
		panel.add(amount);	

		stockChoice = new Choice();
		stockChoice.setBounds(20, 170, 90, 80);
		stockChoice.addItem("");
		for(int i = 0; i < stockList.length; i++)
			stockChoice.addItem(stockList[i]);
		stockChoice.setBackground(Color.cyan);
		panel.add(stockChoice);
		
		transactionChoice = new Choice();
		transactionChoice.setBounds(140, 170, 60, 20);
		transactionChoice.addItem("");
		transactionChoice.addItem("Buy");
		transactionChoice.addItem("Sell");
		transactionChoice.setBackground(Color.orange);
		panel.add(transactionChoice);
		
		amountInput = new JTextField("0");
		amountInput.setBounds(220, 170, 50, 25);
		panel.add(amountInput);
		
		portfolio = new JLabel("My Portfolio");
		portfolio.setBounds(20, 240, 90, 20);
		panel.add(portfolio);
		
		portfolioTable = new JTable();
		portfolioTable.setLocation(20, 260);
		portfolioTable.setSize(250, 100);
		panel.add(portfolioTable);
		portfolioTable.setBorder(new EtchedBorder(EtchedBorder.LOWERED,
			    UIManager.getColor("nimbusInfoBlue"),
			    UIManager.getColor("nimbusFocus")));
		portfolioTable.setRowHeight(20);
		portfolioTable.setShowVerticalLines(true);
		DefaultTableModel portfoliotableModel = new DefaultTableModel(
			    new Object[][]{
			    	{"  Stock", "Amount", "Value"},
			    	{"  Amazon", 0, "$" + 0},
			        {"  Facebook", 0, "$" + 0},
			        {"  Google", 0, "$" + 0},
			        {"  Microsoft", 0, "$" + 0},},
			    new String[]{
			        "", "", ""}
			    ) {

				
				boolean[] columnEditables = new boolean[]{
			        false, false, false
			    };

			    public boolean isCellEditable(int row, int column) {
			        return columnEditables[column];
			    }
			};
		portfolioTable.setModel(portfoliotableModel);
		
		stockSum = new JLabel("Stock Sum:");
		stockSum.setBounds(20, 360, 110, 20);
		panel.add(stockSum);
		
		stockSumAmount = new JLabel("$0");
		stockSumAmount.setBounds(97, 360, 50, 20);
		panel.add(stockSumAmount);
		
		balance = new JLabel("Available:");
		balance.setBounds(20, 375, 110, 20);
		panel.add(balance);
		
		credit = new JLabel("$100000");
		credit.setBounds(97, 375, 50, 20);
		panel.add(credit);
		
		interest = new JLabel("Interest:");
		interest.setBounds(20, 390, 50, 20);
		panel.add(interest);
		
		interestAmount = new JLabel("$0");
		interestAmount.setBounds(97, 390, 50, 20);
		panel.add(interestAmount);
		
		total = new JLabel("Total:");
		total.setBounds(20, 405, 50, 20);
		panel.add(total);
		
		totalAmount = new JLabel("$100000");
		totalAmount.setBounds(97, 405, 50, 20);
		panel.add(totalAmount);
		
		confirm = new JButton("Confirm");
		confirm.setBounds(170, 200, 100, 20);
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String pickStock = stockChoice.getSelectedItem();
				String trans = transactionChoice.getSelectedItem();
				int transAmount = 0;
				if(amountInput.getText().equals(""))
					JOptionPane.showMessageDialog(null, "Amount couldn't be empty", "ATTENTION!", JOptionPane.ERROR_MESSAGE);
				else
					transAmount = Integer.valueOf(amountInput.getText()); 
				
				if(pickStock.equals(""))
					JOptionPane.showMessageDialog(null, "Please select a stock", "ATTENTION!", JOptionPane.ERROR_MESSAGE);					
				if(trans.equals(""))
					JOptionPane.showMessageDialog(null, "Please choose transaction type", "ATTENTION!", JOptionPane.ERROR_MESSAGE);
				if(transAmount <= 0)
					JOptionPane.showMessageDialog(null, "Amount must be more than 0", "ATTENTION!", JOptionPane.ERROR_MESSAGE);
				
				int stockIdx = 0;				
				switch(pickStock){
					case "Amazon":
						stockIdx = 0;
						break;
					case "Facebook":
						stockIdx = 1;
						break;
					case "Google":
						stockIdx = 2;
						break;
					case "Microsoft":
						stockIdx = 3;
						break;
				}
				if(trans.equals("Buy") && curMoney <= stockPrice[stockIdx] * transAmount)
					JOptionPane.showMessageDialog(null, "Sorry but you don't have enough money", "ATTENTION!", JOptionPane.WARNING_MESSAGE);
				else if(trans.equals("Sell") && transAmount > stockAmount[stockIdx])
					JOptionPane.showMessageDialog(null, "Sorry but you can't sell stock with amount more than you currently hold", "ATTENTION!", JOptionPane.WARNING_MESSAGE);
				else {
					if(trans.equals("Buy")){
						stockAmount[stockIdx] += transAmount;
						curMoney -= stockPrice[stockIdx] * transAmount;
						sumStock += stockPrice[stockIdx] * transAmount;
					}
					if(trans.equals("Sell")){
						stockAmount[stockIdx] -= transAmount;
						curMoney += stockPrice[stockIdx] * transAmount;
						sumStock -= stockPrice[stockIdx] * transAmount;
					}
					int[] tmp = new int[4];
					tmp[0] = day;
					if(trans.equals("Buy"))
						tmp[1] = 1;
					tmp[2] = transAmount;
					tmp[3] = stockPrice[stockIdx];
					transHistory.get(stockIdx).add(tmp);
					
					portfolioTable.setValueAt(stockAmount[stockIdx], stockIdx + 1, 1);
					portfolioTable.setValueAt("$" + stockPrice[stockIdx] * stockAmount[stockIdx], stockIdx + 1, 2);
					
					stockSumAmount.setText("$" + sumStock);
					credit.setText("$" + curMoney);
				}
			}
		});
		panel.add(confirm);
		
		transactionHistory = new JLabel("Transaction History");
		transactionHistory.setBounds(810, 150, 120, 20);
		panel.add(transactionHistory);
		
		TextArea textArea = new TextArea();
		textArea.setBounds(810, 200, 160, 190);
		panel.add(textArea);
		
		stockHsChoice = new Choice();
		stockHsChoice.setBounds(810, 170, 90, 80);
		stockHsChoice.addItem("");
		for(int i = 0; i < stockList.length; i++)
			stockHsChoice.addItem(stockList[i]);
		stockHsChoice.setBackground(Color.cyan);
		panel.add(stockHsChoice);
		
		button = new JButton("OK");
		button.setBounds(910, 170, 55, 22);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String pickStock = stockHsChoice.getSelectedItem();
				if(pickStock.equals(""))
					JOptionPane.showMessageDialog(null, "Please select a stock", "ATTENTION!", JOptionPane.ERROR_MESSAGE);
				
				int stockIdx = 0;				
				switch(pickStock){
					case "Amazon":
						stockIdx = 0;
						break;
					case "Facebook":
						stockIdx = 1;
						break;
					case "Google":
						stockIdx = 2;
						break;
					case "Microsoft":
						stockIdx = 3;
						break;
				}
				if(!pickStock.equals("")){
					StringBuilder sb = new StringBuilder();
					for(int[] tmp : transHistory.get(stockIdx))
						sb.append("Day ").append(tmp[0]).append(":\n").append(tmp[1] == 1 ? "BUY " : "SELL ")
						.append(tmp[2]).append(" Shares;\nPrice per share: $").append(tmp[3]).append(".\n");
					textArea.setText(sb.toString());
					if(sb.length() == 0)
						textArea.setText("No transaction yet.");
				}
			}
		});
		panel.add(button);
		
		internalFrame = new JInternalFrame("Data Flow For Markets and Selected Stocks");
		internalFrame.setBounds(280, 150, 520, 280);
		panel.add(internalFrame);
		internalFrame.setVisible(true);
        
        show = new JButton("Visualize");
        show.setBounds(180, 400, 90, 20);
        show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CombinedXYPlot demo = new CombinedXYPlot(internalFrame, stockHistory, marketHistory, day);
		        //demo.pack();
		        RefineryUtilities.centerFrameOnScreen(demo);
		        //demo.setVisible(true);
			}
		});
		panel.add(show);
	}
	
	private int[] getNext(int[][] stock, int cur){
		int[] res = new int[4];
		double[][] data = new double[cur][2];
		for(int i = 0; i < 4; i++){
			SimpleRegression simpleRegression = new SimpleRegression(true);
			for(int j = 0; j < cur; j++){
				data[j][0] = j + 1;
				data[j][1] = stock[i][j];
			}			
			simpleRegression.addData(data);
			res[i] = (int) simpleRegression.predict(cur + 1);
			
//			if(i==0){
//				System.out.println("Slope: " + simpleRegression.getSlope());
//				System.out.println("Intercept: " + simpleRegression.getIntercept());
//				System.out.println(simpleRegression.getSlope() * (cur + 1) + simpleRegression.getIntercept());
//			}
		}
		return res;
	}
}
