package clinic;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static java.util.stream.Collectors.*;


/**
 * Represents a clinic with patients and doctors.
 * 
 */
public class Clinic {
	protected Map<String, Patient> patients;
	protected Map<Integer, Doctor> doctors;
	protected Map<String, Integer> assignedPatient;

	/**
	 * Add a new clinic patient.
	 * 
	 * @param first first name of the patient
	 * @param last last name of the patient
	 * @param ssn SSN number of the patient
	 */
	
	public Clinic() {
		this.patients = new HashMap<>();
		this.doctors = new HashMap<>();
		this.assignedPatient = new HashMap<>();
	}
	
	public void addPatient(String first, String last, String ssn) {
		this.patients.put(ssn, new Patient(first, last, ssn));
	}


	/**
	 * Retrieves a patient information
	 * 
	 * @param ssn SSN of the patient
	 * @return the object representing the patient
	 * @throws NoSuchPatient in case of no patient with matching SSN
	 */
	public String getPatient(String ssn) throws NoSuchPatient {
		if(this.patients.containsKey(ssn))
			return this.patients.get(ssn).getLast() + " " + this.patients.get(ssn).getFirst() + " (" + this.patients.get(ssn).getSSN() + ")";
		else
			throw new NoSuchPatient();
	}

	/**
	 * Add a new doctor working at the clinic
	 * 
	 * @param first first name of the doctor
	 * @param last last name of the doctor
	 * @param ssn SSN number of the doctor
	 * @param docID unique ID of the doctor
	 * @param specialization doctor's specialization
	 */
	public void addDoctor(String first, String last, String ssn, int docID, String specialization) {
		this.doctors.put(docID, new Doctor(first, last, ssn, docID, specialization));
		this.addPatient(first, last, ssn);
	}

	/**
	 * Retrieves information about a doctor
	 * 
	 * @param docID ID of the doctor
	 * @return object with information about the doctor
	 * @throws NoSuchDoctor in case no doctor exists with a matching ID
	 */
	public String getDoctor(int docID) throws NoSuchDoctor {
		if(this.doctors.containsKey(docID))
			return this.doctors.get(docID).getLast() + " " + this.doctors.get(docID).getFirst() + " (" + this.doctors.get(docID).getSsn() + ") [" + this.doctors.get(docID).getId() + "]: " + this.doctors.get(docID).getSpecialization();
		else
			throw new NoSuchDoctor();
	}
	
	/**
	 * Assign a given doctor to a patient
	 * 
	 * @param ssn SSN of the patient
	 * @param docID ID of the doctor
	 * @throws NoSuchPatient in case of not patient with matching SSN
	 * @throws NoSuchDoctor in case no doctor exists with a matching ID
	 */
	public void assignPatientToDoctor(String ssn, int docID) throws NoSuchPatient, NoSuchDoctor {
		if(!this.patients.containsKey(ssn))
			throw new NoSuchPatient();
		if(!this.doctors.containsKey(docID))
			throw new NoSuchDoctor();
		this.assignedPatient.put(ssn, docID);
	}

	/**
	 * Retrieves the id of the doctor assigned to a given patient.
	 * 
	 * @param ssn SSN of the patient
	 * @return id of the doctor
	 * @throws NoSuchPatient in case of not patient with matching SSN
	 * @throws NoSuchDoctor in case no doctor has been assigned to the patient
	 */
	public int getAssignedDoctor(String ssn) throws NoSuchPatient, NoSuchDoctor {
		if(!this.patients.containsKey(ssn))
			throw new NoSuchPatient();
		if(!this.assignedPatient.containsKey(ssn))
			throw new NoSuchDoctor();
		return this.assignedPatient.get(ssn);
	}
	
	/**
	 * Retrieves the patients assigned to a doctor
	 * 
	 * @param id ID of the doctor
	 * @return collection of patient SSNs
	 * @throws NoSuchDoctor in case the {@code id} does not match any doctor 
	 */
	public Collection<String> getAssignedPatients(int id) throws NoSuchDoctor {
		if(!this.doctors.containsKey(id))
			throw new NoSuchDoctor();
		Map<Integer, List<String>> map = this.assignedPatient.entrySet().stream()
											.collect(groupingBy(entry -> entry.getValue(), HashMap::new, mapping(entry -> entry.getKey(), toList())));
		if(!map.containsKey(id))
			return Collections.emptyList();
		return map.get(id);
	}
	
