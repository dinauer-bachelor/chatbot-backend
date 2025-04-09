package org.acme.rouge;

import java.util.Arrays;
import java.util.List;

public class Tokenizer
{
    private static final List<String> STOP_WORDS = List.of("the", "with", "has", "a", "or", "and", "to", "in", "on", "into", "onto", "am", "are", "is");

    public static List<String> tokenize(String text)
    {
        String[] tokens = text.split("\\s+");
        for(int i = 0; i < tokens.length; i++)
        {
            tokens[i] = replace(tokens[i]);
        }
        return Arrays.stream(tokens).filter(item -> !item.isBlank() && !STOP_WORDS.contains(item)).toList();
    }

    private static String replace(String text)
    {
        List<String> charsToReplace = List.of(",", ".", ":", "!", "?", "(", ")", "]", "[", "{", "}", "/");
        for(String character : charsToReplace)
        {
            text = text.replace(character, "");
        }
        return text.toLowerCase();
    }
}
