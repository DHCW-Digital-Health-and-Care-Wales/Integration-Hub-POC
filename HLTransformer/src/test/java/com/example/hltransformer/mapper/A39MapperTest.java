package com.example.hltransformer.mapper;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v23.datatype.*;
import ca.uhn.hl7v2.model.v23.group.ADT_A39_PATIENT;
import ca.uhn.hl7v2.model.v23.message.ADT_A39;
import com.example.hltransformer.model.MainData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.hltransformer.utils.XmlLoader.loadMainDataObj;
import static org.mockito.Mockito.*;

import ca.uhn.hl7v2.model.v23.segment.*;
import ca.uhn.hl7v2.model.v23.segment.MSH;

import javax.xml.bind.JAXBException;

@ExtendWith(MockitoExtension.class)
public class A39MapperTest {

    @InjectMocks
    private A39Mapper mapper;

    @Mock
    private ADT_A39 adtMessage;

    @Spy
    private A39Mapper mapper2;

    private static MSH mockMsh;
    private static ST stMock;
    private static HD hdMock;
    private static IS isMock;
    private static TS tsMock;
    private static CM_MSG cmMSGmock;
    private static TSComponentOne tsCompoMock1;
    private static ID idMock;
    private static PT ptMock1;
    private static EVN evnMock;
    private static PID pidMock;
    private static PD1 pd1Mock;
    private static MRG  mrgMock;
    private static  ADT_A39_PATIENT patient;
    private static SI siMock;
    private static CX cxMock;
    private static XPN  xpnMock;
    private static XAD xadMock;
    private static XTN xtnMock;
    private static TN tnMock;
    private static XON xonMock;
    private static XCN xcnMock;
    private static CN cnMock;

    @BeforeAll
    public static void init() {
        mockMsh = mock(MSH.class);
        stMock = mock(ST.class);
        hdMock = mock(HD.class);
        isMock = mock(IS.class);
        tsMock = mock(TS.class);
        cmMSGmock = mock(CM_MSG.class);
        tsCompoMock1 = mock(TSComponentOne.class);
        idMock = mock(ID.class);
        ptMock1 = mock(PT.class);
        pidMock = mock(PID.class);
        pd1Mock = mock(PD1.class);
        mrgMock = mock(MRG.class);
        patient= mock(ADT_A39_PATIENT.class);
        siMock = mock(SI.class);
        cxMock = mock(CX.class);
        xpnMock = mock(XPN.class);
        xadMock = mock(XAD.class);
        xtnMock =  mock(XTN.class);
        tnMock =  mock(TN.class);
        xonMock = mock(XON.class);
        xcnMock = mock(XCN.class);
        evnMock = mock(EVN.class);
        cnMock = mock(CN.class);
    }

    @Test
    public void testADT_A39Mapper() throws DataTypeException, JAXBException {
        //Given
        MainData MainDataRecent = loadMainDataObj("Sample_Fullpayload.xml");
        // Mocking the ADT_A39 message
        // Mocking the ADT_A39 message
        setupMSH_Mocks();
        setupPID_Mocks();
        setupPD1_Mock();
        setupMRG_Mock();
        setupEVN_Mock();
        //When
        ADT_A39 result = mapper.ADT_A39Mapper(MainDataRecent);

        assert (result != null);
    }
//    @Test
//    public void testADT_A39Mapper_Null() throws DataTypeException, JAXBException {
//        //Given
//        MainDataRecent MainDataRecent = loadMainDataObj("Sample_Fullpayload.xml");
//        // Mocking the ADT_A39 message
//        setupMSH_Mocks();
//        doReturn(null).when(mapper2).MshMapper(MainDataRecent);
//       doReturn(null).when(mapper2).EVNMapper(MainDataRecent);
//        doReturn(null).when(mapper2).PIDMapper(MainDataRecent);
//        doReturn(null).when(mapper2).PD1Mapper(MainDataRecent);
//        doReturn(null).when(mapper2).MRGMapper(MainDataRecent);
//        //When
//        ADT_A39 result = mapper.ADT_A39Mapper(MainDataRecent);
//
//        assert (result == null);
//    }

    @Test
    public void testPidMapper() throws JAXBException, DataTypeException {
        //Given
        MainData MainDataRecent = loadMainDataObj("Sample_Fullpayload.xml");
        // Mocking the ADT_A39 message
        setupPID_Mocks();
        //When
        ADT_A39 result = mapper.PIDMapper(MainDataRecent);
        assert (result != null);
    }

