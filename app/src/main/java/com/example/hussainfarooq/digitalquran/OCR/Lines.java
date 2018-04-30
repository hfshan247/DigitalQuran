package com.example.hussainfarooq.digitalquran.OCR;


public class Lines
{
    private String MaxHeight;

    private String MinTop;

    private com.example.hussainfarooq.digitalquran.OCR.Words[] Words;

    public String getMaxHeight ()
    {
        return MaxHeight;
    }

    public void setMaxHeight (String MaxHeight)
    {
        this.MaxHeight = MaxHeight;
    }

    public String getMinTop ()
    {
        return MinTop;
    }

    public void setMinTop (String MinTop)
    {
        this.MinTop = MinTop;
    }

    public Words[] getWords ()
    {
        return Words;
    }

    public void setWords (Words[] Words)
    {
        this.Words = Words;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [MaxHeight = "+MaxHeight+", MinTop = "+MinTop+", Words = "+Words+"]";
    }
}

