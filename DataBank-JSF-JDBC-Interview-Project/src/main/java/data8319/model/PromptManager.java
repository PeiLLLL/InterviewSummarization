package data8319.model;
public class PromptManager {
    private String customPrompt;

    // Constructor
    public PromptManager() {
    }

    // Getter and Setter for custom prompt
    public String getCustomPrompt() {
        return customPrompt;
    }

    public void setCustomPrompt(String customPrompt) {
        this.customPrompt = customPrompt;
    }

    // Method to get the prompt based on the selection
    public String getPrompt(String promptType) {
        switch (promptType) {
            case "Custom":
                return customPrompt;
            case "Prompt 1":
                return "Provide a detailed summary of the following content, highlighting all key points, context, and important mentions. " +
                       "Include background information where necessary, covering any essential definitions or explanations that would help someone unfamiliar with the topic understand it fully.\n\n";
            case "Prompt 2":
                return "Please provide a thorough and organized summary of the following transcript by completing each of the sections below in a clear and detailed manner:\n\n" +
                       "Main Topic: Identify and explain the primary subject or theme of the interview, including any relevant context or background information necessary for a comprehensive understanding of the discussion.\n\n" +
                       "Participants and Perspectives: List all participants (e.g., Participant A, Participant B) and summarize their roles, viewpoints, and primary contributions to the conversation. Highlight any contrasting viewpoints, agreements, or key exchanges that shaped the discussion.\n\n" +
                       "Key Outcomes or Conclusions: Describe the final conclusions, resolutions, or decisions reached during the interview. If applicable, note any action steps, follow-up items, or unresolved points that emerged by the end of the conversation.\n\n" +
                       "Core Concepts and Themes: Break down each core concept or theme discussed (e.g., Concept A, Concept B), detailing how each was introduced, explored, or developed by the participants. Mention specific contributions by each participant to show how the ideas evolved or were debated.\n\n" +
                       "Detailed Content Summary: Summarize the interview's content in a structured, cohesive manner, capturing the major topics, arguments, and examples covered throughout. Include any essential terminology, definitions, or background points shared during the conversation to enhance comprehension.\n\n";
            default:
                return "";
        }
    }
}

