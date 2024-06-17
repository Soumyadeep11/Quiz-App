package project;

import java.io.Serializable;

public class Question implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String questionText;
	private String[] options;
	private int correctOption;
	private String category;
	private String difficulty;
	
	public Question(String questionText, String[] options, int correctOption, String category, String difficulty)
	{
		this.questionText = questionText;
		this.options = options;
		this.correctOption = correctOption;
		this.category = category;
		this.difficulty = difficulty;
	}
	
	public String getQuestionText() {
		
		return questionText;
		
	}
	
	public String[] getOptions() {
		return options;
	}
	
	
	public int getCorrectOption() {
		return correctOption;
		
	}
		public String getCategory() {
			return category;
		}
		
	public String getDifficulty() {
		return difficulty;
	}
	
	public boolean isCorrect(int userAnswer) {
		return userAnswer == correctOption;
	}
	
	

}
