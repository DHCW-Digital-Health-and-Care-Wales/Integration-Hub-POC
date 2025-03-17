package wales.nhs.dhcw.inthub.wpasHl7;

import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

import java.util.Optional;

public class Utility {

 public  static Optional<String> getNHSNumber(MAINDATA.TRANSACTION transaction){
     if(transaction.getNHSNUMBER()!=null){
         return Optional.of(transaction.getNHSNUMBER());
     }else{
         return Optional.empty();
     }
 }

    public  static Optional<String> getUNITNumber(MAINDATA.TRANSACTION transaction){
        if(transaction.getUNITNUMBER()!=null){
            return Optional.of(transaction.getUNITNUMBER());
        }else{
            return Optional.empty();
        }
    }

    public  static Optional<String> getTELEPHONEDAY(MAINDATA.TRANSACTION transaction){
        if(transaction.getTELEPHONEDAY()!=null){
            return Optional.of(transaction.getTELEPHONEDAY());
        }else{
            return Optional.empty();
        }
    }

    public  static Optional<String> getMOBILE(MAINDATA.TRANSACTION transaction){
        if(transaction.getMOBILE()!=null){
            return Optional.of(transaction.getMOBILE());
        }else{
            return Optional.empty();
        }
    }
}
