/*********************************************************************************************************
 * File:  InterviewPojo.java 
 *
 * @author Mingzi Xu
 * @author
 * @author
 * @author
 * @author
 * @author (original) Mike Norman
 */
package data8319.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.faces.view.ViewScoped;

/**
 *
 * Description:  Model for the Physician object <br>
 * A little read about @ViewScoped <br>
 * https://stackoverflow.com/a/6026009/764951
 */
@ViewScoped
public class InterviewPojo implements Serializable {
	private static final long serialVersionUID = 1L;

	protected int id;
	protected String lastName;
	protected String firstName;
	protected String email;
	protected String phoneNumber;
	protected String specialty;
	protected String content;

	//a field to store the summary of the interview
	protected String chatGPTSummary;
	protected String geminiSummary;
	protected String summary;
	protected LocalDateTime created;
	protected boolean showFullSummary=false; // New field to track full summary visibility

	

	public InterviewPojo() {
		super();
	}
	public boolean isShowFullSummary() {
	    return showFullSummary;
	}

	public void setShowFullSummary(boolean showFullSummary) {
	    this.showFullSummary = showFullSummary;
	}
	
	public int getId() {
		return id;
	}

	/**
	 * @param id new value for id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the value for firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName new value for firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the value for lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName new value for lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	// getter and setter methods for the specialty field here
	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getChatGPTSummary() {
		return chatGPTSummary;
	}

	public void setChatGPTSummary(String chatGPTSummary) {
		this.chatGPTSummary = chatGPTSummary;
	}

	public String getGeminiSummary() {
		return geminiSummary;
	}

	public void setGeminiSummary(String geminiSummary) {
		this.geminiSummary = geminiSummary;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getCreated() {
		return created;
	}
	 private String customPrompt; // Store the custom prompt

	    // Getter and Setter for customPrompt
	    public String getCustomPrompt() {
	        return customPrompt;
	    }

	    public void setCustomPrompt(String customPrompt) {
	        this.customPrompt = customPrompt;
	    }
	 private String inputText;
	    private String selectedPrompt;

	    // Getter and Setter for inputText
	    public String getInputText() {
	        return inputText;
	    }

	    public void setInputText(String inputText) {
	        this.inputText = inputText;
	    }
	    // Getter and Setter for selectedPrompt
	    public String getSelectedPrompt() {
	        return selectedPrompt;
	    }

	    public void setSelectedPrompt(String selectedPrompt) {
	        this.selectedPrompt = selectedPrompt;
	    }
	// Use getter's for member variables
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		// Only include member variables that really contribute to an object's identity
		// i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
		// they shouldn't be part of the hashCode calculation
		return prime * result + Objects.hash(getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		/* enhanced instanceof - yeah!
		 * As of JDK 14, no need for additional 'silly' cast:
		    if (animal instanceof Cat) {
		        Cat cat = (Cat) animal;
		        cat.meow();
                // Other class Cat operations ...
            }
         * Technically, 'otherPhysicianPojo' is a <i>pattern</i> that becomes an in-scope variable binding.
         * Note:  Need to watch out just-in-case there is already a 'otherPhysicianPojo' variable in-scope!
		 */
		if (obj instanceof InterviewPojo otherPhysicianPojo) {
			// See comment (above) in hashCode():  Compare using only member variables that are
			// truly part of an object's identity
			return Objects.equals(this.getId(), otherPhysicianPojo.getId());
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Interview [id = ").append(getId()).append(", ");
		if (getFirstName() != null) {
			builder.append( "firstName = ").append(getFirstName()).append(", ");
		}
		if (getLastName() != null) {
			builder.append("lastName = ").append(getLastName()).append(", ");
		}
		if (getEmail() != null) {
			builder.append("email = ").append(getEmail()).append(", ");
		}
		if (getPhoneNumber() != null) {
			builder.append("phoneNumber = ").append(getPhoneNumber()).append(", ");
		}
		//append the specialty field
		if (getSpecialty() != null) {
		    builder.append("specialty = ").append(getSpecialty()).append(", ");
		}

		builder.append("]");
		return builder.toString();
	}

}