    @Test
    public void testPD1Mapper() throws JAXBException, DataTypeException {
        //Given
        MainData MainDataRecent = loadMainDataObj("Sample_Fullpayload.xml");
        // Mocking the ADT_A39 message
        setupPD1_Mock();
        //When
        ADT_A39 result = mapper.PD1Mapper(MainDataRecent);
        assert (result != null);
    }

    @Test
    public void testMRGMapper() throws JAXBException, DataTypeException {
        //Given
        MainData MainDataRecent = loadMainDataObj("Sample_Fullpayload.xml");
        // Mocking the ADT_A39 message
        setupMRG_Mock();
        //When
        ADT_A39 result = mapper.MRGMapper(MainDataRecent);
        assert (result != null);
    }

    @Test
    public void testEVNMapper() throws JAXBException, DataTypeException {
        //Given
        MainData MainDataRecent = loadMainDataObj("Sample_Fullpayload.xml");
        // Mocking the ADT_A39 message
        setupEVN_Mock();
        //When
        ADT_A39 result = mapper.EVNMapper(MainDataRecent);
        assert (result != null);
    }

    public void setupMSH_Mocks() {

//        when(adtMessage.getMSH()).thenReturn(mockMsh);
        when(mockMsh.getFieldSeparator()).thenReturn(stMock);
        when(mockMsh.getEncodingCharacters()).thenReturn(stMock);
        when(mockMsh.getMsh3_SendingApplication()).thenReturn(hdMock);
        when(hdMock.getNamespaceID()).thenReturn(isMock);
        when(mockMsh.getMsh4_SendingFacility()).thenReturn(hdMock);
        when(hdMock.getNamespaceID()).thenReturn(isMock);
        when(mockMsh.getReceivingApplication()).thenReturn(hdMock);
        when(hdMock.getNamespaceID()).thenReturn(isMock);
        when(mockMsh.getReceivingFacility()).thenReturn(hdMock);
        when(hdMock.getNamespaceID()).thenReturn(isMock);

        when(mockMsh.getDateTimeOfMessage()).thenReturn(tsMock);
        when(tsMock.getTimeOfAnEvent()).thenReturn(tsCompoMock1);
        when(mockMsh.getMsh9_MessageType()).thenReturn(cmMSGmock);
        when(cmMSGmock.getMessageType()).thenReturn(idMock);
        when(cmMSGmock.getTriggerEvent()).thenReturn(idMock);
        when(mockMsh.getMessageControlID()).thenReturn(stMock);
        when(mockMsh.getProcessingID()).thenReturn(ptMock1);
        when(ptMock1.getProcessingID()).thenReturn(stMock);
        when(mockMsh.getVersionID()).thenReturn(idMock);
        when(mockMsh.getAcceptAcknowledgementType()).thenReturn(idMock);
    }

