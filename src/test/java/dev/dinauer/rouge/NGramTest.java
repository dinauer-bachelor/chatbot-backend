package dev.dinauer.rouge;

import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import java.util.List;

public class NGramTest
{
    @Test
    void testNGramEquals()
    {
        // given
        NGram ngram1 = new NGram(List.of("Katze", "dick"));
        NGram ngram2 = new NGram(List.of("Katze", "dick"));

        // then
        Assert.assertTrue(ngram1.equals(ngram2));
    }

    @Test
    void testNGramNotEquals()
    {
        // given
        NGram ngram1 = new NGram(List.of("Katze", "dick"));
        NGram ngram2 = new NGram(List.of("Hund", "dick"));
        NGram ngram3 = new NGram(List.of("dick", "Katze"));
        NGram ngram4 = new NGram(List.of("Katze", "dick", "ist"));

        // then
        Assert.assertFalse(ngram1.equals(ngram2));
        Assert.assertFalse(ngram1.equals(ngram3));
        Assert.assertFalse(ngram1.equals(ngram4));
    }
}
