package mountainhuts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.*;

/**
 * Class {@code Region} represents the main facade
 * class for the mountains hut system.
 * 
 * It allows defining and retrieving information about
 * municipalities and mountain huts.
 *
 */
public class Region {
	
	protected String name;
	protected List<String> altitudeRanges;
	protected List<Municipality> municipalities;
	protected List<MountainHut> mountainHuts;

	/**
	 * Create a region with the given name.
	 * 
	 * @param name
	 *            the name of the region
	 */
	public Region(String name) {
		this.name = name;
		this.altitudeRanges = new ArrayList<>();
		this.municipalities = new ArrayList<>();
		this.mountainHuts = new ArrayList<>();
	}

	/**
	 * Return the name of the region.
	 * 
	 * @return the name of the region
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Create the ranges given their textual representation in the format
	 * "[minValue]-[maxValue]".
	 * 
	 * @param ranges
	 *            an array of textual ranges
	 */
	public void setAltitudeRanges(String... ranges) {
		for(String range: ranges) {
			this.altitudeRanges.add(range);
		}
	}

	/**
	 * Return the textual representation in the format "[minValue]-[maxValue]" of
	 * the range including the given altitude or return the default range "0-INF".
	 * 
	 * @param altitude
	 *            the geographical altitude
	 * @return a string representing the range
	 */
	public String getAltitudeRange(Integer altitude) {
		String [] values = new String[2];
		for(String range: this.altitudeRanges) {
			values = range.split("-");
			if(altitude >= Integer.valueOf(values[0]) && altitude <= Integer.valueOf(values[1]))
				return range;
		}
		return "0-INF";
	}

	/**
	 * Return all the municipalities available.
	 * 
	 * The returned collection is unmodifiable
	 * 
	 * @return a collection of municipalities
	 */
	public Collection<Municipality> getMunicipalities() {
		return this.municipalities;
	}

	/**
	 * Return all the mountain huts available.
	 * 
	 * The returned collection is unmodifiable
	 * 
	 * @return a collection of mountain huts
	 */
	public Collection<MountainHut> getMountainHuts() {
		return this.mountainHuts;
	}

	/**
	 * Create a new municipality if it is not already available or find it.
	 * Duplicates must be detected by comparing the municipality names.
	 * 
	 * @param name
	 *            the municipality name
	 * @param province
	 *            the municipality province
	 * @param altitude
	 *            the municipality altitude
	 * @return the municipality
	 */
	public Municipality createOrGetMunicipality(String name, String province, Integer altitude) {
		for(Municipality mun: this.municipalities) {
			if(mun.getName().equals(name))
				return mun;
		}
		Municipality mun = new Municipality(name, province, altitude);
		this.municipalities.add(mun);
		return mun;
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 *
	 * @param name
	 *            the mountain hut name
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return the mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, String category, Integer bedsNumber,
			Municipality municipality) {
		for(MountainHut mHut: this.mountainHuts) {
			if(mHut.getName().equals(name))
				return mHut;
		}
		MountainHut mHut = new MountainHut(name, null, category, bedsNumber, municipality);
		this.mountainHuts.add(mHut);
		return mHut;
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 * 
	 * @param name
	 *            the mountain hut name
	 * @param altitude
	 *            the mountain hut altitude
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return a mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, Integer altitude, String category, Integer bedsNumber,
			Municipality municipality) {
		for(MountainHut mHut: this.mountainHuts) {
			if(mHut.getName() == name)
				return mHut;
		}
		MountainHut mHut = new MountainHut(name, altitude, category, bedsNumber, municipality);
		this.mountainHuts.add(mHut);
		return mHut;
	}

	/**
	 * Creates a new region and loads its data from a file.
	 * 
	 * The file must be a CSV file and it must contain the following fields:
	 * <ul>
	 * <li>{@code "Province"},
	 * <li>{@code "Municipality"},
	 * <li>{@code "MunicipalityAltitude"},
	 * <li>{@code "Name"},
	 * <li>{@code "Altitude"},
	 * <li>{@code "Category"},
	 * <li>{@code "BedsNumber"}
	 * </ul>
	 * 
	 * The fields are separated by a semicolon (';'). The field {@code "Altitude"}
	 * may be empty.
	 * 
	 * @param name
	 *            the name of the region
	 * @param file
	 *            the path of the file
	 */
	public static Region fromFile(String name, String file) {
		Region reg = new Region(name);
		List<String> rows = new ArrayList<>();
		String [] values = new String[7];
		boolean first = true;
		rows = readData(file);
		for(String row: rows) {
			if(first)
				first = false;
			else {
				values = row.split(";");
				reg.createOrGetMunicipality(values[1], values[0], Integer.valueOf(values[2]));
				if(values[4] == "")
					reg.createOrGetMountainHut(values[3], null, values[5], Integer.valueOf(values[6]), reg.createOrGetMunicipality(values[1], values[0], Integer.valueOf(values[2])));
				else
					reg.createOrGetMountainHut(values[3], Integer.valueOf(values[4]), values[5], Integer.valueOf(values[6]), reg.createOrGetMunicipality(values[1], values[0], Integer.valueOf(values[2])));
			}
		}
		return reg;
	}

