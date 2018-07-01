package basic;

import com.fqyang.AddressBookProtos;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class Demo1 {

    public static void main(String args[]){
        //ConstructAndSerializeToLocalFile();
        //DeserializeFromFile();

        FromAndToByteArray();
        System.out.println("done");
    }

    //construct a Person object, and then serialize it.
    private static void ConstructAndSerializeToLocalFile(){
        AddressBookProtos.Person.Builder person =  AddressBookProtos.Person.newBuilder();

        person.setId(1);
        person.setName("Lilei");
        person.setEmail("fqyang@163.com");

        AddressBookProtos.Person.PhoneNumber.Builder MobileNumber =
                AddressBookProtos.Person.PhoneNumber.newBuilder().setNumber("4099134756");
        MobileNumber.setType(AddressBookProtos.Person.PhoneType.MOBILE);
        person.addPhones(MobileNumber);

        AddressBookProtos.Person.PhoneNumber.Builder HomeNumber =
                AddressBookProtos.Person.PhoneNumber.newBuilder().setNumber("10086");
        HomeNumber.setType(AddressBookProtos.Person.PhoneType.HOME);
        person.addPhones(HomeNumber);

        FileOutputStream output = null;
        try {
            output = new FileOutputStream("C:\\D_study\\Protobuf\\exercise\\demo1.txt");
            person.build().writeTo(output);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void DeserializeFromFile(){
        String MessageFile = "C:\\D_study\\Protobuf\\exercise\\demo1.txt";
        // Read the existing address book.
        try {
            AddressBookProtos.Person person =
                    AddressBookProtos.Person.parseFrom(new FileInputStream(MessageFile));

            System.out.println("Person ID: " + person.getId());
            System.out.println("Name: " + person.getName());
            System.out.println("Email: " + person.getEmail());

            for (AddressBookProtos.Person.PhoneNumber phoneNumber : person.getPhonesList()) {
                switch (phoneNumber.getType()) {
                    case MOBILE:
                        System.out.print("  Mobile phone #: ");
                        break;
                    case HOME:
                        System.out.print("  Home phone #: ");
                        break;
                    case WORK:
                        System.out.print("  Work phone #: ");
                        break;
                }
                System.out.println(phoneNumber.getNumber());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void FromAndToByteArray(){
        AddressBookProtos.Person.Builder person =  AddressBookProtos.Person.newBuilder();

        person.setId(1);
        person.setName("Lilei");
        person.setEmail("fqyang@163.com");

        AddressBookProtos.Person.PhoneNumber.Builder MobileNumber =
                AddressBookProtos.Person.PhoneNumber.newBuilder().setNumber("4099134756");
        MobileNumber.setType(AddressBookProtos.Person.PhoneType.MOBILE);
        person.addPhones(MobileNumber);

        AddressBookProtos.Person.PhoneNumber.Builder HomeNumber =
                AddressBookProtos.Person.PhoneNumber.newBuilder().setNumber("10086");
        HomeNumber.setType(AddressBookProtos.Person.PhoneType.HOME);
        person.addPhones(HomeNumber);

        byte[] result = person.build().toByteArray();
        System.out.println("byte array len: " + result.length);

        try {
            AddressBookProtos.Person PersonCopy = AddressBookProtos.Person.parseFrom(result);

            System.out.println("Person ID: " + PersonCopy.getId());
            System.out.println("Name: " + PersonCopy.getName());
            System.out.println("Email: " + PersonCopy.getEmail());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
