package com.example.hussainfarooq.digitalquran.OCR;

import com.example.hussainfarooq.digitalquran.OCR.Lines;

public class TextOverlay
{
    private com.example.hussainfarooq.digitalquran.OCR.Lines[] Lines;

    private String Message;

    private String HasOverlay;

    public Lines[] getLines ()
    {
        return Lines;
    }

    public void setLines (Lines[] Lines)
    {
        this.Lines = Lines;
    }

    public String getMessage ()
    {
        return Message;
    }

    public void setMessage (String Message)
    {
        this.Message = Message;
    }

    public String getHasOverlay ()
    {
        return HasOverlay;
    }

    public void setHasOverlay (String HasOverlay)
    {
        this.HasOverlay = HasOverlay;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Lines = "+Lines+", Message = "+Message+", HasOverlay = "+HasOverlay+"]";
    }
}

