package org.acme.rouge;

import java.util.LinkedList;
import java.util.List;

public class NGramSplitter
{
    private final Integer nGram;

    public NGramSplitter(Integer nGram)
    {
        this.nGram = nGram;
    }

    public List<NGram> split(String text)
    {
        List<NGram> result = new LinkedList<>();
        List<String> tokens = Tokenizer.tokenize(text);
        for(int i = 0; i < tokens.size() - nGram + 1; i++)
        {
            List<String> ngram = new LinkedList<>();
            for(int u = 0; u < nGram; u++)
            {
                ngram.add(tokens.get(i + u));
            }
            result.add(new NGram(ngram));
        }
        return result;
    }
}
