package dev.dinauer.rouge;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class NGramSplitterTest
{
    @Inject
    NGramSplitter nGramSplitter;

    @Test
    void testSplit()
    {
        // given
        String text = "Die Katze ist dick und doof und frisst den ganzen Tag.";

        //when
        List<NGram> unigrams = nGramSplitter.split(text, 1);
        List<NGram> bigrams = nGramSplitter.split(text, 2);
        List<NGram> trigrams = nGramSplitter.split(text, 3);

        //then
        Assertions.assertEquals(11, unigrams.size());
        Assertions.assertEquals(List.of("die"), unigrams.getFirst().getSequence());
        Assertions.assertEquals(List.of("ist"), unigrams.get(2).getSequence());

        Assertions.assertEquals(10, bigrams.size());
        Assertions.assertEquals(List.of("die", "katze"), bigrams.getFirst().getSequence());
        Assertions.assertEquals(List.of("ist", "dick"), bigrams.get(2).getSequence());

        Assertions.assertEquals(9, trigrams.size());
        Assertions.assertEquals(List.of("die", "katze", "ist"), trigrams.getFirst().getSequence());
        Assertions.assertEquals(List.of("katze", "ist", "dick"), trigrams.get(1).getSequence());
        Assertions.assertEquals(List.of("den", "ganzen", "tag"), trigrams.get(8).getSequence());
    }

    @Test
    void testEmptySplit()
    {
        // given
        String text = "";

        //when
        List<NGram> unigrams = nGramSplitter.split(text, 1);

        //then
        Assertions.assertEquals(0, unigrams.size());
    }
}
