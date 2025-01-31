/*********************************************************************************************************
 * File:  InterviewController.java 
 *
 * @author Mingzi Xu
 * @author
 * @author
 * @author
 * @author
 * @author (original) Mike Norman
 */
package data8319.jsf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;

import data8319.dao.InterviewDao;
import data8319.dao.ListDataDao;
import data8319.model.InterviewPojo;
import data8319.service.ChatGptService;
import data8319.service.SummaryGenerator;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.annotation.SessionMap;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

/**
 * Description:  Responsible for collection of interview Pojo's in XHTML (list) <h:dataTable> </br>
 * Delegates all C-R-U-D behavior to DAO
 */

// managed bean with a session scope
@Named
@SessionScoped 
public class InterviewController implements Serializable {
	private static final long serialVersionUID = 1L;

	// session map object will be injected. 
	@Inject
	@SessionMap
	private Map<String, Object> sessionMap;

	@Inject
	private InterviewDao interviewDao;

	@Inject
	private ListDataDao listDataDao;

	private List<InterviewPojo> interviews;

	private boolean isUserAction = false;
	 private String customPrompt=null; // Store the custom prompt

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
	public void toggleSummary(int interviewId) {
	        //InterviewPojo interview = interviewDao.readInterviewById(interviewId);
	        for (InterviewPojo interview : interviews) {
	            if (interview.getId() == interviewId) {
	                boolean newState = !interview.isShowFullSummary();
	                interview.setShowFullSummary(newState);
	                System.out.println("Show Full Summary toggled to: " + newState);
	                break; // Exit the loop once the interview is found
	            }
	        }
	    }
	//Necessary methods to make controller work

	public void loadInterviews() {
		setInterviews(interviewDao.readAllInterviews());
		 for (InterviewPojo interview : interviews) {
		        interview.setShowFullSummary(false); // Initialize to false
		        System.out.println("Show Full Summary: " + interview.isShowFullSummary()); 
		    }
	}

	public List<InterviewPojo> getInterviews() {
		return interviews;
	}

	public void setInterviews(List<InterviewPojo> interviews) {
		this.interviews = interviews;
	}

	public List<String> getSpecialties() {
		return this.listDataDao.readAllSpecialties();
	}
	 private Part uploadedFile;

	    // Getter and setter for uploadedFile
	    public Part getUploadedFile() {
	        return uploadedFile;
	    }

	    public void setUploadedFile(Part uploadedFile) {
	        this.uploadedFile = uploadedFile;
	    }
	public String navigateToAddForm() {
		//Pay attention to the name here, it will be used as the object name in add-Interview.xhtml
		//ex. <h:inputText value="#{newInterviewn.firstName}" id="firstName" />
		sessionMap.put("newInterview", new InterviewPojo());
		return "add-interview.xhtml?faces-redirect=true";
	}
	public String listInterviews() {
		//Pay attention to the name here, it will be used as the object name in add-Interview.xhtml
		//ex. <h:inputText value="#{newInterviewn.firstName}" id="firstName" />
		sessionMap.put("newInterview", new InterviewPojo());
		 return "list-interviews.xhtml?faces-redirect=true";
	}
	public String submitInterview(InterviewPojo interview, Part uploadedFile) {
    try {
    	
        // Check if a file is uploaded
        if (uploadedFile == null || uploadedFile.getSize() == 0) {
            // Show an alert if no file is uploaded
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please upload a file.", null));
            return null; // Redirect back to the form
        }

        // Get the content type of the uploaded file
        String fileType = uploadedFile.getContentType();
        // Define allowed file types (e.g., text files only)
        if (!fileType.equals("text/plain")) {
            // Show an alert if the file type is not supported
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unsupported file format. Please upload a text file.", null));
            return null; // Redirect back to the form
        }

        // Read the content of the uploaded file
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(uploadedFile.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            // Set the file content to the 'content' field of the interview
            interview.setContent(fileContent.toString());
        }

        // Set the created date to the current date and time
        interview.setCreated(LocalDateTime.now());

        // Use DAO to insert the interview into the database
        interviewDao.createInterview(interview);

        // Navigate the user back to list-interviews.xhtml
        return "list-interviews.xhtml?faces-redirect=true";
    } catch (Exception e) {
        e.printStackTrace();
        return null; // Return null in case of an error, handle appropriately in your app
    }
}

	  
	public String navigateToUpdateForm(int interviewId) {
		//Use DAO to find the interview object from the database first
        InterviewPojo interview = interviewDao.readInterviewById(interviewId);
		// Use session map to keep track of of the object being edited
        sessionMap.put("editInterview", interview);

		// navigate the user to the edit/update form
        return "edit-interview.xhtml?faces-redirect=true";
	}
	public String submitUpdatedInterview(InterviewPojo interview) {
		// Use DAO to update the interview in the database
		interviewDao.updateInterview(interview);
		// navigate the user back to list-interviews.xhtml
        return "list-interviews.xhtml?faces-redirect=true";
	}

