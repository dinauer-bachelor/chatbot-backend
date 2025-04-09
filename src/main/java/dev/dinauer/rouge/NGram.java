package dev.dinauer.rouge;

import java.util.List;

public class NGram
{
    private final int gramCount;
    private final List<String> sequence;

    public NGram(List<String> sequence)
    {
        this.gramCount = sequence.size();
        this.sequence = sequence;
    }

    public int getGramCount()
    {
        return gramCount;
    }

    public List<String> getSequence()
    {
        return sequence;
    }

    public boolean equals(Object object)
    {
        if (object instanceof NGram other)
        {
            if (this.gramCount == other.getGramCount())
            {
                return sequence.equals(other.getSequence());
            }
        }
        return false;
    }
}
