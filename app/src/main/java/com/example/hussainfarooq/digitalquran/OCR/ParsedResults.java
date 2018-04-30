package com.example.hussainfarooq.digitalquran.OCR;


public class ParsedResults
{
   private String ParsedText;

   private String FileParseExitCode;

   private String ErrorMessage;

   private com.example.hussainfarooq.digitalquran.OCR.TextOverlay TextOverlay;

   private String ErrorDetails;

   public String getParsedText ()
   {
      return ParsedText;
   }

   public void setParsedText (String ParsedText)
   {
      this.ParsedText = ParsedText;
   }

   public String getFileParseExitCode ()
   {
      return FileParseExitCode;
   }

   public void setFileParseExitCode (String FileParseExitCode)
   {
      this.FileParseExitCode = FileParseExitCode;
   }

   public String getErrorMessage ()
   {
      return ErrorMessage;
   }

   public void setErrorMessage (String ErrorMessage)
   {
      this.ErrorMessage = ErrorMessage;
   }

   public TextOverlay getTextOverlay ()
   {
      return TextOverlay;
   }

   public void setTextOverlay (TextOverlay TextOverlay)
   {
      this.TextOverlay = TextOverlay;
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
      return "ClassPojo [ParsedText = "+ParsedText+", FileParseExitCode = "+FileParseExitCode+", ErrorMessage = "+ErrorMessage+", TextOverlay = "+TextOverlay+", ErrorDetails = "+ErrorDetails+"]";
   }
}
