package com.example.hussainfarooq.digitalquran.OCR;

import com.example.hussainfarooq.digitalquran.OCR.ParsedResults;

public class Rootobject
{
    private com.example.hussainfarooq.digitalquran.OCR.ParsedResults[] ParsedResults;

    private String IsErroredOnProcessing;

    private String ErrorMessage;

    private String ProcessingTimeInMilliseconds;

    private String SearchablePDFURL;

    private String OCRExitCode;

    private String ErrorDetails;

    public ParsedResults[] getParsedResults ()
    {
        return ParsedResults;
    }

    public void setParsedResults (ParsedResults[] ParsedResults)
    {
        this.ParsedResults = ParsedResults;
    }

    public String getIsErroredOnProcessing ()
    {
        return IsErroredOnProcessing;
    }

    public void setIsErroredOnProcessing (String IsErroredOnProcessing)
    {
        this.IsErroredOnProcessing = IsErroredOnProcessing;
    }

    public String getErrorMessage ()
{
    return ErrorMessage;
}

    public void setErrorMessage (String ErrorMessage)
    {
        this.ErrorMessage = ErrorMessage;
    }

    public String getProcessingTimeInMilliseconds ()
    {
        return ProcessingTimeInMilliseconds;
    }

    public void setProcessingTimeInMilliseconds (String ProcessingTimeInMilliseconds)
    {
        this.ProcessingTimeInMilliseconds = ProcessingTimeInMilliseconds;
    }

    public String getSearchablePDFURL ()
    {
        return SearchablePDFURL;
    }

    public void setSearchablePDFURL (String SearchablePDFURL)
    {
        this.SearchablePDFURL = SearchablePDFURL;
    }

    public String getOCRExitCode ()
    {
        return OCRExitCode;
    }

    public void setOCRExitCode (String OCRExitCode)
    {
        this.OCRExitCode = OCRExitCode;
    }

    public String getErrorDetails ()
{
    return ErrorDetails;
}

    public void setErrorDetails (String ErrorDetails)
    {
        this.ErrorDetails = ErrorDetails;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ParsedResults = "+ParsedResults+", IsErroredOnProcessing = "+IsErroredOnProcessing+", ErrorMessage = "+ErrorMessage+", ProcessingTimeInMilliseconds = "+ProcessingTimeInMilliseconds+", SearchablePDFURL = "+SearchablePDFURL+", OCRExitCode = "+OCRExitCode+", ErrorDetails = "+ErrorDetails+"]";
    }
}