	/**
	 * Loads data about doctors and patients from the given stream.
	 * <p>
	 * The text file is organized by rows, each row contains info about
	 * either a patient or a doctor.</p>
	 * <p>
	 * Rows containing a patient's info begin with letter {@code "P"} followed by first name,
	 * last name, and SSN. Rows containing doctor's info start with letter {@code "M"},
	 * followed by badge ID, first name, last name, SSN, and speciality.<br>
	 * The elements on a line are separated by the {@code ';'} character possibly
	 * surrounded by spaces that should be ignored.</p>
	 * <p>
	 * In case of error in the data present on a given row, the method should be able
	 * to ignore the row and skip to the next one.<br>

	 * 
	 * @param reader reader linked to the file to be read
	 * @throws IOException in case of IO error
	 */
	
	private String readLine (Reader r) throws IOException{
		StringBuffer res = new StringBuffer();
		try {
			int ch = r.read();
			if(ch == -1)
				return null;
			while(ch != -1) {
				char unicode = (char) ch;
				if(unicode == '\n')
					break;
				if(unicode != '\r')
					res.append(unicode);
				ch = r.read();
			}
			return res.toString();
		}
		catch(IOException e) {
			throw new IOException();
		}
	}
	
	public int loadData(Reader reader) throws IOException {
		int n = 0;
		String line;
		List<String> values = new ArrayList<>();
		try {
			line = this.readLine(reader);
			while(line != null) {
				values = Arrays.asList(line.split(";"));
				if(values.get(0).trim().equals("P") && values.size() == 4) {
					this.patients.put(values.get(3).trim(), new Patient(values.get(1).trim(), values.get(2).trim(), values.get(3).trim()));
					n++;
				}
				else if(values.get(0).trim().equals("M") && values.size() == 6) {
					this.doctors.put(Integer.valueOf(values.get(1).trim()), new Doctor(values.get(2).trim(), values.get(3).trim(), values.get(4).trim(), Integer.valueOf(values.get(1).trim()), values.get(5).trim()));
					n++;
				}
				line = this.readLine(reader);
			}
		}
		catch(IOException e) {
			throw new IOException();
		}
		return n;
	}


	/**
	 * Loads data about doctors and patients from the given stream.
	 * <p>
	 * The text file is organized by rows, each row contains info about
	 * either a patient or a doctor.</p>
	 * <p>
	 * Rows containing a patient's info begin with letter {@code "P"} followed by first name,
	 * last name, and SSN. Rows containing doctor's info start with letter {@code "M"},
	 * followed by badge ID, first name, last name, SSN, and speciality.<br>
	 * The elements on a line are separated by the {@code ';'} character possibly
	 * surrounded by spaces that should be ignored.</p>
	 * <p>
	 * In case of error in the data present on a given row, the method calls the
	 * {@link ErrorListener#offending} method passing the line itself,
	 * ignores the row, and skip to the next one.<br>
	 * 
	 * @param reader reader linked to the file to be read
	 * @param listener listener used for wrong line notifications
	 * @throws IOException in case of IO error
	 */
	public int loadData(Reader reader, ErrorListener listener) throws IOException {
		int n = 0;
		String line;
		List<String> values = new ArrayList<>();
		try {
			line = this.readLine(reader);
			while(line != null) {
				values = Arrays.asList(line.split(";"));
				if(values.get(0).trim().equals("P") && values.size() == 4) {
					this.patients.put(values.get(3).trim(), new Patient(values.get(1).trim(), values.get(2).trim(), values.get(3).trim()));
					n++;
				}
				else if(values.get(0).trim().equals("M") && values.size() == 6 && values.get(1).trim().matches("[0-9]+")) {
					this.doctors.put(Integer.valueOf(values.get(1).trim()), new Doctor(values.get(2).trim(), values.get(3).trim(), values.get(4).trim(), Integer.valueOf(values.get(1).trim()), values.get(5).trim()));
					n++;
				}
				else
					listener.offending(line);
				line = this.readLine(reader);
			}
		}
		catch(IOException e) {
			throw new IOException();
		}
		return n;
	}
	