    private void setupPID_Mocks() {
        when(adtMessage.getPATIENT()).thenReturn(patient);
        when(adtMessage.getPATIENT().getPID()).thenReturn(pidMock);
        when(adtMessage.getPATIENT().getPID().getSetIDPatientID()).thenReturn(siMock);
        when(adtMessage.getPATIENT().getPID().getPatientIDInternalID(0)).thenReturn(cxMock);
        when(adtMessage.getPATIENT().getPID().getPatientIDInternalID(1)).thenReturn(cxMock);
        when(cxMock.getCx3_CodeIdentifyingTheCheckDigitSchemeEmployed()).thenReturn(idMock);
        when(cxMock.getCx4_AssigningAuthority()).thenReturn(hdMock);
        when(cxMock.getCx4_AssigningAuthority().getHd1_NamespaceID()).thenReturn(isMock);
        when(cxMock.getCx5_IdentifierTypeCode()).thenReturn(isMock);

        when(pidMock.getPid5_PatientName(0)).thenReturn(xpnMock);
        when(xpnMock.getXpn1_FamilyName()).thenReturn(stMock);
        when(xpnMock.getXpn2_GivenName()).thenReturn(stMock);
        when(xpnMock.getXpn3_MiddleInitialOrName()).thenReturn(stMock);
        when(xpnMock.getXpn5_PrefixEgDR()).thenReturn(stMock);

        when(pidMock.getPid6_MotherSMaidenName()).thenReturn(xpnMock);
        when(pidMock.getPid7_DateOfBirth()).thenReturn(tsMock);
        when(pidMock.getPid7_DateOfBirth().getTs1_TimeOfAnEvent()).thenReturn(tsCompoMock1);
        when(pidMock.getPid8_Sex()).thenReturn(isMock);
        when(pidMock.getPid9_PatientAlias(0)).thenReturn(xpnMock);
        when(pidMock.getPid9_PatientAlias(0).getXpn1_FamilyName()).thenReturn(stMock);
        when(pidMock.getPid9_PatientAlias(0).getXpn2_GivenName()).thenReturn(stMock);

        when(pidMock.getPid11_PatientAddress(0)).thenReturn(xadMock);
        when(pidMock.getPid11_PatientAddress(1)).thenReturn(xadMock);
        when(xadMock.getXad1_StreetAddress()).thenReturn(stMock);
        when(xadMock.getXad2_OtherDesignation()).thenReturn(stMock);
        when(xadMock.getXad3_City()).thenReturn(stMock);
        when(xadMock.getXad4_StateOrProvince()).thenReturn(stMock);
        when(xadMock.getXad5_ZipOrPostalCode()).thenReturn(stMock);
        when(xadMock.getXad7_AddressType()).thenReturn(idMock);

        when(pidMock.getPid13_PhoneNumberHome(0)).thenReturn(xtnMock);
        when(pidMock.getPid13_PhoneNumberHome(1)).thenReturn(xtnMock);
        when(pidMock.getPid13_PhoneNumberHome(2)).thenReturn(xtnMock);
        when(xtnMock.getXtn1_9999999X99999CAnyText()).thenReturn(tnMock);
        when(xtnMock.getXtn2_TelecommunicationUseCode()).thenReturn(idMock);
        when(xtnMock.getXtn3_TelecommunicationEquipmentType()).thenReturn(idMock);
        when(xtnMock.getXtn3_TelecommunicationEquipmentType()).thenReturn(idMock);
        when(xtnMock.getXtn4_EmailAddress()).thenReturn(stMock);
        when(xtnMock.getXtn9_AnyText()).thenReturn(stMock);

        when(pidMock.getPid16_MaritalStatus(0)).thenReturn(isMock);
        when(pidMock.getPid17_Religion()).thenReturn(isMock);
        when(pidMock.getPid22_EthnicGroup()).thenReturn(isMock);
        when(pidMock.getPid29_PatientDeathDateAndTime()).thenReturn(tsMock);
        when(pidMock.getPid29_PatientDeathDateAndTime().getTs1_TimeOfAnEvent()).thenReturn(tsCompoMock1);
        when(pidMock.getPid30_PatientDeathIndicator()).thenReturn(idMock);
    }

    private void setupPD1_Mock(){
        when(adtMessage.getPATIENT()).thenReturn(patient);
        when(adtMessage.getPATIENT().getPD1()).thenReturn(pd1Mock);
        when(pd1Mock.getPatientPrimaryFacility(0)).thenReturn(xonMock);
        when(pd1Mock.getPatientPrimaryFacility(0).getOrganizationName()).thenReturn(stMock);
        when(pd1Mock.getPatientPrimaryFacility(0).getCheckDigit()).thenReturn(stMock);
        when(pd1Mock.getPatientPrimaryCareProviderNameIDNo(0)).thenReturn(xcnMock);
        when(xcnMock.getXcn1_IDNumber()).thenReturn(stMock);
        when(xcnMock.getXcn2_FamilyName()).thenReturn(stMock);
        when(xcnMock.getXcn3_GivenName()).thenReturn(stMock);
    }

    private void setupMRG_Mock(){
        when(adtMessage.getPATIENT()).thenReturn(patient);
        when(adtMessage.getPATIENT().getMRG()).thenReturn(mrgMock);
        when(mrgMock.getMrg1_PriorPatientIDInternal(1)).thenReturn(cxMock);
        when(cxMock.getCx1_ID()).thenReturn(stMock);
        when(cxMock.getCx4_AssigningAuthority()).thenReturn(hdMock);
        when(cxMock.getCx4_AssigningAuthority().getHd1_NamespaceID()).thenReturn(isMock);
        when(cxMock.getCx5_IdentifierTypeCode()).thenReturn(isMock);
    }

    private void setupEVN_Mock(){
//        when(adtMessage.getEVN()).thenReturn(evnMock);
        when(evnMock.getRecordedDateTime()).thenReturn(tsMock);
        when(tsMock.getTimeOfAnEvent()).thenReturn(tsCompoMock1);
        when(evnMock.getOperatorID()).thenReturn(cnMock);
        when(evnMock.getOperatorID().getIDNumber()).thenReturn(stMock);
        when(evnMock.getEventOccurred()).thenReturn(tsMock);
    }
}
