package org.acme.rouge;

import org.acme.rouge.NGram;
import org.acme.rouge.NGramSplitter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NGramSplitterTest
{
    @Test
    void testSplit()
    {
        // given
        String text = "Die Katze ist dick und doof und frisst den ganzen Tag.";

        //when
        List<NGram> unigrams = new NGramSplitter(1).split(text);
        List<NGram> twoNGrams = new NGramSplitter(2).split(text);
        List<NGram> threeNGrams = new NGramSplitter(3).split(text);

        //then
        Assertions.assertEquals(11, unigrams.size());
        Assertions.assertEquals(List.of("die"), unigrams.getFirst().getSequence());
        Assertions.assertEquals(List.of("ist"), unigrams.get(2).getSequence());

        Assertions.assertEquals(10, twoNGrams.size());
        Assertions.assertEquals(List.of("die", "katze"), twoNGrams.getFirst().getSequence());
        Assertions.assertEquals(List.of("ist", "dick"), twoNGrams.get(2).getSequence());

        Assertions.assertEquals(9, threeNGrams.size());
        Assertions.assertEquals(List.of("die", "katze", "ist"), threeNGrams.getFirst().getSequence());
        Assertions.assertEquals(List.of("katze", "ist", "dick"), threeNGrams.get(1).getSequence());
        Assertions.assertEquals(List.of("den", "ganzen", "tag"), threeNGrams.get(8).getSequence());
    }

    @Test
    void testEmptySplit()
    {
        // given
        String text = "";

        //when
        List<NGram> unigrams = new NGramSplitter(1).split(text);

        //then
        Assertions.assertEquals(0, unigrams.size());
    }
}
