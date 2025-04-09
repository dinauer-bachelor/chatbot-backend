package dev.dinauer.rouge;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class RougeCalculatorTest
{
    @Inject
    RougeCalculator rougeCalculator;

    @Test
    void testOverlappingNGrams_01()
    {
        // given
        List<NGram> reference = List.of(
                new NGram(List.of("die", "katze")),
                new NGram(List.of("katze", "ist")),
                new NGram(List.of("ist", "doof"))
        );
        List<NGram> candidate = List.of(
                new NGram(List.of("die", "katze")),
                new NGram(List.of("katze", "ist")),
                new NGram(List.of("ist", "schlau"))
        );

        // when
        int overlappingNGrams = rougeCalculator.countOverlappingNGrams(reference, candidate);

        // then
        assertEquals(2, overlappingNGrams);
    }

    @Test
    void testOverlappingNGrams_02()
    {
        // given
        List<NGram> reference = List.of(
                new NGram(List.of("die", "katze")),
                new NGram(List.of("katze", "ist")),
                new NGram(List.of("ist", "doof"))
        );
        List<NGram> candidate = List.of(
                new NGram(List.of("der", "hund")),
                new NGram(List.of("hund", "ist")),
                new NGram(List.of("ist", "doof"))
        );

        // when
        int overlappingNGrams = rougeCalculator.countOverlappingNGrams(reference, candidate);

        // then
        assertEquals(1, overlappingNGrams);
    }

    @Test
    void testRouge1()
    {
        // given
        String candidate = "Die Katze schläft auf dem Sofa.";
        String reference = "Die Katze liegt auf dem Sofa.";

        // when
        double rouge1 = rougeCalculator.calc(reference, candidate, 1);
        double rouge2 = rougeCalculator.calc(reference, candidate, 2);

        // then
        assertEquals(0.66, rouge1, 0.01);
        assertEquals(0, rouge2, 0);
    }
}
