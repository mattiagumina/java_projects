package sports;
import java.util.*;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;

 
/**
 * Facade class for the research evaluation system
 *
 */
public class Sports {

	Set<String> activities = new HashSet<>();
	Map<String, List<String>> categories = new HashMap<>();
	Map<String, Product> products = new HashMap<>();
	List<Rating> ratings = new ArrayList<>();
	
    //R1
    /**
     * Define the activities types treated in the portal.
     * The method can be invoked multiple times to add different activities.
     * 
     * @param actvities names of the activities
     * @throws SportsException thrown if no activity is provided
     */
    public void defineActivities (String... activities) throws SportsException {
    	if(activities == null || activities.length == 0)
    		throw new SportsException("");
    	Stream.of(activities).forEach(activity -> this.activities.add(activity));
    }

    /**
     * Retrieves the names of the defined activities.
     * 
     * @return activities names sorted alphabetically
     */
    public List<String> getActivities() {
        return this.activities.stream().sorted().collect(toList());
    }


    /**
     * Add a new category of sport products and the linked activities
     * 
     * @param name name of the new category
     * @param activities reference activities for the category
     * @throws SportsException thrown if any of the specified activity does not exist
     */
    public void addCategory(String name, String... linkedActivities) throws SportsException {
    	if(!Stream.of(linkedActivities).allMatch(activity -> this.activities.contains(activity)))
    		throw new SportsException("");
    	this.categories.put(name, Arrays.asList(linkedActivities));
    }

    /**
     * Retrieves number of categories.
     * 
     * @return categories count
     */
    public int countCategories() {
        return this.categories.size();
    }

    /**
     * Retrieves all the categories linked to a given activity.
     * 
     * @param activity the activity of interest
     * @return list of categories (sorted alphabetically)
     */
    public List<String> getCategoriesForActivity(String activity) {
        return this.categories.entrySet().stream()
        										.filter(entry -> entry.getValue().contains(activity))
        										.map(entry -> entry.getKey())
        										.sorted()
        										.collect(toList());
    }

    //R2
    /**
     * Add a research group and the relative disciplines.
     * 
     * @param name name of the research group
     * @param disciplines list of disciplines
     * @throws SportsException thrown in case of duplicate name
     */
    public void addProduct(String name, String activityName, String categoryName) throws SportsException {
    	if(this.products.containsKey(name))
    		throw new SportsException("");
    	this.products.put(name, new Product(name, activityName, Map.entry(categoryName, this.categories.get(categoryName))));
    }

    /**
     * Retrieves the list of products for a given category.
     * The list is sorted alphabetically.
     * 
     * @param categoryName name of the category
     * @return list of products
     */
    public List<String> getProductsForCategory(String categoryName){
        return this.products.values().stream()
        									.filter(product -> product.category.getKey().equals(categoryName))
        									.map(product -> product.name)
        									.sorted()
        									.collect(toList());
    }

    /**
     * Retrieves the list of products for a given activity.
     * The list is sorted alphabetically.
     * 
     * @param activityName name of the activity
     * @return list of products
     */
    public List<String> getProductsForActivity(String activityName){
        return this.products.values().stream()
        									.filter(product -> product.activity.equals(activityName))
        									.map(product -> product.name)
        									.sorted()
        									.collect(toList());
    }

    /**
     * Retrieves the list of products for a given activity and a set of categories
     * The list is sorted alphabetically.
     * 
     * @param activityName name of the activity
     * @param categoryNames names of the categories
     * @return list of products
     */
    public List<String> getProducts(String activityName, String... categoryNames){
        return this.products.values().stream()
        									.filter(product -> product.activity.equals(activityName))
        									.filter(product -> Stream.of(categoryNames).anyMatch(category -> category.equals(product.category.getKey())))
        									.map(product -> product.name)
        									.sorted()
        									.collect(toList());
    }

    //    //R3
    /**
     * Add a new product rating
     * 
     * @param productName name of the product
     * @param userName name of the user submitting the rating
     * @param numStars score of the rating in stars
     * @param comment comment for the rating
     * @throws SportsException thrown numStars is not correct
     */
    public void addRating(String productName, String userName, int numStars, String comment) throws SportsException {
    	if(numStars < 0 || numStars > 5)
    		throw new SportsException("");
    	this.ratings.add(new Rating(productName, userName, numStars, comment));
    }



    /**
     * Retrieves the ratings for the given product.
     * The ratings are sorted by descending number of stars.
     * 
     * @param productName name of the product
     * @return list of ratings sorted by stars
     */
    public List<String> getRatingsForProduct(String productName) {
        return this.ratings.stream()
        							.filter(rating -> rating.productName.equals(productName))
        							.sorted(Comparator.comparing(rating -> 1.0D / rating.numStars))
        							.map(rating -> rating.toString())
        							.collect(toList());
    }


    //R4
    /**
     * Returns the average number of stars of the rating for the given product.
     * 
     * 
     * @param productName name of the product
     * @return average rating
     */
    public double getStarsOfProduct (String productName) {
        return this.ratings.stream()
        							.filter(rating -> rating.productName.equals(productName))
        							.map(rating -> rating.numStars)
        							.collect(averagingInt(x -> x));
    }

    /**
     * Computes the overall average stars of all ratings
     *  
     * @return average stars
     */
    public double averageStars() {
        return this.ratings.stream()
        							.map(rating -> rating.numStars)
        							.collect(averagingInt(x -> x));

    }

    //R5 Statistiche
    /**
     * For each activity return the average stars of the entered ratings.
     * 
     * Activity names are sorted alphabetically.
     * 
     * @return the map associating activity name to average stars
     */
    public SortedMap<String, Double> starsPerActivity() {
        return this.ratings.stream()
        							.collect(groupingBy(rating -> this.products.get(rating.productName).activity, TreeMap::new, mapping(rating -> rating.numStars, averagingInt(x -> x))));
    }

    /**
     * For each average star rating returns a list of
     * the products that have such score.
     * 
     * Ratings are sorted in descending order.
     * 
     * @return the map linking the average stars to the list of products
     */
    public SortedMap<Double, List<String>> getProductsPerStars () {
        return this.ratings.stream()
        							.collect(groupingBy(rating -> rating.productName, TreeMap::new, mapping(rating -> rating.numStars, averagingInt(x -> x))))
        							.entrySet().stream()
        							.collect(groupingBy(entry -> entry.getValue(), () -> new TreeMap(Comparator.reverseOrder()), mapping(entry -> entry.getKey(), toList())));
    }

}