	/**
	 * Reads the lines of a text file.
	 *
	 * @param file path of the file
	 * @return a list with one element per line
	 */
	public static List<String> readData(String file) {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			return in.lines().collect(toList());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Count the number of municipalities with at least a mountain hut per each
	 * province.
	 * 
	 * @return a map with the province as key and the number of municipalities as
	 *         value
	 */
	public Map<String, Long> countMunicipalitiesPerProvince() {
		Map<String, Long> map = new HashMap<>();
		map = this.municipalities.stream()
				.collect(groupingBy(Municipality::getProvince, counting()));
		return map;
	}

	/**
	 * Count the number of mountain huts per each municipality within each province.
	 * 
	 * @return a map with the province as key and, as value, a map with the
	 *         municipality as key and the number of mountain huts as value
	 */
	public Map<String, Map<String, Long>> countMountainHutsPerMunicipalityPerProvince() {
		Map<String, Map<String, Long>> map = new HashMap<>();
		map = this.mountainHuts.stream()
										.collect(groupingBy(x -> x.getMunicipality().getProvince(), groupingBy(x -> x.getMunicipality().getName(), counting())));
		return map;
	}

	/**
	 * Count the number of mountain huts per altitude range. If the altitude of the
	 * mountain hut is not available, use the altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the number of mountain huts
	 *         as value
	 */
	
	private String getAltitudeRange(MountainHut hut) {
		int altitude;
		if(hut.getAltitude().isPresent())
			altitude = hut.getAltitude().get();
		else
			altitude = hut.getMunicipality().getAltitude();
		return this.getAltitudeRange(altitude);
	}
	
	public Map<String, Long> countMountainHutsPerAltitudeRange() {
		Map<String, Long> map = new HashMap<>();
		map = this.mountainHuts.stream()
										.collect(groupingBy(hut -> getAltitudeRange(hut), HashMap::new, counting()));
		return map;
	}

	/**
	 * Compute the total number of beds available in the mountain huts per each
	 * province.
	 * 
	 * @return a map with the province as key and the total number of beds as value
	 */
	public Map<String, Integer> totalBedsNumberPerProvince() {
		Map<String, Integer> map = new HashMap<>();
		map = this.mountainHuts.stream()
										.collect(groupingBy(hut -> hut.getMunicipality().getProvince(), HashMap::new, summingInt(MountainHut::getBedsNumber)));
		return map;
	}

	/**
	 * Compute the maximum number of beds available in a single mountain hut per
	 * altitude range. If the altitude of the mountain hut is not available, use the
	 * altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the maximum number of beds
	 *         as value
	 */
	public Map<String, Optional<Integer>> maximumBedsNumberPerAltitudeRange() {
		Map<String, Optional<Integer>> map = new HashMap<>();
		map = this.mountainHuts.stream()
										.collect(groupingBy(hut -> getAltitudeRange(hut), HashMap::new, mapping(MountainHut::getBedsNumber, maxBy(Comparator.comparingInt(n -> n)))));
		return map;
	}

	/**
	 * Compute the municipality names per number of mountain huts in a municipality.
	 * The lists of municipality names must be in alphabetical order.
	 * 
	 * @return a map with the number of mountain huts in a municipality as key and a
	 *         list of municipality names as value
	 */
	public Map<Long, List<String>> municipalityNamesPerCountOfMountainHuts() {
		Map<Long, List<String>> map = new HashMap<>();
		map = this.mountainHuts.stream()
										.collect(groupingBy(hut -> hut.getMunicipality().getName(), () -> new TreeMap<String, Long>(), counting()))
												.entrySet().stream()
												.collect(groupingBy(entry -> entry.getValue(), HashMap::new, mapping(entry -> entry.getKey(), toList())));
		return map;
	}

}
