package com.example.hussainfarooq.digitalquran.OCR;


public class Words
{
    private String Top;

    private String Height;

    private String Width;

    private String WordText;

    private String Left;

    public String getTop ()
    {
        return Top;
    }

    public void setTop (String Top)
    {
        this.Top = Top;
    }

    public String getHeight ()
    {
        return Height;
    }

    public void setHeight (String Height)
    {
        this.Height = Height;
    }

    public String getWidth ()
    {
        return Width;
    }

    public void setWidth (String Width)
    {
        this.Width = Width;
    }

    public String getWordText ()
    {
        return WordText;
    }

    public void setWordText (String WordText)
    {
        this.WordText = WordText;
    }

    public String getLeft ()
    {
        return Left;
    }

    public void setLeft (String Left)
    {
        this.Left = Left;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Top = "+Top+", Height = "+Height+", Width = "+Width+", WordText = "+WordText+", Left = "+Left+"]";
    }
}