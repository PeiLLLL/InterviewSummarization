package data8319.service;

import data8319.model.InterviewPojo;

public class SummaryGenerator {
    private final ChatGptService chatGptService;
    
    public SummaryGenerator(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    public String generateSummary(InterviewPojo interview, String promptTemplate) throws Exception {
        String prompt = String.format(promptTemplate, interview.getContent());
        return chatGptService.generateSummary(prompt);
    }
    public class SummaryFormatter {
        public static String formatSummary(String summary) {
            return summary != null ? summary.replace("\n", "<br>") : "";
        }
    } 
}