	public String deleteInterview(int interviewId) {
		//Use DAO to delete the interview from the database
		interviewDao.deleteInterviewById(interviewId);
		//navigate the user back to list-interviews.xhtml
        return "list-interviews.xhtml?faces-redirect=true";
	}
	public void downloadInterview(int interviewId) {
        try {
            // Fetch the interview by ID (replace this with your actual method to fetch interview)
            InterviewPojo interview = interviewDao.readInterviewById(interviewId);

            if (interview != null) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = facesContext.getExternalContext();

                // Set the response content type and headers for file download
                externalContext.setResponseContentType("text/plain");
                externalContext.setResponseHeader("Content-Disposition", "attachment;filename=interview_content_" + interviewId + ".txt");

                // Only write the 'content' field of the interview to the file
                String content = interview.getContent();

                if (content != null && !content.isEmpty()) {
                    OutputStream outputStream = externalContext.getResponseOutputStream();
                    outputStream.write(content.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                    outputStream.close();
                } else {
                    // Handle the case where there is no content
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "No content available for download.", null));
                }

                // Mark the response as complete
                facesContext.responseComplete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	//generate summary
	public String goToSummaryOptionsPage(int interviewId) {
		InterviewPojo interview = interviewDao.readInterviewById(interviewId);
			// Use session map to keep track of of the object being edited
	     sessionMap.put("selectedInterview", interview);
	    return "summary-options.xhtml?faces-redirect=true";
	}

	 public String selectPrompt(int interviewId,String prompt) {
		    System.out.println("selectPrompt called with interviewId: " + interviewId + " and prompt: " + prompt);

	        this.selectedPrompt = prompt;
	        if (prompt.equals("Custom")) {
	            this.inputText = customPrompt;  // Use the custom prompt entered by the user
	        } else {
	        // Depending on the selected prompt, you can set a predefined prompt
	        switch (prompt) {
	            case "Prompt 1":
	                this.inputText = "Provide a detailed summary of the following  content, highlighting all key points, context, and important mentions. " +
	                        "Include background information where necessary, covering any essential definitions or explanations that would help someone unfamiliar with the topic understand it fully.\n\n" ;
	                break;
	            case "Prompt 2":
	                this.inputText = "Please provide a thorough and organized summary of the following transcript by completing each of the sections below in a clear and detailed manner:\n\n" +
		                    "Main Topic: Identify and explain the primary subject or theme of the interview, including any relevant context or background information necessary for a comprehensive understanding of the discussion.\n\n" +
		                    "Participants and Perspectives: List all participants (e.g., Participant A, Participant B) and summarize their roles, viewpoints, and primary contributions to the conversation. Highlight any contrasting viewpoints, agreements, or key exchanges that shaped the discussion.\n\n" +
		                    "Key Outcomes or Conclusions: Describe the final conclusions, resolutions, or decisions reached during the interview. If applicable, note any action steps, follow-up items, or unresolved points that emerged by the end of the conversation.\n\n" +
		                    "Core Concepts and Themes: Break down each core concept or theme discussed (e.g., Concept A, Concept B), detailing how each was introduced, explored, or developed by the participants. Mention specific contributions by each participant to show how the ideas evolved or were debated.\n\n" +
		                    "Detailed Content Summary: Summarize the interview's content in a structured, cohesive manner, capturing the major topics, arguments, and examples covered throughout. Include any essential terminology, definitions, or background points shared during the conversation to enhance comprehension.\n\n";
	                break;
	            default:
	                this.inputText = "";
	                break;
	        }
	        }
	        generateSummary(interviewId, selectedPrompt);
	        
	        return "summary-options.xhtml?faces-redirect=true";
	    }
	public String generateSummary(int interviewId, String selectedPrompt) {
		System.out.println("generateSummary called with interviewId: " + interviewId);
		
		// Fetch the interview by ID
	    InterviewPojo interview = interviewDao.readInterviewById(interviewId);
	    
	    
	    if (interview != null ) {
	        try {
	            // Prepare the ChatGPT API request
	           // String prompt = "Generate a summary for the following interview content:\n\n" + interview.getContent();
	        
	        	
	            String prompt = selectedPrompt +"Interview Content:\n" + interview.getContent();
               
	        	//String prompt= PromptProvider.getSummaryPrompt1(interview.getContent());
	           // String chatGptSummary = callChatGptApi(prompt);
	            //chatGptSummary = chatGptSummary.replace("\n", "<br>");
	            SummaryGenerator summaryGenerator = new SummaryGenerator(new ChatGptService());
	            String generatedSummary = summaryGenerator.generateSummary(interview, prompt);

	    	    //System.out.println("Generated Summary: " + chatGptSummary);

	            // Update the summary field with the ChatGPT-generated summary
	            interview.setSummary(generatedSummary);

	            // Use DAO to update the interview in the database
	            interviewDao.updateInterview(interview);
	            // Update the session map to reflect changes on the UI
	            sessionMap.put("selectedInterview", interview);

	         // Return the generated summary
	            return generatedSummary;
	            // Confirmation message
	           
	        } catch (Exception e) {
	            // Error message if API call fails
	            FacesContext.getCurrentInstance().addMessage(null, 
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
	                "Error generating summary for Interview ID: " + interviewId, null));
	            e.printStackTrace();
	        }
	    } else {
	        // Error message if interview not found
	        FacesContext.getCurrentInstance().addMessage(null, 
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, 
	            "Interview not found for ID: " + interviewId, null));
	    } 
	    return null; // Return null if there’s an error
	   
	}
	 public String getFormattedSummary(InterviewPojo interview) {
	        if (interview.getSummary() != null) {
	            // Replace newlines with <br> for HTML display without changing the database
	            return interview.getSummary().replace("\n", "<br>");
	        }
	        return "";
	    }
	
	    private boolean successMessageVisible = false;
	    public boolean isSuccessMessageVisible() {
	        return successMessageVisible;
	    }

	    public void closeSuccessDialog() {
	        successMessageVisible = false;
	    }
	    public void setSuccessMessageVisible(boolean successMessageVisible) {
	        this.successMessageVisible = successMessageVisible;
	        System.out.print(successMessageVisible);
	    }
	 // Method to save the summary in the database
	    public void saveSummary(int interviewId) {
	        System.out.println("saveSummaryAndUpdateSatisfaction called with interviewId: " + interviewId);

	        InterviewPojo interview = interviewDao.readInterviewById(interviewId);
	        if (interview != null && interview.getSummary() != null) {
	            // Save the summary to the database
	        	interviewDao.updateInterview(interview);
	        }
	        successMessageVisible = true;

	       // return "summary-options.xhtml?faces-redirect=true"; // Navigate back to the same page after saving
	    }
	

	// Second API call for key phrases -google
	


		      // Passes the provided text input to the Gemini model and returns the text-only response.
		      // For the specified textPrompt, the model returns a list of possible store names.
	public static String textInput(
		        	      String projectId, String location, String modelName, String prompt) throws IOException {
		 try {      	    // Initialize client that will be used to send requests. This client only needs
		        	    // to be created once, and can be reused for multiple requests.
		        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
		          GenerativeModel model = new GenerativeModel(modelName, vertexAI);

		          GenerateContentResponse response = model.generateContent(prompt);
		          String output = ResponseHandler.getText(response);
		          return output;
		        }
    } catch (IOException e) {
        System.err.println("IOException occurred while communicating with Vertex AI: " + e.getMessage());
        e.printStackTrace();  // Print stack trace for detailed error context
        throw e;  // Rethrow the exception to be handled in the calling method
    } catch (Exception e) {
        System.err.println("Unexpected error during API call: " + e.getMessage());
        e.printStackTrace();  // Print stack trace for detailed error context
        throw new IOException("Unexpected error during API call", e);  // Wrap and throw as IOException
    }
}
		    
	
	//download summary-done
	public void downloadSummary(int interviewId) {
	    try {
	        // Fetch the interview by ID
	        InterviewPojo interview = interviewDao.readInterviewById(interviewId);

	        if (interview != null) {
	            FacesContext facesContext = FacesContext.getCurrentInstance();
	            ExternalContext externalContext = facesContext.getExternalContext();

	            // Set the response content type and headers for file download
	            externalContext.setResponseContentType("text/plain");
	            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=interview_summary_" + interviewId + ".txt");

	            // Write the 'summary' field of the interview to the file
	            String summary = interview.getSummary();

	            if (summary != null && !summary.isEmpty()) {
	                OutputStream outputStream = externalContext.getResponseOutputStream();
	                outputStream.write(summary.getBytes(StandardCharsets.UTF_8));
	                outputStream.flush();
	                outputStream.close();
	            } else {
	                // Handle the case where there is no summary available
	                FacesContext.getCurrentInstance().addMessage(null,
	                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "No summary available for download.", null));
	            }

	            // Mark the response as complete
	            facesContext.responseComplete();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


}