	/**
	 * Retrieves the collection of doctors that have no patient at all.
	 * The doctors are returned sorted in alphabetical order
	 * 
	 * @return the collection of doctors' ids
	 */
	public Collection<Integer> idleDoctors(){
	return this.doctors.values().stream()
									.filter(doctor -> !this.assignedPatient.containsValue(doctor.getId()))
									.sorted(Comparator.comparing(doctor -> doctor.getLast() + " " + doctor.getFirst()))
									.map(doctor -> doctor.getId())
									.collect(toList());
	}

	/**
	 * Retrieves the collection of doctors having a number of patients larger than the average.
	 * 
	 * @return  the collection of doctors' ids
	 */
	public Collection<Integer> busyDoctors(){
		double averageNumOfPatients = this.assignedPatient.entrySet().stream()
																			.collect(groupingBy(entry -> entry.getValue(), HashMap::new, counting()))
																			.entrySet().stream()
																			.collect(averagingLong(entry -> entry.getValue()));
		return this.assignedPatient.entrySet().stream()
													.collect(groupingBy(entry -> entry.getValue(), HashMap::new, counting()))
													.entrySet().stream()
													.filter(entry -> entry.getValue() >= averageNumOfPatients)
													.map(entry -> entry.getKey())
													.collect(toList());
	}

	/**
	 * Retrieves the information about doctors and relative number of assigned patients.
	 * <p>
	 * The method returns list of strings formatted as "{@code ### : ID SURNAME NAME}" where {@code ###}
	 * represent the number of patients (printed on three characters).
	 * <p>
	 * The list is sorted by decreasing number of patients.
	 * 
	 * @return the collection of strings with information about doctors and patients count
	 */
	public Collection<String> doctorsByNumPatients(){
		Collection<String> result = this.assignedPatient.entrySet().stream()
													.collect(groupingBy(entry -> entry.getValue(), HashMap::new, counting()))
													.entrySet().stream()
													.sorted(Comparator.comparingLong(entry -> entry.getValue()))
													.collect(collectingAndThen(toList(), list -> {
														Collections.reverse(list);
														return list.stream();
													}))
													.map(entry -> String.format("%3d : %d %s %s", entry.getValue(), this.doctors.get(entry.getKey()).getId(), this.doctors.get(entry.getKey()).getLast(), this.doctors.get(entry.getKey()).getFirst()))
													.collect(toList());
		result.addAll(this.doctors.keySet().stream()
													.filter(docId -> !this.assignedPatient.containsValue(docId))
													.map(docId -> String.format("000 : %d %s %s", docId, this.doctors.get(docId).getLast(), this.doctors.get(docId).getFirst()))
													.collect(toList()));
		return result;
	}
	
	/**
	 * Retrieves the number of patients per (their doctor's)  speciality
	 * <p>
	 * The information is a collections of strings structured as {@code ### - SPECIALITY}
	 * where {@code SPECIALITY} is the name of the speciality and 
	 * {@code ###} is the number of patients cured by doctors with such speciality (printed on three characters).
	 * <p>
	 * The elements are sorted first by decreasing count and then by alphabetic speciality.
	 * 
	 * @return the collection of strings with speciality and patient count information.
	 */
	public Collection<String> countPatientsPerSpecialization(){
		return this.assignedPatient.entrySet().stream()
												.map(entry -> Map.entry(entry.getKey(), this.doctors.get(entry.getValue()).getSpecialization()))
												.collect(groupingBy(entry -> entry.getValue(), () -> new TreeMap<>(), counting()))
												.entrySet().stream()
												.map(entry -> Map.entry(entry.getValue(), entry.getKey()))
												.sorted(Comparator.comparingLong(entry -> entry.getKey()))
												.collect(collectingAndThen(toList(), list -> {
													Collections.reverse(list);
													return list.stream();
												}))
												.map(entry -> String.format("%3d - %s", entry.getKey(), entry.getValue()))
												.collect(toList());
	}

}
