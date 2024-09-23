package it.polito.oop.books;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Book {
	private Map<String, Topic> topicsDictionary = new HashMap<>();
	private List<Chapter> chapters = new LinkedList<>();
	private List<Question> questions = new LinkedList<>();

	/**
	 * Creates a new topic, if it does not exist yet, or returns a reference to the
	 * corresponding topic.
	 * 
	 * @param keyword the unique keyword of the topic
	 * @return the {@link Topic} associated to the keyword
	 * @throws BookException
	 */
	public Topic getTopic(String keyword) throws BookException {
		if (keyword == null || keyword == "")
			throw new BookException();
		if (!topicsDictionary.containsKey(keyword))
			topicsDictionary.put(keyword, new Topic(keyword));
		return topicsDictionary.get(keyword);
	}

	public Question createQuestion(String question, Topic mainTopic) {
	    Question q = new Question(question, mainTopic);
	    questions.add(q);
	    return q;
	}

	public TheoryChapter createTheoryChapter(String title, int numPages, String text) {
		TheoryChapter c = new TheoryChapter(title, numPages, text);
		chapters.add(c);
		return c;
	}

	public ExerciseChapter createExerciseChapter(String title, int numPages) {
		ExerciseChapter c = new ExerciseChapter(title, numPages);
		chapters.add(c);
		return c;
	}

	public List<Topic> getAllTopics() {
		return chapters.stream()
		        .flatMap(c -> c.getTopics().stream())
		        .distinct()
		        .sorted()
		        .collect(Collectors.toList());
	}

	public boolean checkTopics() {
		Set<Topic> t = chapters.stream().filter(c -> c instanceof TheoryChapter).flatMap(c -> c.getTopics().stream())
				.collect(Collectors.toSet());
		Set<Topic> e = chapters.stream().filter(c -> c instanceof ExerciseChapter).flatMap(c -> c.getTopics().stream())
				.collect(Collectors.toSet());
		return t.containsAll(e);
	}

	public Assignment newAssignment(String ID, ExerciseChapter chapter) {
		return new Assignment(ID, chapter);
	}

	
//	public List<String> topicPopularity() {
//		return chapters.stream()
//		        .filter(c -> c instanceof TheoryChapter)
//		        .flatMap(c -> c.getTopics().stream())
//				.collect(Collectors.groupingBy(s -> s.toString(), Collectors.counting())).entrySet().stream()
//				.sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry::getKey))
//				.map(e -> e.getKey() + " : " + e.getValue()).collect(Collectors.toList());
//	}

	public Map<Long, List<Question>> questionOptions() {
		return chapters.stream()
		        .filter(c -> c instanceof ExerciseChapter)
				.flatMap(c -> ((ExerciseChapter) c).questions.stream())
				.collect(Collectors.groupingBy(q -> q.numAnswers(), Collectors.toList()));
		
		// For all defined questions:
//        return questions.stream()
//                .collect(Collectors.groupingBy(q -> q.numAnswers(), Collectors.toList()));
	}
}
