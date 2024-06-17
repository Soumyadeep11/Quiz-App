package project;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class QuizApp extends JFrame {
	
	private List<Question> questions;
	private List<Question> filteredQuestions;
	private int currentQuestionIndex = 0;
	private int score = 0;
	private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionsGroup;
    private JLabel timerLabel;
    private Timer questionTimer;
    private int timeLeft;
    
    
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> difficultyComboBox;
    
    
    public QuizApp() {
    	setTitle("Quiz Application");
    	setSize(600, 400);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	questions = loadQuestions();
    	filteredQuestions = new ArrayList<>(questions);
    	
    	
    	JPanel  mainPanel = new JPanel(new BorderLayout());
    	JPanel topPanel = new JPanel(new GridLayout(2, 3));
    	JPanel questionPanel = new JPanel(new BorderLayout());
    	JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
    	JPanel bottomPanel = new JPanel(new FlowLayout());
    	
    	categoryComboBox = new JComboBox<>(new String[]{"All", "General Knowledge", "Science", "Math"});
    	difficultyComboBox = new JComboBox<>(new String[]{"All", "Easy","Medium", "Hard"});
    	
    	
    	topPanel.add(new JLabel("Select Category:"));
    	topPanel.add(categoryComboBox);
    	topPanel.add(new JLabel("Select Difficulty:"));
    	topPanel.add(difficultyComboBox);
    	
    	JButton filterButton = new JButton("Filter Questions");
    	filterButton.addActionListener(new ActionListener() {
    		
    		public void actionPerformed(ActionEvent e) {
    			
    			filterQuestions();
    			
    		}
    	});
    	
    	topPanel.add(filterButton);
    	
    	mainPanel.add(topPanel, BorderLayout.NORTH);
    	
    	
    	questionLabel = new JLabel("Question will appear here", SwingConstants.CENTER);
    	questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
    	questionPanel.add(questionLabel, BorderLayout.NORTH);
    	
    	
    	optionsGroup = new ButtonGroup();
    	optionButtons = new JRadioButton[4];
    	 
    	for(int i = 0; i<optionButtons.length;i++) {
    		optionButtons[i] = new JRadioButton();
    		optionsGroup.add(optionButtons[i]);
    		optionsPanel.add(optionButtons[i]);
    		
    	}
    	
    	questionPanel.add(optionsPanel,  BorderLayout.CENTER);
    	mainPanel.add(questionPanel, BorderLayout.CENTER);
    	
    	timerLabel = new JLabel("Time left : 30 seconds");
    	bottomPanel.add(timerLabel);
    	
    	JButton nextButton = new JButton("Next");
    	nextButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			handleNextButton();
    			
    		}
    	});
    	
    	bottomPanel.add(nextButton);
    	mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    	
    	add(mainPanel);
    	
    	loadQuestion(currentQuestionIndex);
    	startQuestionTimer();
    	
    }
    
    
    private List<Question> loadQuestions(){
    	
    	List<Question> questions = new ArrayList<>();
    	questions.add(new Question("Which of the following options leads to the probability and security of Java ?", new String[] {"Bytecode is executed by JVM", "The applet makes the Java code secure and portable", "Use of exception handling", "Dynamic binding between objects"}, 0, "General Knowledge", "Easy"));
    	questions.add(new Question("What is 2 + 2?", new String[] {"3", "4", "5", "6"}, 1, "Math", "Easy"));
   
    
        return questions;
    }
    
    private void filterQuestions() {
    	String selectedCategory = categoryComboBox.getSelectedItem().toString();
    	String selectedDifficulty =  difficultyComboBox.getSelectedItem().toString();
    	filteredQuestions.clear();
    	
    	for(Question question : questions) {
    		boolean categoryMatches = selectedCategory.equals("All") || question.getCategory().equals(selectedCategory);
    		boolean difficultyMatches = selectedDifficulty.equals("All") || question.getDifficulty().equals(selectedDifficulty);
    		if(categoryMatches && difficultyMatches) {
    			filteredQuestions.add(question);
    		}
    	}
    	
    	if(!filteredQuestions.isEmpty()) {
    		currentQuestionIndex = 0;
    		score = 0;
    		loadQuestion(currentQuestionIndex);
    		startQuestionTimer();
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(this, "No question avaialble for the selected criteria" , "No Questions", JOptionPane.INFORMATION_MESSAGE);
    		
    	}
    }
    
    private void loadQuestion(int questionIndex) {
    	Question question = filteredQuestions.get(questionIndex);
    	questionLabel.setText("<html>"  + question.getQuestionText() + "<html>");
    	String[] options = question.getOptions();
    	for(int i = 0; i < options.length; i++) {
    		optionButtons[i].setText(options[i]);
    		optionButtons[i].setSelected(false);
     		optionButtons[i].setSelected(true);
    	}
    
    for (int i = options.length; i< optionButtons.length; i++) {
    	optionButtons[i].setVisible(false);
    }
    
    }
   
    
    
    
    private void handleNextButton() {
    	
    	checkAnswer();
    	moveToNextQuestion();
    	
    }
    
    private void checkAnswer() {
    
    	Question currentQuestion = filteredQuestions.get(currentQuestionIndex);
    	int selectedIndex = -1;
    	for(int i = 0; i < optionButtons.length; i++) {
    		if(optionButtons[i].isSelected()) {
    			selectedIndex = i;
    			break;
    			
    		}
    	}
    
    if(selectedIndex != -1 && currentQuestion.isCorrect(selectedIndex)) {
    	score++;
    	
    }
    }
    
    private void moveToNextQuestion() {
    	
    
    currentQuestionIndex++;
    
    if(currentQuestionIndex < filteredQuestions.size()) {
    	loadQuestion(currentQuestionIndex);
    	startQuestionTimer();
    	
    }
    else
    {
    	showResults();
    	
    }
    
    }
    
    private void startQuestionTimer() {
    	if(questionTimer != null) {
    		questionTimer.cancel();
    	}
    	timeLeft = 30;
    	timerLabel.setText("Time left: " + timeLeft + " seconds");
    	
    questionTimer = new Timer();
    questionTimer.scheduleAtFixedRate(new TimerTask() {
 
    
      public void run() {
    	  SwingUtilities.invokeLater(() -> {
    		  timeLeft--;
    		  timerLabel.setText("Time left: " + timeLeft + " seconds");
    		  if(timeLeft <= 0) {
    			  questionTimer.cancel();
    			checkAnswer();
    			moveToNextQuestion();
    		  }
    });
    
      }
    }, 1000, 1000);
    }
    
    private void showResults() {
    	JOptionPane.showMessageDialog(this, "You scored " + score + " out of " + filteredQuestions.size(), "Quiz Completed", JOptionPane.INFORMATION_MESSAGE);
    	
    }
    

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SwingUtilities.invokeLater(() -> new QuizApp().setVisible(true));
	}

}
