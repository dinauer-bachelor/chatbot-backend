package org.acme.rouge;

import java.util.*;

public class RougeCalculator
{
    public double calc(String reference, String candidate, Integer nGram)
    {
        NGramSplitter nGramSplitter = new NGramSplitter(nGram);
        List<NGram> referenceNGram = nGramSplitter.split(reference);
        List<NGram> candidateNGram = nGramSplitter.split(candidate);

        return countOverlappingNGrams(referenceNGram, candidateNGram) / (referenceNGram.size() * 1.0);
    }

    protected int countOverlappingNGrams(List<NGram> reference, List<NGram> candidate)
    {
        int nGramsInBoth = 0;

        for(NGram ngram : reference)
        {
            if(candidate.contains(ngram))
            {
                nGramsInBoth++;
            }
        }

        return nGramsInBoth;
    }
}
