package com.example.hltransformer.mapper;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v23.message.ADT_A39;
import com.example.hltransformer.model.MainData;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class A39Mapper {

    private final ADT_A39 adtMessage;

    public A39Mapper() {
        adtMessage = new ADT_A39();
    }

    public ADT_A39 ADT_A39Mapper(MainData mainDataObj) throws DataTypeException {
        if(MshMapper(mainDataObj)!=null) {
            if(EVNMapper(mainDataObj)!=null) {
                if(PIDMapper(mainDataObj)!=null){
                    if(PD1Mapper(mainDataObj)!=null){
                        if(MRGMapper(mainDataObj)!=null){

                        }
                    }
                }

            }
        }else{
            return null;
        }
        return adtMessage;
    }

    public ADT_A39 MshMapper(MainData mainDataObj) throws DataTypeException {

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            // Mapping header
            adtMessage.getMSH().getFieldSeparator().setValue("|");
            adtMessage.getMSH().getEncodingCharacters().setValue("^~\\&");
            adtMessage.getMSH().getMsh3_SendingApplication().getNamespaceID().setValue(mainDataObj.getTransaction().getSystemId());
            adtMessage.getMSH().getMsh4_SendingFacility().getNamespaceID().setValue(mainDataObj.getTransaction().getDhaCode());
            adtMessage.getMSH().getReceivingApplication().getNamespaceID().setValue("200");
            adtMessage.getMSH().getReceivingFacility().getNamespaceID().setValue("200");

            adtMessage.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().setValue(sdf.format(now)); //yyyyMMddHHmmss
            adtMessage.getMSH().getMsh9_MessageType().getMessageType().setValue("ADT");
            adtMessage.getMSH().getMsh9_MessageType().getTriggerEvent().setValue("A40");
//        adtMessage.getMSH().getMessageType().getExtraComponents().
            adtMessage.getMSH().getMessageControlID().setValue(mainDataObj.getTransaction().getSystemId() + mainDataObj.getTransaction().getTransactionId());
            adtMessage.getMSH().getProcessingID().getProcessingID().setValue("P");
            adtMessage.getMSH().getVersionID().setValue("2.5.1");
            adtMessage.getMSH().getAcceptAcknowledgementType().setValue("AL");
            return adtMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ADT_A39 EVNMapper(MainData mainDataObj) throws DataTypeException {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            adtMessage.getEVN().getRecordedDateTime().getTimeOfAnEvent().setValue(sdf.format(now));
            adtMessage.getEVN().getOperatorID().getIDNumber().setValue(mainDataObj.getTransaction().getUserId());
            adtMessage.getEVN().getEventOccurred().getTimeOfAnEvent().setValue(sdf.format(now));

            return adtMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ADT_A39 PIDMapper(MainData mainDataObj) throws DataTypeException {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        try {
            adtMessage.getPATIENT().getPID().getSetIDPatientID().setValue("1");
            adtMessage.getPATIENT().getPID().getPatientIDInternalID(0).getCx3_CodeIdentifyingTheCheckDigitSchemeEmployed().setValue(mainDataObj.getTransaction().getNhsNumber());
            adtMessage.getPATIENT().getPID().getPatientIDInternalID(0).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("NHS");
            adtMessage.getPATIENT().getPID().getPatientIDInternalID(0).getCx5_IdentifierTypeCode().setValue("NH");
            adtMessage.getPATIENT().getPID().getPatientIDInternalID(1).getCx3_CodeIdentifyingTheCheckDigitSchemeEmployed().setValue(mainDataObj.getTransaction().getUnitNumber());
            adtMessage.getPATIENT().getPID().getPatientIDInternalID(1).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue(mainDataObj.getTransaction().getSystemId());
            adtMessage.getPATIENT().getPID().getPatientIDInternalID(1).getCx5_IdentifierTypeCode().setValue("PI");

            adtMessage.getPATIENT().getPID().getPid5_PatientName(0).getXpn1_FamilyName().setValue(mainDataObj.getTransaction().getSurname());
            adtMessage.getPATIENT().getPID().getPid5_PatientName(0).getXpn2_GivenName().setValue(mainDataObj.getTransaction().getForename());
            adtMessage.getPATIENT().getPID().getPid5_PatientName(0).getXpn3_MiddleInitialOrName().setValue(null);
            adtMessage.getPATIENT().getPID().getPid5_PatientName(0).getXpn5_PrefixEgDR().setValue(mainDataObj.getTransaction().getTitle());

            adtMessage.getPATIENT().getPID().getPid6_MotherSMaidenName().getXpn1_FamilyName().setValue(null);
            LocalDate date = LocalDate.parse(mainDataObj.getTransaction().getBirthdate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String outputDate = date.format(outputFormatter);
            adtMessage.getPATIENT().getPID().getPid7_DateOfBirth().getTs1_TimeOfAnEvent().setValue(outputDate);//yyyymmdd
            adtMessage.getPATIENT().getPID().getPid8_Sex().setValue(mainDataObj.getTransaction().getGender());
            adtMessage.getPATIENT().getPID().getPid9_PatientAlias(0).getXpn1_FamilyName().setValue(null);
            adtMessage.getPATIENT().getPID().getPid9_PatientAlias(0).getXpn2_GivenName().setValue(null);

            //11
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(0).getXad1_StreetAddress().setValue(mainDataObj.getTransaction().getAddress1());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(0).getXad2_OtherDesignation().setValue(mainDataObj.getTransaction().getAddress2());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(0).getXad3_City().setValue(mainDataObj.getTransaction().getAddress3());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(0).getXad4_StateOrProvince().setValue(mainDataObj.getTransaction().getAddress4());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(0).getXad5_ZipOrPostalCode().setValue(mainDataObj.getTransaction().getPostcode());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(0).getXad7_AddressType().setValue("H");
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(1).getXad1_StreetAddress().setValue(mainDataObj.getTransaction().getContactAddress1());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(1).getXad2_OtherDesignation().setValue(mainDataObj.getTransaction().getContactAddress2());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(1).getXad3_City().setValue(mainDataObj.getTransaction().getContactAddress3());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(1).getXad4_StateOrProvince().setValue(mainDataObj.getTransaction().getContactAddress4());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(1).getXad5_ZipOrPostalCode().setValue(mainDataObj.getTransaction().getContactAddress6());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(1).getXad7_AddressType().setValue("M");

            //13
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(0).getXtn1_9999999X99999CAnyText().setValue(mainDataObj.getTransaction().getTelephoneDay());
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(0).getXtn2_TelecommunicationUseCode().setValue("PRN");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(0).getXtn3_TelecommunicationEquipmentType().setValue("PH");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(0).getXtn4_EmailAddress().setValue(mainDataObj.getTransaction().getEmail());
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(0).getXtn9_AnyText().setValue("DAY");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(1).getXtn1_9999999X99999CAnyText().setValue(mainDataObj.getTransaction().getTelephoneNight());
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(1).getXtn2_TelecommunicationUseCode().setValue("PRN");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(1).getXtn3_TelecommunicationEquipmentType().setValue("PH");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(1).getXtn9_AnyText().setValue("NIGHT");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(2).getXtn1_9999999X99999CAnyText().setValue(mainDataObj.getTransaction().getMobile());
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(2).getXtn2_TelecommunicationUseCode().setValue("PRS");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(2).getXtn3_TelecommunicationEquipmentType().setValue("CP");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(2).getXtn9_AnyText().setValue("Mobile");

            adtMessage.getPATIENT().getPID().getPid16_MaritalStatus(0).setValue(mainDataObj.getTransaction().getMaritalStatus().toString());
            adtMessage.getPATIENT().getPID().getPid17_Religion().setValue(mainDataObj.getTransaction().getReligionStatus());
            adtMessage.getPATIENT().getPID().getPid22_EthnicGroup().setValue(mainDataObj.getTransaction().getEthnicOrigin());
            if(mainDataObj.getTransaction().getDeathdate()!=null&& !mainDataObj.getTransaction().getDeathdate().isBlank()){
                LocalDate date1 = LocalDate.parse(mainDataObj.getTransaction().getBirthdate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String deathDate = date.format(outputFormatter);
                adtMessage.getPATIENT().getPID().getPid29_PatientDeathDateAndTime().getTs1_TimeOfAnEvent().setValue(deathDate);
                adtMessage.getPATIENT().getPID().getPid30_PatientDeathIndicator().setValue("Y");
            }else{
                adtMessage.getPATIENT().getPID().getPid29_PatientDeathDateAndTime().getTs1_TimeOfAnEvent().setValue(mainDataObj.getTransaction().getDeathdate()); //yyyyMMdd
                adtMessage.getPATIENT().getPID().getPid30_PatientDeathIndicator().setValue("N");
            }
            // pid 32 and pid 33.1 not avalible

            return adtMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ADT_A39 PD1Mapper(MainData mainDataObj) throws DataTypeException {
        try {
            adtMessage.getPATIENT().getPD1().getPatientPrimaryFacility(0).getOrganizationName().setValue(mainDataObj.getTransaction().getGpAddr1());
            adtMessage.getPATIENT().getPD1().getPatientPrimaryFacility(0).getCheckDigit().setValue(mainDataObj.getTransaction().getGpPractice());
            adtMessage.getPATIENT().getPD1().getPatientPrimaryCareProviderNameIDNo(0).getXcn1_IDNumber().setValue(mainDataObj.getTransaction().getRegisteredGp());
            adtMessage.getPATIENT().getPD1().getPatientPrimaryCareProviderNameIDNo(0).getXcn2_FamilyName().setValue(mainDataObj.getTransaction().getGpSurname());
            adtMessage.getPATIENT().getPD1().getPatientPrimaryCareProviderNameIDNo(0).getXcn3_GivenName().setValue(mainDataObj.getTransaction().getGpInits());

            return adtMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ADT_A39 MRGMapper(MainData mainDataObj) throws DataTypeException {
        try {
            adtMessage.getPATIENT().getMRG().getMrg1_PriorPatientIDInternal(0).getCx1_ID().setValue(mainDataObj.getTransaction().getOldUnitNumber().toString());
            adtMessage.getPATIENT().getMRG().getMrg1_PriorPatientIDInternal(0).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue(mainDataObj.getTransaction().getSystemId());
            adtMessage.getPATIENT().getMRG().getMrg1_PriorPatientIDInternal(0).getCx5_IdentifierTypeCode().setValue("PI");
            return adtMessage;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
