package dev.dinauer.rouge;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class RougeCalculator
{
    @Inject
    NGramSplitter nGramSplitter;

    @Inject
    Tokenizer tokenizer;

    public double calc(String reference, String candidate, Integer nGram)
    {
        List<NGram> referenceNGram = nGramSplitter.split(reference, nGram);
        List<NGram> candidateNGram = nGramSplitter.split(candidate, nGram);

        return countOverlappingNGrams(referenceNGram, candidateNGram) / (referenceNGram.size() * 1.0);
    }

    protected int countOverlappingNGrams(List<NGram> reference, List<NGram> candidate)
    {
        int nGramsInBoth = 0;

        for (NGram ngram : reference)
        {
            if (candidate.contains(ngram))
            {
                nGramsInBoth++;
            }
        }

        return nGramsInBoth;
    }
}
