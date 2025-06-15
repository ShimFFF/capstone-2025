package naeilmolae.global.templates;


import org.springframework.stereotype.Component;

@Component
public class PromptManager {

    private final String CheckForOffensiveLanguagePrompt = "You are an assistant that checks whether a sentence contains offensive " +
            "language. Respond only with 'true' if the sentence contains offensive language and 'false' otherwise.";


    public String createPrompt(String request, String responseFormat) {
        PromptTemplate template = new PromptTemplate();
        return template.fillTemplate(request, responseFormat);
    }

    //부적절하다면 문장 작성자에게 왜 부적절한지 높임말로 reason에 간결히 작성해줘. reason 이 없어도 null로 채워줘.
    public String createCheckForOffensiveLanguagePrompt( String situation, String statement ) {
        PromptTemplate template = new PromptTemplate();
        return template.fillTemplate(
                """
               ## 명령 
               주어진 문장이 특정 상황에 처한 사람에게 적절한 응원이나 표현인지 판단해줘.
               **상황과 약간이라도 관련 있으면** 적절한 응원으로 간주해.
               적절한 응원이라면, 아래 형식으로 응답해:
               {"is_proper": true, "reason": null}

               **문장이 상황과 다소 다르지만 긍정적인 의미라면**, 부적절하지 않은 것으로 판단해.  
               그래도 다소 어긋난 경우 아래 형식으로 응답해:
               {"is_proper": false, "reason": 0}

               **문장에 욕설이나 듣기 거북한 표현이 포함되어 있다면**, 아래 형식으로 응답해:
               {"is_proper": false, "reason": 1}

               ## 상황
               '%s' 

               ## 문장
               '%s' 

               """.formatted(situation, statement),
                """
                {"is_proper":<boolean>, "reason": <integer or null>}
                """
        );
    }

    public String createCheckForOffensiveLanguagePrompt2(String stat1, String stat2) {
        PromptTemplate template = new PromptTemplate();
        return template.fillTemplate(
                """
                ## 명령 
                문장2가 문장1을 따라 읽었는지 판단해줘. 문장2는 STT를 통해 변환된 문장이므로, 약간의 오차가 있을 수 있지만 발음을 고려하여 비교해야 해.
                문장2가 문장1을 올바르게 따라 읽었다면, 아래 형식으로 응답해:
                {"is_proper": true, "reason": null}
                문장2가 문장1을 정확히 따라 읽지 않았다면, 아래 형식으로 응답해:
                {"is_proper": false, "reason": 0}
                문장2에 욕설이나 불쾌한 표현이 포함되어 있다면, 아래 형식으로 응답해:
                {"is_proper": false, "reason": 1}
                ## 문장1
                \'%s\' 
                ## 문장2
                \'%s\' 
                """.formatted(stat1, stat2),
                """
                {"is_proper":<boolean>, "reason": <integer or null>}
                """
        );
    }

}
