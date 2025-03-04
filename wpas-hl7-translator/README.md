# WPAS HL7 Translator

## JAXB binding

Java classes were generated from [WPAS schema](./src/main/resources/xsd/WPAS_Schema.xsd) file with Jakarta xjc tool.

To regenerate them:

1. download Jakarta distribution from https://eclipse-ee4j.github.io/jaxb-ri/ 
2. Run the xjc tool: 
```shell
jaxb-ri/bin/xjc.sh -p wales.nhs.dhcw.inthub.wpasHl7.xml -mark-generated ./WPAS_Schema.xsd
```
3. Copy generated classes to the proper source folder.