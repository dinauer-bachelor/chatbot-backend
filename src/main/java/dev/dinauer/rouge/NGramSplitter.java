package dev.dinauer.rouge;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;

import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class NGramSplitter
{
    @Inject
    StopwordRemover stopwordRemover;

    @Inject
    Tokenizer tokenizer;

    public List<NGram> split(String text, int nGram)
    {
        List<NGram> result = new LinkedList<>();
        List<String> tokens = tokenizer.tokenize(text);
        List<String> tokensWithoutStopwords = stopwordRemover.remove(tokens, getLanguage(text));
        for (int i = 0; i < tokensWithoutStopwords.size() - nGram + 1; i++)
        {
            List<String> ngram = new LinkedList<>();
            for (int u = 0; u < nGram; u++)
            {
                ngram.add(tokensWithoutStopwords.get(i + u));
            }
            result.add(new NGram(ngram));
        }
        return result;
    }

    private String getLanguage(String text)
    {
        LanguageDetector languageDetector = new OptimaizeLangDetector().loadModels();
        return languageDetector.detect(text).getLanguage();
    }
}
