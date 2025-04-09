package org.acme.rouge;

import org.acme.rouge.RougeCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RougeCalculatorTest
{
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
        int overlappingNGrams = new RougeCalculator().countOverlappingNGrams(reference, candidate);

        // then
        Assertions.assertEquals(2, overlappingNGrams);
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
        int overlappingNGrams = new RougeCalculator().countOverlappingNGrams(reference, candidate);

        // then
        Assertions.assertEquals(1, overlappingNGrams);
    }

    @Test
    void testRouge1()
    {
        // given
        String candidate = "Die Katze schläft auf dem Sofa.";
        String reference = "Die Katze liegt auf dem Sofa.";

        // when
        double rouge1 = new RougeCalculator().calc(reference, candidate, 1);
        double rouge2 = new RougeCalculator().calc(reference, candidate, 2);

        // then
        Assertions.assertEquals(0.83, rouge1, 0.01);
        Assertions.assertEquals(0.6, rouge2, 0.01);
    }
}
