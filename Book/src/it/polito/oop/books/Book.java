package it.polito.oop.books;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static java.util.stream.Collectors.*;

public class Book {
	
	Map<String, Topic> topics = new TreeMap<>();
	List<Question> questions = new ArrayList<>();
	List<TheoryChapter> theoryChapters = new ArrayList<>();
	List<ExerciseChapter> exerciseChapters = new ArrayList<>();
	List<Assignment> assignments = new ArrayList<>();

    /**
	 * Creates a new topic, if it does not exist yet, or returns a reference to the
	 * corresponding topic.
	 * 
	 * @param keyword the unique keyword of the topic
	 * @return the {@link Topic} associated to the keyword
	 * @throws BookException
	 */
	public Topic getTopic(String keyword) throws BookException {
	    if(keyword == null || keyword == "")
	    	throw new BookException();
	    if(!this.topics.containsKey(keyword))
	    	this.topics.put(keyword, new Topic(keyword));
	    return this.topics.get(keyword);
	}

	public Question createQuestion(String question, Topic mainTopic) {
        Question q = new Question(question, mainTopic);
        this.questions.add(q);
        return q;
	}

	public TheoryChapter createTheoryChapter(String title, int numPages, String text) {
        TheoryChapter t = new TheoryChapter(title, numPages, text);
        this.theoryChapters.add(t);
        return t;
	}

	public ExerciseChapter createExerciseChapter(String title, int numPages) {
        ExerciseChapter e = new ExerciseChapter(title, numPages);
        this.exerciseChapters.add(e);
        return e;
	}

	public List<Topic> getAllTopics() {
        List<Topic> result =  this.theoryChapters.stream()
						        							.map(t -> t.getTopics())
						        							.flatMap(list -> list.stream())
						        							.collect(toList());
        
        List<Topic> exerciseTopics = this.exerciseChapters.stream()
															.map(e -> e.getTopics())
															.flatMap(list -> list.stream())
															.collect(toList());
        result.addAll(exerciseTopics);
        return result.stream().distinct().sorted(Comparator.comparing(t -> t.toString())).collect(toList());
	}

	public boolean checkTopics() {
        return this.exerciseChapters.stream()
        								.map(e -> e.getTopics())
        								.flatMap(list -> list.stream())
        								.allMatch(topic -> this.theoryChapters.stream()
        																			.map(t -> t.getTopics())
        																			.flatMap(list -> list.stream())
        																			.collect(toList()).contains(topic));
	}

	public Assignment newAssignment(String ID, ExerciseChapter chapter) {
        Assignment a = new Assignment(ID, chapter);
        this.assignments.add(a);
        return a;
	}
	
    /**
     * builds a map having as key the number of answers and 
     * as values the list of questions having that number of answers.
     * @return
     */
    public Map<Long,List<Question>> questionOptions(){
        return this.questions.stream()
        							.collect(groupingBy(question -> Long.valueOf(question.answers.size()), HashMap::new, toList()));
    }
}
