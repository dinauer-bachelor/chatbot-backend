package dev.dinauer.rouge;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class Tokenizer
{
    public List<String> tokenize(String text)
    {
        String[] tokens = text.split("\\s+");
        for (int i = 0; i < tokens.length; i++)
        {
            tokens[i] = replace(tokens[i]);
        }
        return Arrays.stream(tokens).filter(item -> !item.isBlank()).toList();
    }

    private String replace(String text)
    {
        List<String> charsToReplace = List.of(",", ".", ":", "!", "?", "(", ")", "]", "[", "{", "}", "/");
        for (String character : charsToReplace)
        {
            text = text.replace(character, "");
        }
        return text.toLowerCase();
    }
}
