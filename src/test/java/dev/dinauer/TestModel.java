package dev.dinauer;

import dev.dinauer.rouge.RougeCalculator;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusTest
public class TestModel
{
    private static final Logger LOG = LoggerFactory.getLogger(TestModel.class);

    @Inject
    ClaraBot claraBot;

    @Test
    @Disabled
    void test_01()
    {
        // given
        String prompt = "Hello Clara, can you tell me about our projects?";

        String reference = """
                Hello,
                            
                currently we have 5 projects in our company. Those are:
                1. DR5: This project is about problems and incidents we collect during production of the DR5 car.
                2. DR4: This project centers around problems we face during production of the DR4 car.
                3. SCM: THis project is about optimization of iur supply chain processes which includes procurement, storage and transportation.
                4. FMA: This project coordinates infrastructural tasks regarding our facility at the Augsburg site.
                5. FMN: This project is responsible for maintenance of our buildings and production plant located in Nuremberg.
                            
                These are our projects. If you want to know more about it, please ask me.
                """;

        String horribleReference = """
                Hello,
                            
                unfortunately I cannot help you with our projects. I need more information. I can assist you with projects and issues.
                """;

        // when
        String generatedAnswer = claraBot.get().chat(prompt);
        double rouge1 = new RougeCalculator().calc(reference, generatedAnswer, 1);
        double rouge2 = new RougeCalculator().calc(reference, generatedAnswer, 2);
        double rouge3 = new RougeCalculator().calc(reference, generatedAnswer, 3);

        double badRouge1 = new RougeCalculator().calc(horribleReference, generatedAnswer, 1);
        double badRouge2 = new RougeCalculator().calc(horribleReference, generatedAnswer, 2);
        double badRouge3 = new RougeCalculator().calc(horribleReference, generatedAnswer, 3);

        // then
        LOG.info("Ratings: [Rouge-1: {}, Rouge-2: {}, Rouge-3: {}]", rouge1, rouge2, rouge3);
        LOG.info("Bad Ratings: [Rouge-1: {}, Rouge-2: {}, Rouge-3: {}]", badRouge1, badRouge2, badRouge3);
    }

    @Test
    @Disabled
    void test_02()
    {
        // given
        String prompt = "Hello Clara, can you tell me more about the problem with the delayed delivery of the brake components?";

        String reference = """
                Hello,
                            
                there was indeed a problem with delivering brake components from Supplier X with key SCM-1. It is marked a bug. It looks like they were delayed by 5 days.
                It's expected to impact the production line and we expect an impact on production timeline for our vehicle batches planned for next week.
                This was caused by customs clearance issues. The issue is marked as Open.
                            
                Tell me if you want to know more about this.
                """;

        String horribleReference = """
                Hello,
                            
                I was not able to find specific issues about your request related to 'break components'. If you meant something specific, please provide more details so I can assist you further.
                """;

        // when
        String generatedAnswer = claraBot.get().chat(prompt);
        double rouge1 = new RougeCalculator().calc(reference, generatedAnswer, 1);
        double rouge2 = new RougeCalculator().calc(reference, generatedAnswer, 2);
        double rouge3 = new RougeCalculator().calc(reference, generatedAnswer, 3);

        double badRouge1 = new RougeCalculator().calc(horribleReference, generatedAnswer, 1);
        double badRouge2 = new RougeCalculator().calc(horribleReference, generatedAnswer, 2);
        double badRouge3 = new RougeCalculator().calc(horribleReference, generatedAnswer, 3);

        // then
        LOG.info("Ratings: [Rouge-1: {}, Rouge-2: {}, Rouge-3: {}]", rouge1, rouge2, rouge3);
        LOG.info("Bad Ratings: [Rouge-1: {}, Rouge-2: {}, Rouge-3: {}]", badRouge1, badRouge2, badRouge3);
    }
}
